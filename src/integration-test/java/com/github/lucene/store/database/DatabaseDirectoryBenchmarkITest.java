package com.github.lucene.store.database;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.util.Collection;

import org.apache.lucene.index.CheckIndex;
import org.apache.lucene.index.CheckIndex.Status;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;
import org.junit.Before;
import org.junit.Test;

import com.github.lucene.store.AbstractSpringContextIntegrationTests;
import com.github.lucene.store.TestUtils;

public class DatabaseDirectoryBenchmarkITest extends AbstractSpringContextIntegrationTests {

    private Directory fsDirectory;
    private Directory ramDirectory;
    private Directory databaseDirectory;

    private final Collection<String> docs = TestUtils.loadDocuments(3000, 5);
    private final OpenMode openMode = OpenMode.CREATE_OR_APPEND;
    private final boolean useCompoundFile = false;

    @Before
    public void setUp() throws Exception {
        ramDirectory = new RAMDirectory();
        fsDirectory = FSDirectory.open(FileSystems.getDefault().getPath("target/index"));
        databaseDirectory = new DatabaseDirectory(dataSource, dialect, indexTableName);
    }

    @Test
    public void testTiming() throws IOException {
        final long ramTiming = timeIndexWriter(ramDirectory);
        final long fsTiming = timeIndexWriter(fsDirectory);
        final long databaseTiming = timeIndexWriter(databaseDirectory);

        System.out.println("RAMDirectory Time: " + ramTiming + " ms");
        System.out.println("FSDirectory Time : " + fsTiming + " ms");
        System.out.println("DatabaseDirectory Time : " + databaseTiming + " ms");
    }

    private long timeIndexWriter(final Directory dir) throws IOException {
        final long start = System.currentTimeMillis();
        TestUtils.addDocuments(dir, analyzer, openMode, useCompoundFile, docs);
        TestUtils.addDocuments(dir, analyzer, openMode, useCompoundFile, docs);

        if (dir instanceof DatabaseDirectory) {
            final CheckIndex c = new CheckIndex(dir);
            c.setInfoStream(System.err);
            final Status status = c.checkIndex();
            c.close();
            System.out.println(status);
        }

        TestUtils.optimize(dir, analyzer, openMode, useCompoundFile);
        final long stop = System.currentTimeMillis();
        return stop - start;
    }
}
