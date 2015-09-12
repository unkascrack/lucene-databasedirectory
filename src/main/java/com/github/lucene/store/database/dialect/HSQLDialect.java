package com.github.lucene.store.database.dialect;

import java.io.IOException;

import org.apache.lucene.store.LockFactory;

import com.github.lucene.store.database.DatabasePhantomReadLockFactory;

public class HSQLDialect extends Dialect {

    private static final String DIALECT_CONFIG = "hsqldialect.sql";

    public HSQLDialect() throws IOException {
        super(DIALECT_CONFIG);
    }

    @Override
    public boolean supportsTableExists() {
        return true;
    }

    @Override
    public LockFactory getLockFactory() {
        return DatabasePhantomReadLockFactory.INSTANCE;
    }
}
