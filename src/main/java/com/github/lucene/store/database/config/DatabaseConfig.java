package com.github.lucene.store.database.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.lucene.store.LockFactory;

public abstract class DatabaseConfig {

    private static final String DEFAULT_CONFIG = "config.sql";

    protected static final String PROPERTY_SQL_TABLE_EXISTS = "sql.table.exists";
    protected static final String PROPERTY_SQL_TABLE_CREATE = "sql.table.create";
    protected static final String PROPERTY_SQL_SELECT_ALL = "sql.select.listall";
    protected static final String PROPERTY_SQL_SELECT_NAME = "sql.select.name";
    protected static final String PROPERTY_SQL_SELECT_SIZE = "sql.select.size";
    protected static final String PROPERTY_SQL_SELECT_CONTENT = "sql.select.content";
    protected static final String PROPERTY_SQL_INSERT = "sql.insert";
    protected static final String PROPERTY_SQL_UPDATE = "sql.update";
    protected static final String PROPERTY_SQL_DELETE = "sql.delete";

    protected final Properties properties;

    /**
     * The default value for the threshold (in bytes). Currently 16K.
     */
    public static final long DEFAULT_THRESHOLD = 16 * 1024;

    private long threshold;

    /**
     * @param config
     */
    public DatabaseConfig(final String config) {
        this(config, DEFAULT_THRESHOLD);
    }

    /**
     * @param config
     * @param threshold
     */
    public DatabaseConfig(final String config, final long threshold) {
        try {
            this.threshold = threshold;
            properties = new Properties();
            properties.load(getClass().getResourceAsStream(DEFAULT_CONFIG));
            InputStream stream = getClass().getResourceAsStream(config);
            if (stream == null) {
                stream = getClass().getClassLoader().getResourceAsStream(config);
            }
            properties.load(stream);
        } catch (final IOException e) {
            throw new IllegalArgumentException("Could not load config [" + config + "]: " + e.getMessage());
        }
    }

    /**
     * Does the database support a special query to check if a table exists.
     * Defaults to <code>false</code>.
     *
     * @return
     */
    public abstract boolean supportsTableExists();

    /**
     * @return
     */
    public abstract LockFactory getLockFactory();

    /**
     * @return
     */
    public final long getThreshold() {
        return threshold;
    }

    /**
     * @param threshold
     */
    public final void setThreshold(final long threshold) {
        this.threshold = threshold;
    }

    /**
     * If the database support a special query to check if a table exists, the
     * actual sql that is used to perform it. Defaults to throw an Unsupported
     * excetion (see {@link #supportsTableExists()}.
     *
     * @param tableName
     * @param schemaName
     * @return
     */
    public final String sqlTableExists(final String tableName) {
        return String.format(properties.getProperty(PROPERTY_SQL_TABLE_EXISTS), tableName);
    }

    /**
     * @param tableName
     * @return
     */
    public final String sqlTableCreate(final String tableName) {
        return String.format(properties.getProperty(PROPERTY_SQL_TABLE_CREATE), tableName);
    }

    /**
     * @param tableName
     * @return
     */
    public final String sqlSelectAll(final String tableName) {
        return String.format(properties.getProperty(PROPERTY_SQL_SELECT_ALL), tableName);
    }

    /**
     * @param tableName
     * @return
     */
    public final String sqlSelectName(final String tableName) {
        return String.format(properties.getProperty(PROPERTY_SQL_SELECT_NAME), tableName);
    }

    /**
     * @param tableName
     * @return
     */
    public final String sqlSelectSize(final String tableName) {
        return String.format(properties.getProperty(PROPERTY_SQL_SELECT_SIZE), tableName);
    }

    /**
     * @param tableName
     * @return
     */
    public final String sqlSelectContent(final String tableName) {
        return String.format(properties.getProperty(PROPERTY_SQL_SELECT_CONTENT), tableName);
    }

    /**
     * @param tableName
     * @return
     */
    public final String sqlInsert(final String tableName) {
        return String.format(properties.getProperty(PROPERTY_SQL_INSERT), tableName);
    }

    /**
     * @param tableName
     * @return
     */
    public final String sqlUpdate(final String tableName) {
        return String.format(properties.getProperty(PROPERTY_SQL_UPDATE), tableName);
    }

    /**
     * @param tableName
     * @return
     */
    public final String sqlDeleteByName(final String tableName) {
        return String.format(properties.getProperty(PROPERTY_SQL_DELETE), tableName);
    }
}
