package com.github.lucene.store;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.DateTools;

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
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;

public class CreateTestIndex {

    private static final OpenMode openMode = OpenMode.CREATE;
    private static final boolean useCompoundFile = false;

    public static void populate(final Directory directory, final Analyzer analyzer) throws IOException, ParseException {
        final String dataDir = new File("target/test-classes/data").getAbsolutePath();
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
        final Properties props = new Properties();
        props.load(new FileInputStream(file));
        String category = file.getParent().substring(rootDir.length());
        category = category.replace(File.separatorChar, '/');

        final String isbn = props.getProperty("isbn");
        final String title = props.getProperty("title");
        final String authors = props.getProperty("author");
        final String url = props.getProperty("url");
        final String subject = props.getProperty("subject");
        final String pubmonth = props.getProperty("pubmonth");
        final Date date = DateTools.stringToDate(pubmonth);
        final String[] contents = new String[] { title, subject, authors, category };

        System.out.println(title + "\n" + authors + "\n" + subject + "\n" + pubmonth + "\n" + category + "\n---------");

        final Document doc = new Document();
        doc.add(new StringField("isbn", isbn, Field.Store.YES));
        doc.add(new StringField("category", category, Field.Store.YES));
        doc.add(new TextField("title", title, Field.Store.YES));
        doc.add(new StringField("title2", title.toLowerCase(), Field.Store.YES));
        for (final String author : authors.split(",")) {
            doc.add(new StringField("author", author, Field.Store.YES));
        }
        doc.add(new StringField("url", url, Field.Store.YES));
        doc.add(new TextField("subject", subject, Field.Store.YES));
        doc.add(new IntField("pubmonth", Integer.parseInt(pubmonth), Field.Store.YES));
        doc.add(new IntField("pubmonthAsDay", (int) (date.getTime() / (1000 * 3600 * 24)), Field.Store.NO));
        for (final String text : contents) {
            doc.add(new TextField("contents", text, Field.Store.NO));
        }
        return doc;
    }

    private static void findFiles(final List<File> result, final File dir) {
        for (final File file : dir.listFiles()) {
            if (file.getName().endsWith(".properties")) {
                result.add(file);
            } else if (file.isDirectory()) {
                findFiles(result, file);
            }
        }
    }

}
