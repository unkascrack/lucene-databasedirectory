package com.github.lucene.store.database.dialect;

import org.apache.lucene.store.LockFactory;

import com.github.lucene.store.database.lock.DatabaseReadWriteLockFactory;

public class HSQLDialect extends Dialect {

    private static final String DIALECT_CONFIG = "hsqldialect.sql";

    public HSQLDialect() {
        super(DIALECT_CONFIG);
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
