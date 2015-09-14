package com.github.lucene.store.database;

import java.io.IOException;

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.junit.Assert;
import org.junit.Test;

import com.github.lucene.store.AbstractSpringContextIntegrationTests;
import com.github.lucene.store.CreateTestIndex;

public class DatabaseDirectoryIndexSearchITest extends AbstractSpringContextIntegrationTests {

    @Override
    public void initDirectory() throws DatabaseDirectoryException, IOException, java.text.ParseException {
        super.initDirectory();
        CreateTestIndex.populate(directory, analyzer);
    }

    @Test
    public void testSearch_whenQuerySearchIsNotFound_shouldNoFoundResults() throws IOException, ParseException {
        final DirectoryReader reader = DirectoryReader.open(directory);
        final IndexSearcher isearcher = new IndexSearcher(reader);
        final QueryParser parser = new QueryParser("author", analyzer);
        final Query query = parser.parse("text");
        final TopDocs hits = isearcher.search(query, 1000);
        Assert.assertEquals(0, hits.totalHits);
        reader.close();
    }

    @Test
    public void testSearch_whenQuerySearchIsFound_shouldReturnResults() throws IOException, ParseException {
        final DirectoryReader reader = DirectoryReader.open(directory);
        final IndexSearcher isearcher = new IndexSearcher(reader);
        final Term term = new Term("isbn", "9781935182023");
        final Query query = new TermQuery(term);
        final TopDocs docs = isearcher.search(query, 10);
        Assert.assertEquals(1, docs.totalHits);
        reader.close();
    }
}
