package com.github.lucene.store.database.config;

/**
 * HSQL Database Config
 *
 */
public class HSQLConfig extends DatabaseConfig {

    private static final String HSQL_CONFIG = "hsql.sql";

    public HSQLConfig() {
        super(HSQL_CONFIG);
    }

    public HSQLConfig(final long threshold) {
        super(HSQL_CONFIG, threshold);
    }
}
