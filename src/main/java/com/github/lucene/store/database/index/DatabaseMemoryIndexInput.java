package com.github.lucene.store.database.index;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.apache.lucene.store.BufferedIndexInput;
import org.apache.lucene.store.IOContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.lucene.store.database.DatabaseDirectory;
import com.github.lucene.store.database.DatabaseDirectoryException;
import com.github.lucene.store.database.handler.DatabaseDirectoryHandler;

/**
 * An <code>IndexInput</code> implementation that will read all the relevant data from the database when created, and
 * will cache it untill it is closed.
 * <p/>
 * Used for small file entries in the database like the segments file.
 *
 */
public class DatabaseMemoryIndexInput extends BufferedIndexInput {

    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseMemoryIndexInput.class);
    private static final DatabaseDirectoryHandler handler = DatabaseDirectoryHandler.INSTANCE;

    private final DatabaseDirectory directory;
    private final String name;
    private final ByteBuffer buffer;
    private int pos = 0;

    public DatabaseMemoryIndexInput(final DatabaseDirectory directory, final String name, final IOContext context)
            throws DatabaseDirectoryException {
        super(name, context);
        this.directory = directory;
        this.name = name;
        final byte[] content = handler.fileContent(directory, name);
        buffer = content != null && content.length > 0 ? ByteBuffer.wrap(content) : ByteBuffer.allocate(0);
    }

    @Override
    protected void readInternal(final byte[] b, final int offset, final int length) throws IOException {
        LOGGER.trace("{}.readInternal({}, {}, {})", this, b, offset, length);
        System.arraycopy(buffer.array(), pos, b, offset, length);
        pos += length;
    }

    @Override
    protected void seekInternal(final long pos) throws IOException {
        LOGGER.trace("{}.seekInternal({})", this, pos);
        this.pos = (int) pos;
    }

    @Override
    public void close() throws IOException {
        LOGGER.trace("{}.close()", this);
    }

    @Override
    public long length() {
        LOGGER.trace("{}.length()", this);
        return buffer.limit();
    }

    @Override
    public String toString() {
        return new StringBuilder().append(this.getClass().getSimpleName()).append(":").append(directory).append("/")
                .append(name).toString();
    }
}
