package com.github.lucene.store.database.dialect;

import org.apache.lucene.store.LockFactory;
import org.junit.Assert;
import org.junit.Test;

import com.github.lucene.store.database.lock.DatabaseReadWriteLockFactory;

public class HSQLDialectTest {

    private static final String TABLENAME = "tableName";

    private final Dialect dialect = new HSQLDialect();

    @Test
    public void supportsTableExists_shouldReturnTrue() {
        Assert.assertTrue(dialect.supportsTableExists());
    }

    @Test
    public void getLockFactory_shouldReturnLockReadWriteLockFactory() {
        final LockFactory lockFactory = dialect.getLockFactory();
        Assert.assertNotNull(lockFactory);
        Assert.assertEquals(DatabaseReadWriteLockFactory.class, lockFactory.getClass());
    }

    @Test
    public void sqlSelectAll() {
        final String sql = dialect.sqlSelectAll(TABLENAME);
        Assert.assertNotNull(sql);
        Assert.assertTrue(sql.contains(TABLENAME));
    }
}
