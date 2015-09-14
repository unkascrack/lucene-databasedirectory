package com.github.lucene.store.database.dialect;

import org.apache.lucene.store.LockFactory;
import org.junit.Assert;
import org.junit.Test;

import com.github.lucene.store.database.lock.DatabasePhantomReadLockFactory;

public class HSQLDialectTest {

    private static final String TABLENAME = "tableName";

    private final Dialect dialect = new HSQLDialect();

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
