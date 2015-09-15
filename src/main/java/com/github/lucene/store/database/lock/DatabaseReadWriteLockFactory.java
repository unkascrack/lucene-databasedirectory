package com.github.lucene.store.database.lock;

import java.io.IOException;

import org.apache.lucene.store.AlreadyClosedException;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.Lock;
import org.apache.lucene.store.LockFactory;
import org.apache.lucene.store.LockObtainFailedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.lucene.store.database.DatabaseDirectory;
import com.github.lucene.store.database.DatabaseDirectoryException;
import com.github.lucene.store.database.handler.DatabaseDirectoryHandler;

public class DatabaseReadWriteLockFactory extends LockFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseReadWriteLockFactory.class);
    private static final DatabaseDirectoryHandler handler = DatabaseDirectoryHandler.INSTANCE;

    public static final LockFactory INSTANCE = new DatabaseReadWriteLockFactory();

    private DatabaseReadWriteLockFactory() {
    }

    @Override
    public Lock obtainLock(final Directory dir, final String lockName) throws IOException {
        LOGGER.info("{}.obtainLock({}, {})", this, dir, lockName);

        final DatabaseDirectory directory = (DatabaseDirectory) dir;
        try {
            if (handler.existsFile(directory, lockName)) {
                throw new LockObtainFailedException("Lock instance already obtained: " + directory);
            }
            handler.saveFile(directory, lockName, null, 0);
            return new DatabaseReadWriteLock(directory, lockName);
        } catch (final DatabaseDirectoryException e) {
            throw new LockObtainFailedException("Lock instance already obtained: " + directory);
        }
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

    public static final class DatabaseReadWriteLock extends Lock {

        private final DatabaseDirectory directory;
        private final String name;
        private volatile boolean closed;

        public DatabaseReadWriteLock(final DatabaseDirectory directory, final String name) {
            this.directory = directory;
            this.name = name;
        }

        @Override
        public void ensureValid() throws IOException {
            LOGGER.debug("{}.ensureValid()", this);
            if (closed) {
                throw new AlreadyClosedException("Lock instance already released: " + this);
            }
            if (!handler.existsFile(directory, name)) {
                throw new AlreadyClosedException("Lock instance already released: " + this);
            }
        }

        @Override
        public void close() throws IOException {
            LOGGER.debug("{}.close()", this);
            if (!closed) {
                handler.deleteFile(directory, name);
                closed = true;
            }
        }

        @Override
        public String toString() {
            return this.getClass().getSimpleName();
        }
    }

}
