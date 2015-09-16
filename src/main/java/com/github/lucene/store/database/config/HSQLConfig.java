package com.github.lucene.store.database.config;

import org.apache.lucene.store.LockFactory;

import com.github.lucene.store.database.lock.DatabaseReadWriteLockFactory;

public class HSQLConfig extends DatabaseConfig {

    private static final String HSQL_CONFIG = "hsql.sql";

    public HSQLConfig() {
        super(HSQL_CONFIG);
    }

    public HSQLConfig(final long threshold) {
        super(HSQL_CONFIG, threshold);
    }

    @Override
    public boolean supportsTableExists() {
        return true;
    }

    @Override
    public LockFactory getLockFactory() {
        return DatabaseReadWriteLockFactory.INSTANCE;
    }
}
