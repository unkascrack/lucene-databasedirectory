package com.github.lucene.store.database.datasource;

import java.sql.Connection;

/**
 * Subinterface of Connection to be implemented by connection proxies.
 * <p/>
 * Initial version taken from Spring.
 *
 * @author kimchy
 */
public interface ConnectionProxy extends Connection {

    /**
     * Return the target connection of this proxy.
     * <p/>
     * This will typically either be the native JDBC Connection or a wrapper from a connection pool.
     */
    Connection getTargetConnection();

    /**
     * If the given Jdbc Connection actually controls the connection.
     *
     * @see TransactionAwareDataSourceProxy
     * @see DataSourceUtils#releaseConnection(java.sql.Connection)
     */
    boolean isControlConnection();
}
