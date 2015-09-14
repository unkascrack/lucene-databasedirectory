package com.github.lucene.store.database;

import java.io.IOException;
import java.util.Collection;

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.github.lucene.store.AbstractSpringContextIntegrationTests;
import com.github.lucene.store.TestUtils;

public class DatabaseDirectoryIndexSearchITest extends AbstractSpringContextIntegrationTests {

    private Directory directory;

    private final Collection<String> docs = TestUtils.loadDocuments(3000, 5);
    private final OpenMode openMode = OpenMode.CREATE;
    private final boolean useCompoundFile = false;

    @Before
    public void initDirectory() throws DatabaseDirectoryException, IOException {
        directory = new DatabaseDirectory(dataSource, dialect, indexTableName);
        // create empty index
        final IndexWriterConfig config = TestUtils.getIndexWriterConfig(analyzer, openMode, useCompoundFile);
        final IndexWriter writer = new IndexWriter(directory, config);
        writer.close();
    }

    @After
    public void closeDirectory() throws IOException {
        directory.close();
    }

    @Test
    public void testSearch_whenIndexIsEmpty_shouldNoFoundResults() throws IOException, ParseException {
        final DirectoryReader reader = DirectoryReader.open(directory);
        final IndexSearcher isearcher = new IndexSearcher(reader);
        // Parse a simple query that searches for "text":

        final QueryParser parser = new QueryParser("fieldname", analyzer);
        final Query query = parser.parse("text");
        final TopDocs hits = isearcher.search(query, 1000);
        Assert.assertEquals(0, hits.totalHits);
        reader.close();
    }

    @Test
    public void testSearch_whenIndexIsNotEmpty() throws IOException, ParseException {
        TestUtils.addDocuments(directory, analyzer, openMode, useCompoundFile, docs);

        final DirectoryReader reader = DirectoryReader.open(directory);
        final IndexSearcher isearcher = new IndexSearcher(reader);
        final QueryParser parser = new QueryParser("index_store_analyzed", analyzer);
        final Query query = parser.parse("bibamus");
        final TopDocs hits = isearcher.search(query, 1000);
        Assert.assertTrue(hits.totalHits > 0);
        reader.close();
    }
}
