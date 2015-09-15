package com.github.lucene.store.database;

import java.io.IOException;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.junit.Test;

import com.github.lucene.store.AbstractSpringContextIntegrationTests;
import com.github.lucene.store.TestUtils;

public class DatabaseDirectoryIndexAddITest extends AbstractSpringContextIntegrationTests {

    private static final OpenMode openMode = OpenMode.CREATE;
    private static final boolean useCompoundFile = false;

    @Test
    public void addDocument() throws IOException {
        final IndexWriterConfig config = TestUtils.getIndexWriterConfig(analyzer, openMode, useCompoundFile);
        final IndexWriter writer = new IndexWriter(directory, config);
        final Document doc = new Document();
        doc.add(new StringField("index_store_unanalyzed", "index store unanalyzed", Field.Store.YES));
        doc.add(new StoredField("unindexed_store_unanalyzed", "unindexed store unanalyzed"));
        doc.add(new StringField("index_unstore_unanalyzed", "index unstore unanalyzed", Field.Store.NO));
        doc.add(new TextField("index_store_analyzed", "index store analyzed", Field.Store.YES));
        doc.add(new TextField("index_unstore_analyzed", "index unstore analyzed", Field.Store.NO));
        writer.addDocument(doc);
        writer.close();
    }
}
