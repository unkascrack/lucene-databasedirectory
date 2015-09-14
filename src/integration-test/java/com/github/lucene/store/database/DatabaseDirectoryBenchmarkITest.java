package com.github.lucene.store.database;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.CheckIndex;
import org.apache.lucene.index.CheckIndex.Status;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
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

    private final Collection<String> docs = loadDocuments(3000, 5);
    private final OpenMode openMode = OpenMode.CREATE_OR_APPEND;
    private final boolean useCompoundFile = false;

    @Before
    public void setUp() throws Exception {
        ramDirectory = new RAMDirectory();
        fsDirectory = FSDirectory.open(FileSystems.getDefault().getPath("target/index"));
        databaseDirectory = directory;
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
        addDocuments(dir, analyzer, openMode, useCompoundFile, docs);
        addDocuments(dir, analyzer, openMode, useCompoundFile, docs);

        if (dir instanceof DatabaseDirectory) {
            final CheckIndex c = new CheckIndex(dir);
            c.setInfoStream(System.err);
            final Status status = c.checkIndex();
            c.close();
            System.out.println(status);
        }

        optimize(dir, analyzer, openMode, useCompoundFile);
        final long stop = System.currentTimeMillis();
        return stop - start;
    }

    public Collection<String> loadDocuments(final int numDocs, final int wordsPerDoc) {
        final Collection<String> docs = new ArrayList<String>(numDocs);
        for (int i = 0; i < numDocs; i++) {
            final StringBuffer doc = new StringBuffer(wordsPerDoc);
            for (int j = 0; j < wordsPerDoc; j++) {
                doc.append("Bibamus ");
            }
            docs.add(doc.toString());
        }
        return docs;
    }

    public void addDocuments(final Directory directory, final Analyzer analyzer, final OpenMode openMode,
            final boolean useCompoundFile, final Collection<String> docs) throws IOException {
        final IndexWriterConfig config = TestUtils.getIndexWriterConfig(analyzer, openMode, useCompoundFile);
        final IndexWriter writer = new IndexWriter(directory, config);
        for (final Object element : docs) {
            final Document doc = new Document();
            final String word = (String) element;
            doc.add(new StringField("index_store_unanalyzed", word, Field.Store.YES));
            doc.add(new StoredField("unindexed_store_unanalyzed", word));
            doc.add(new StringField("index_unstore_unanalyzed", word, Field.Store.NO));
            doc.add(new TextField("index_store_analyzed", word, Field.Store.YES));
            doc.add(new TextField("index_unstore_analyzed", word, Field.Store.NO));
            writer.addDocument(doc);
        }
        writer.close();
    }

    public void optimize(final Directory directory, final Analyzer analyzer, final OpenMode openMode,
            final boolean useCompoundFile) throws IOException {
        final IndexWriterConfig config = TestUtils.getIndexWriterConfig(analyzer, openMode, useCompoundFile);
        final IndexWriter writer = new IndexWriter(directory, config);
        writer.forceMerge(1);
        writer.close();
    }
}
