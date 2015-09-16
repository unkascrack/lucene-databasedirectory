package com.github.lucene.store.database.lock;

import java.io.IOException;
import java.text.ParseException;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.Lock;
import org.apache.lucene.store.LockFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.github.lucene.store.database.DatabaseDirectory;
import com.github.lucene.store.database.DatabaseDirectoryException;
import com.github.lucene.store.database.config.DatabaseConfig;
import com.github.lucene.store.database.config.HSQLConfig;

@ContextConfiguration(locations = { "classpath:spring-config-test.xml" })
@TestExecutionListeners({ TransactionalTestExecutionListener.class, DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class })
@TransactionConfiguration(defaultRollback = true)
public class DatabaseReadWriteLockFactoryITestNG extends AbstractTestNGSpringContextTests {

    private final LockFactory lockFactory = DatabaseReadWriteLockFactory.INSTANCE;

    private static final String indexTableName = "INDEX_TABLE";
    @Resource
    protected DataSource dataSource;
    protected Directory directory;
    protected final DatabaseConfig config = new HSQLConfig();

    @BeforeClass
    public void initDirectory() throws DatabaseDirectoryException, IOException, ParseException {
        directory = new DatabaseDirectory(dataSource, config, indexTableName);
    }

    @AfterClass
    public void closeDirectory() throws IOException {
        directory.close();
    }

    @Autowired
    PlatformTransactionManager transactionManager;

    @Test(threadPoolSize = 5, invocationCount = 5)
    public void obtainLock_shouldReturnLock() throws IOException {
        new TransactionTemplate(transactionManager).execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(final TransactionStatus status) {
                try {
                    status.setRollbackOnly();
                    System.out.println("********************** newTransaction = " + status.isNewTransaction());
                    final Lock lock = lockFactory.obtainLock(directory, IndexWriter.WRITE_LOCK_NAME);
                    Assert.assertNotNull(lock);
                    lock.ensureValid();
                    lock.close();
                } catch (final IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
