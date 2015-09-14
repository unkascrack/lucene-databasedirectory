package com.github.lucene.store;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import com.github.lucene.store.database.dialect.Dialect;
import com.github.lucene.store.database.dialect.HSQLDialect;

@ContextConfiguration(locations = { "classpath:spring-config-test.xml" })
@TestExecutionListeners({ TransactionalTestExecutionListener.class, DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class })
@TransactionConfiguration(defaultRollback = true)
@Transactional
public abstract class AbstractSpringContextIntegrationTests extends AbstractJUnit4SpringContextTests {

    @Resource
    protected DataSource dataSource;

    protected final String indexTableName = "INDEX_TABLE";
    protected final Dialect dialect = new HSQLDialect();
    protected final Analyzer analyzer = new SimpleAnalyzer();

}
