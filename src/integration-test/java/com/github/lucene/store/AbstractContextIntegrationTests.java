package com.github.lucene.store;

import java.io.IOException;
import java.io.PrintWriter;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.hsqldb.Server;
import org.hsqldb.persist.HsqlProperties;
import org.hsqldb.server.ServerAcl.AclFormatException;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import com.github.lucene.store.database.config.DatabaseConfig;
import com.github.lucene.store.database.config.HSQLConfig;
import com.github.lucene.store.database.datasource.TransactionAwareDataSourceProxy;

import net.sf.log4jdbc.sql.jdbcapi.DataSourceSpy;

public abstract class AbstractContextIntegrationTests {

    private static Server server;

    protected DataSource dataSource;

    protected final String indexTableName = "INDEX_TABLE";
    protected final DatabaseConfig config = new HSQLConfig();
    protected final Analyzer analyzer = new SimpleAnalyzer();

    @BeforeClass
    public static void initDatabase() throws IOException, AclFormatException {
        final HsqlProperties properties = new HsqlProperties();
        properties.setProperty("server.database.0", "mem:test");
        // properties.setProperty("server.database.0","file:./target/testdb");
        // properties.setProperty("server.dbname.0", "test");
        // properties.setProperty("server.port", "9001");

        server = new Server();
        server.setProperties(properties);
        server.setLogWriter(new PrintWriter(System.out));
        server.setErrWriter(new PrintWriter(System.out));
        server.start();
    }

    @AfterClass
    public static void closeDatabase() {
        server.shutdown();
    }

    @Before
    public void initDataSource() throws Exception {
        final String driverClassName = "org.hsqldb.jdbc.JDBCDriver";
        final String url = "jdbc:hsqldb:mem:test";
        final String username = "sa";
        final String password = "";

        // final HikariConfig config = new HikariConfig();
        // config.setDriverClassName(driverClassName);
        // config.setJdbcUrl(url);
        // config.setUsername(username);
        // config.setPassword(password);
        // config.setAutoCommit(false);
        // final HikariDataSource ds = new HikariDataSource(config);

        // final ConnectionFactory connectionFactory = new
        // DriverManagerConnectionFactory(url, username, password);
        // final PoolableConnectionFactory poolableConnectionFactory = new
        // PoolableConnectionFactory(connectionFactory,
        // null);
        // final ObjectPool<PoolableConnection> connectionPool = new
        // GenericObjectPool<>(poolableConnectionFactory);
        // poolableConnectionFactory.setPool(connectionPool);
        // final DataSource ds = new PoolingDataSource<>(connectionPool);

        final BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName(driverClassName);
        ds.setUrl(url);
        ds.setUsername(username);
        ds.setPassword(password);
        ds.setDefaultAutoCommit(false);

        dataSource = new TransactionAwareDataSourceProxy(new DataSourceSpy(ds));
        // dataSource = new DataSourceSpy(ds);
    }
}
