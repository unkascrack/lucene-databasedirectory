package com.github.lucene.store.database;

import java.io.IOException;

import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.AlreadyClosedException;
import org.apache.lucene.store.Lock;
import org.apache.lucene.store.LockFactory;
import org.apache.lucene.store.LockObtainFailedException;
import org.junit.Assert;
import org.junit.Test;

import com.github.lucene.store.AbstractSpringContextIntegrationTests;

public class DatabasePhantomReadLockFactoryITest extends AbstractSpringContextIntegrationTests {

    private final LockFactory lockFactory = DatabasePhantomReadLockFactory.INSTANCE;

    @Test
    public void obtainLock_shouldReturnLock() throws IOException {
        final Lock lock = lockFactory.obtainLock(directory, IndexWriter.WRITE_LOCK_NAME);
        Assert.assertNotNull(lock);
        lock.ensureValid();
        lock.close();
    }

    @Test(expected = AlreadyClosedException.class)
    public void obtainLock_whenLockIsCloseAndEnsureValid_shouldThrowAlreadyClosedException() throws IOException {
        final Lock lock = lockFactory.obtainLock(directory, IndexWriter.WRITE_LOCK_NAME);
        Assert.assertNotNull(lock);
        lock.close();
        lock.ensureValid();
    }

    @Test(expected = LockObtainFailedException.class)
    public void obtainLock_whenOpenNewLockWhenOtherIsOpen_shouldThrowLockObtainFailedException() throws IOException {
        final Lock lock = lockFactory.obtainLock(directory, IndexWriter.WRITE_LOCK_NAME);
        Assert.assertNotNull(lock);
        try {
            lockFactory.obtainLock(directory, IndexWriter.WRITE_LOCK_NAME);
        } finally {
            lock.close();
        }
    }

    @Test
    public void obtainLock_whenLockFileFoundButIsClosed_shouldReturnNewLock() throws IOException {
        final Lock lock1 = directory.obtainLock(IndexWriter.WRITE_LOCK_NAME);
        Assert.assertNotNull(lock1);
        lock1.close();

        final Lock lock2 = directory.obtainLock(IndexWriter.WRITE_LOCK_NAME);
        Assert.assertNotNull(lock2);
        lock2.close();
    }
}
