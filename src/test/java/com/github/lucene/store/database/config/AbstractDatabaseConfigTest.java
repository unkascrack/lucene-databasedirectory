package com.github.lucene.store.database.config;

import org.apache.lucene.store.LockFactory;
import org.junit.Assert;
import org.junit.Test;

import com.github.lucene.store.database.lock.DatabaseReadWriteLockFactory;

public abstract class AbstractDatabaseConfigTest {

    private static final String TABLENAME = "tableName";

    private final DatabaseConfig databaseConfig;

    public AbstractDatabaseConfigTest(final DatabaseConfig databaseConfig) {
        this.databaseConfig = databaseConfig;
    }

    @Test
    public void supportsTableExists_shouldReturnTrue() {
        Assert.assertTrue(databaseConfig.supportsTableExists());
    }

    @Test
    public void getLockFactory_shouldReturnLockReadWriteLockFactory() {
        final LockFactory lockFactory = databaseConfig.getLockFactory();
        Assert.assertNotNull(lockFactory);
        Assert.assertEquals(DatabaseReadWriteLockFactory.class, lockFactory.getClass());
    }

    @Test
    public void sqlTableCreate() {
        final String sqlTableCreate = databaseConfig.sqlTableCreate(TABLENAME);
        Assert.assertNotNull(sqlTableCreate);
        Assert.assertTrue(sqlTableCreate.contains(TABLENAME));
    }

    @Test
    public void sqlSelectAll() {
        final String sqlSelectAll = databaseConfig.sqlSelectAll(TABLENAME);
        Assert.assertNotNull(sqlSelectAll);
        Assert.assertTrue(sqlSelectAll.contains(TABLENAME));
    }

    @Test
    public void sqlSelectName() {
        final String sqlSelectName = databaseConfig.sqlSelectName(TABLENAME);
        Assert.assertNotNull(sqlSelectName);
        Assert.assertTrue(sqlSelectName.contains(TABLENAME));
    }

    @Test
    public void sqlSelectSize() {
        final String sqlSelectSize = databaseConfig.sqlSelectSize(TABLENAME);
        Assert.assertNotNull(sqlSelectSize);
        Assert.assertTrue(sqlSelectSize.contains(TABLENAME));
    }

    @Test
    public void sqlSelectContent() {
        final String sqlSelectContent = databaseConfig.sqlSelectContent(TABLENAME);
        Assert.assertNotNull(sqlSelectContent);
        Assert.assertTrue(sqlSelectContent.contains(TABLENAME));
    }

    @Test
    public void sqlInsert() {
        final String sqlInsert = databaseConfig.sqlInsert(TABLENAME);
        Assert.assertNotNull(sqlInsert);
        Assert.assertTrue(sqlInsert.contains(TABLENAME));
    }

    @Test
    public void sqlUpdate() {
        final String sqlUpdate = databaseConfig.sqlUpdate(TABLENAME);
        Assert.assertNotNull(sqlUpdate);
        Assert.assertTrue(sqlUpdate.contains(TABLENAME));
    }

    @Test
    public void sqlDelete() {
        final String sqlDelete = databaseConfig.sqlDelete(TABLENAME);
        Assert.assertNotNull(sqlDelete);
        Assert.assertTrue(sqlDelete.contains(TABLENAME));
    }
}
