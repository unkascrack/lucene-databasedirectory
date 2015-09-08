package com.github.lucene.store.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

class DataSourceUtils {

    private DataSourceUtils() {
    }

    /**
     * Returns <code>true</code> if the connection was created by the {@link TransactionAwareDataSourceProxy} and it
     * controls the connection (i.e. it is the most outer connection created).
     *
     * @param connection
     * @return
     */
    static boolean isControlConnection(final Connection connection) {
        return connection instanceof ConnectionProxy && ((ConnectionProxy) connection).isControlConnection();
    }

    /**
     * Returns a jdbc connection, and in case of failure, wraps the sql exception with a Jdbc device exception.
     *
     * @param dataSource
     * @return
     * @throws DatabaseStoreException
     */
    static Connection getConnection(final DataSource dataSource) throws DatabaseStoreException {
        try {
            return dataSource.getConnection();
        } catch (final SQLException e) {
            throw new DatabaseStoreException("Failed to open jdbc connection", e);
        }
    }

    /**
     * Close the given JDBC connection and ignore any thrown exception. This is useful for typical finally blocks in
     * manual JDBC code.
     * <p/>
     * Will only close the connection under two conditions: If the connection was not created by the
     * {@link TransactionAwareDataSourceProxy}, or if it was created by {@link TransactionAwareDataSourceProxy}, and the
     * connection controls the connection (i.e. it is the most outer connection created).
     *
     * @param connection
     * @throws DatabaseStoreException
     */
    static void releaseConnection(final Connection connection) throws DatabaseStoreException {
        if (connection == null) {
            return;
        }
        if (!(connection instanceof ConnectionProxy) || isControlConnection(connection)) {
            try {
                connection.close();
            } catch (final SQLException e) {
                throw new DatabaseStoreException("Failed to release jdbc connection", e);
            }
        }
    }

    /**
     * Commits the connection only if the connection is controlled by us. The connection is controlled if it is the
     * <code>TransactionAwareDataSourceProxy</code> and it is the most outer connection in the tree of connections the
     * <code>TransactionAwareDataSourceProxy</code> returned.
     *
     * @param connection
     * @throws DatabaseStoreException
     */
    static void commitConnection(final Connection connection) throws DatabaseStoreException {
        try {
            if (connection != null && isControlConnection(connection)) {
                connection.commit();
            }
        } catch (final SQLException e) {
            rollbackConnection(connection);
            throw new DatabaseStoreException("Failed to commit jdbc connection", e);
        }
    }

    /**
     * Rollbacks the connection only if the connection is controlled by us. The connection is controlled if it is the
     * <code>TransactionAwareDataSourceProxy</code> and it is the most outer connection in the tree of connections the
     * <code>TransactionAwareDataSourceProxy</code> returned.
     *
     * @param connection
     */
    static void rollbackConnection(final Connection connection) {
        try {
            if (connection != null && isControlConnection(connection)) {
                connection.rollback();
            }
        } catch (final SQLException e) {
            // do nothing
        }
    }

    /**
     * Close the given JDBC Statement and ignore any thrown exception. This is useful for typical finally blocks in
     * manual JDBC code.
     *
     * @param statement
     *            the JDBC Statement to close
     */
    static void closeStatement(final Statement statement) {
        if (statement != null) {
            try {
                statement.close();
            } catch (final SQLException ex) {
            }
        }
    }

    /**
     * Close the given JDBC ResultSet and ignore any thrown exception. This is useful for typical finally blocks in
     * manual JDBC code.
     *
     * @param resultSet
     *            the JDBC ResultSet to close
     */
    static void closeResultSet(final ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (final SQLException ex) {
            }
        }
    }
}
