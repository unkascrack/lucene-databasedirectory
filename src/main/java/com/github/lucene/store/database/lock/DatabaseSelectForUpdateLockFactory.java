package com.github.lucene.store.database.lock;

import java.io.IOException;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.Lock;
import org.apache.lucene.store.LockFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.lucene.store.database.DatabaseDirectory;
import com.github.lucene.store.database.handler.DatabaseDirectoryHandler;

public class DatabaseSelectForUpdateLockFactory extends LockFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseSelectForUpdateLockFactory.class);
    private static final DatabaseDirectoryHandler handler = DatabaseDirectoryHandler.INSTANCE;

    public static final LockFactory INSTANCE = new DatabaseSelectForUpdateLockFactory();

    private DatabaseSelectForUpdateLockFactory() {
    }

    @Override
    public Lock obtainLock(final Directory dir, final String lockName) throws IOException {
        LOGGER.info("{}.obtainLock({}, {})", this, dir, lockName);
        // TODO Auto-generated method stub
        return null;

        // final DatabaseDirectory directory = (DatabaseDirectory) dir;
        // try {
        // handler.saveFile(directory, lockName, null, 0);
        // return new DatabaseSelectForUpdateLock(directory, lockName);
        // } catch (final DatabaseDirectoryException e) {
        // throw new LockObtainFailedException("Lock instance already obtained: " + directory);
        // }
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

    static final class DatabaseSelectForUpdateLock extends Lock {

        private final DatabaseDirectory directory;
        private final String name;
        private volatile boolean closed;

        public DatabaseSelectForUpdateLock(final DatabaseDirectory directory, final String name) {
            this.directory = directory;
            this.name = name;
        }

        @Override
        public void close() throws IOException {
            // TODO Auto-generated method stub

        }

        @Override
        public void ensureValid() throws IOException {
            // TODO Auto-generated method stub

        }

    }

}
