package com.github.lucene.store.database;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

import javax.sql.DataSource;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.IOContext;
import org.apache.lucene.store.IndexInput;
import org.apache.lucene.store.IndexOutput;
import org.apache.lucene.store.Lock;
import org.apache.lucene.store.LockFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.lucene.store.database.dialect.Dialect;
import com.github.lucene.store.database.handler.DatabaseDirectoryHandler;
import com.github.lucene.store.database.index.DatabaseMemoryIndexInput;
import com.github.lucene.store.database.index.DatabaseMemoryIndexOutput;

public class DatabaseDirectory extends Directory {

    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseDirectory.class);

    /**
     *
     */
    private static final DatabaseDirectoryHandler handler = DatabaseDirectoryHandler.INSTANCE;

    private final String indexTableName;
    private final DataSource dataSource;
    private final Dialect dialect;
    private final LockFactory lockFactory;

    /**
     * @param dataSource
     * @param dialect
     * @param indexTableName
     * @throws DatabaseDirectoryException
     */
    public DatabaseDirectory(final DataSource dataSource, final Dialect dialect, final String indexTableName)
            throws DatabaseDirectoryException {
        this.dataSource = dataSource;
        this.dialect = dialect;
        this.indexTableName = indexTableName;
        lockFactory = dialect.getLockFactory();

        // create directory, if it doesn't exist
        if (dialect.supportsTableExists() && !handler.existsIndexTable(this)) {
            LOGGER.info("{}: creating lucene index table", this);
            handler.createIndexTable(this);
        }
    }

    public String getIndexTableName() {
        return indexTableName;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public Dialect getDialect() {
        return dialect;
    }

    @Override
    public String[] listAll() throws IOException {
        final String[] files = handler.listAllFiles(this);
        LOGGER.debug("{}.listAll() = {}", this, Arrays.toString(files));
        return files;
    }

    @Override
    public void deleteFile(final String name) throws IOException {
        LOGGER.debug("{}.deleteFile({})", this, name);
        handler.deleteFile(this, name);
    }

    @Override
    public long fileLength(final String name) throws IOException {
        final long length = handler.fileLength(this, name);
        LOGGER.debug("{}.fileLength({}) = {}", this, name, length);
        return length;
    }

    @Override
    public IndexOutput createOutput(final String name, final IOContext context) throws IOException {
        LOGGER.debug("{}.createOutput({}, {})", this, name, context);
        return new DatabaseMemoryIndexOutput(this, name, context);
    }

    @Override
    public void sync(final Collection<String> names) throws IOException {
        LOGGER.debug("{}.sync({})", this, names);
        handler.syncFiles(this, names);
    }

    @Override
    public void renameFile(final String source, final String dest) throws IOException {
        LOGGER.debug("{}.renameFile({}, {})", this, source, dest);
        handler.renameFile(this, source, dest);
    }

    @Override
    public IndexInput openInput(final String name, final IOContext context) throws IOException {
        LOGGER.debug("{}.openInput({}, {})", this, name, context);
        return new DatabaseMemoryIndexInput(this, name, context);
    }

    @Override
    public Lock obtainLock(final String name) throws IOException {
        LOGGER.debug("{}.obtainLock({})", this, name);
        return lockFactory.obtainLock(this, name);
    }

    @Override
    public void close() throws IOException {
        // TODO do nothing??
        LOGGER.debug("{}.close()", this);
    }

    @Override
    public String toString() {
        return new StringBuilder().append(this.getClass().getSimpleName()).append("@").append(indexTableName)
                .toString();
    }
}
