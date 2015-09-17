package com.github.lucene.store.database;

import java.io.IOException;
import java.text.ParseException;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.junit.Assert;
import org.junit.Test;

import com.github.lucene.store.AbstractSpringContextIntegrationTests;
import com.github.lucene.store.CreateJavaTestIndex;
import com.github.lucene.store.TestUtils;

public class DatabaseDirectoryIndexAddITest extends AbstractSpringContextIntegrationTests {

    private static final OpenMode openMode = OpenMode.CREATE_OR_APPEND;
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

    @Test
    public void addMultipleDocuments() throws IOException, ParseException {
        final long start = System.currentTimeMillis();
        CreateJavaTestIndex.populate(directory, analyzer);
        final long stop = System.currentTimeMillis();
        System.out.println("DatabaseDirectory Time : " + (stop - start) + " ms");

        final IndexWriterConfig config = TestUtils.getIndexWriterConfig(analyzer, openMode, useCompoundFile);
        final IndexWriter writer = new IndexWriter(directory, config);
        writer.forceMerge(1);
        writer.close();

        final DirectoryReader reader = DirectoryReader.open(directory);
        final IndexSearcher isearcher = new IndexSearcher(reader);
        final Term term = new Term("fileName", "AbstractDatabaseConfigTest.java");
        final Query query = new TermQuery(term);
        final TopDocs docs = isearcher.search(query, 10);
        Assert.assertEquals(1, docs.totalHits);
        reader.close();

    }
}
