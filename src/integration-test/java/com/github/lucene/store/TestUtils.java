package com.github.lucene.store;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;

public class TestUtils {

    public static Collection<String> loadDocuments(final int numDocs, final int wordsPerDoc) {
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

    public static void addDocuments(final Directory directory, final Analyzer analyzer, final OpenMode openMode,
            final boolean useCompoundFile, final Collection<String> docs) throws IOException {
        final IndexWriterConfig config = getIndexWriterConfig(analyzer, openMode, useCompoundFile);
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

    public static void optimize(final Directory directory, final Analyzer analyzer, final OpenMode openMode,
            final boolean useCompoundFile) throws IOException {
        final IndexWriterConfig config = getIndexWriterConfig(analyzer, openMode, useCompoundFile);
        final IndexWriter writer = new IndexWriter(directory, config);
        writer.forceMerge(1);
        writer.close();
    }

    public static IndexWriterConfig getIndexWriterConfig(final Analyzer analyzer, final OpenMode openMode,
            final boolean useCompoundFile) {
        final IndexWriterConfig config = new IndexWriterConfig(analyzer);
        config.setOpenMode(openMode);
        config.setUseCompoundFile(useCompoundFile);
        config.setInfoStream(System.err);
        return config;
    }
}
