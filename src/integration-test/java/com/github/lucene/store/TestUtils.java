package com.github.lucene.store;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;

public class TestUtils {

    public static IndexWriterConfig getIndexWriterConfig(final Analyzer analyzer, final OpenMode openMode,
            final boolean useCompoundFile) {
        final IndexWriterConfig config = new IndexWriterConfig(analyzer);
        config.setOpenMode(openMode);
        config.setUseCompoundFile(useCompoundFile);
        config.setInfoStream(System.err);
        return config;
    }
}
