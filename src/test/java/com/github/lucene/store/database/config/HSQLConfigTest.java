package com.github.lucene.store.database.config;

import org.apache.lucene.store.LockFactory;
import org.junit.Assert;
import org.junit.Test;

import com.github.lucene.store.database.config.DatabaseConfig;
import com.github.lucene.store.database.config.HSQLConfig;
import com.github.lucene.store.database.lock.DatabaseReadWriteLockFactory;

public class HSQLConfigTest {

    private static final String TABLENAME = "tableName";

    private final DatabaseConfig config = new HSQLConfig();

    @Test
    public void supportsTableExists_shouldReturnTrue() {
        Assert.assertTrue(config.supportsTableExists());
    }

    @Test
    public void getLockFactory_shouldReturnLockReadWriteLockFactory() {
        final LockFactory lockFactory = config.getLockFactory();
        Assert.assertNotNull(lockFactory);
        Assert.assertEquals(DatabaseReadWriteLockFactory.class, lockFactory.getClass());
    }

    @Test
    public void sqlSelectAll() {
        final String sql = config.sqlSelectAll(TABLENAME);
        Assert.assertNotNull(sql);
        Assert.assertTrue(sql.contains(TABLENAME));
    }
}
