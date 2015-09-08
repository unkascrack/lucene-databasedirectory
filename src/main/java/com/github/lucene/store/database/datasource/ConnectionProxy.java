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
     * If the given Jdbc Connection actually controls the connection.
     *
     * @see TransactionAwareDataSourceProxy
     * @see DataSourceUtils#releaseConnection(java.sql.Connection)
     */
    boolean isControlConnection();
}
