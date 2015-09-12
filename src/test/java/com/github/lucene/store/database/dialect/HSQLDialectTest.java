package com.github.lucene.store.database.dialect;

import java.io.IOException;

import org.apache.lucene.store.LockFactory;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.github.lucene.store.database.DatabasePhantomReadLockFactory;

public class HSQLDialectTest {

    private static Dialect dialect;
    private static final String TABLENAME = "tableName";

    @BeforeClass
    public static void setUp() throws IOException {
        dialect = new HSQLDialect();
    }

    @Test
    public void supportsTableExists_shouldReturnTrue() {
        Assert.assertTrue(dialect.supportsTableExists());
    }

    @Test
    public void getLockFactory_shouldReturnLockPhantomReadLockFactory() {
        final LockFactory lockFactory = dialect.getLockFactory();
        Assert.assertNotNull(lockFactory);
        Assert.assertEquals(DatabasePhantomReadLockFactory.class, lockFactory.getClass());
    }

    @Test
    public void sqlSelectAll() {
        final String sql = dialect.sqlSelectAll(TABLENAME);
        Assert.assertNotNull(sql);
        Assert.assertTrue(sql.contains(TABLENAME));
    }
}
