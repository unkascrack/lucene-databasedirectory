package com.github.lucene.store;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.Analyzer;

/**
 * Copyright Manning Publications Co.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific lan
*/

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;

public class CreateJavaTestIndex {

    private static final OpenMode openMode = OpenMode.CREATE;
    private static final boolean useCompoundFile = false;

    public static void populate(final Directory directory, final Analyzer analyzer) throws IOException, ParseException {
        final String dataDir = new File("src").getAbsolutePath();
        final List<File> results = new ArrayList<File>();
        findFiles(results, new File(dataDir));

        final IndexWriterConfig config = TestUtils.getIndexWriterConfig(analyzer, openMode, useCompoundFile);
        final IndexWriter writer = new IndexWriter(directory, config);
        for (final File file : results) {
            final Document doc = getDocument(dataDir, file);
            writer.addDocument(doc);
        }
        writer.close();
    }

    private static Document getDocument(final String rootDir, final File file) throws IOException, ParseException {
        final String fileName = file.getName();
        final String filePath = file.getAbsolutePath();
        final String content = FileUtils.readFileToString(file);

        System.out.println(fileName + ":" + filePath + "\n---------");

        final Document doc = new Document();
        doc.add(new StringField("fileName", fileName, Field.Store.YES));
        doc.add(new StringField("filePath", filePath, Field.Store.YES));
        doc.add(new TextField("content", content, Field.Store.NO));
        return doc;
    }

    private static void findFiles(final List<File> result, final File dir) {
        for (final File file : dir.listFiles()) {
            if (file.getName().endsWith(".java")) {
                result.add(file);
            } else if (file.isDirectory()) {
                findFiles(result, file);
            }
        }
    }

}
