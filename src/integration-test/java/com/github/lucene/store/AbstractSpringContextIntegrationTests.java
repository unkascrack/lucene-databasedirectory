package com.github.lucene.store;

import java.io.IOException;
import java.text.ParseException;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.store.Directory;
import org.junit.After;
import org.junit.Before;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import com.github.lucene.store.database.DatabaseDirectory;
import com.github.lucene.store.database.DatabaseDirectoryException;
import com.github.lucene.store.database.config.DatabaseConfig;
import com.github.lucene.store.database.config.HSQLConfig;

@ContextConfiguration(locations = { "classpath:spring-config-test.xml" })
@TestExecutionListeners({ TransactionalTestExecutionListener.class, DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class })
@TransactionConfiguration(defaultRollback = true)
@Transactional
public abstract class AbstractSpringContextIntegrationTests extends AbstractJUnit4SpringContextTests {

    private static final String indexTableName = "INDEX_TABLE";

    @Resource
    protected DataSource dataSource;

    protected Directory directory;
    protected final DatabaseConfig config = new HSQLConfig();
    protected final Analyzer analyzer = new SimpleAnalyzer();

    @Before
    public void initDirectory() throws DatabaseDirectoryException, IOException, ParseException {
        directory = new DatabaseDirectory(dataSource, config, indexTableName);
    }

    @After
    public void closeDirectory() throws IOException {
        directory.close();
    }
}
