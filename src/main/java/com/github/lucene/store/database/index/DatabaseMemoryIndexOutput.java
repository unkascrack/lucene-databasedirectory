package com.github.lucene.store.database.index;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

import org.apache.lucene.store.IOContext;
import org.apache.lucene.store.IndexOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.lucene.store.database.DatabaseDirectory;
import com.github.lucene.store.database.DatabaseDirectoryException;
import com.github.lucene.store.database.handler.DatabaseDirectoryHandler;

/**
 * An <code>IndexOutput</code> implemenation that stores all the data written to it in memory, and flushes it to the
 * database when the output is closed.
 * <p/>
 * Useful for small file entries like the segment file.
 *
 */
public class DatabaseMemoryIndexOutput extends IndexOutput {

    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseMemoryIndexOutput.class);
    private static final DatabaseDirectoryHandler handler = DatabaseDirectoryHandler.INSTANCE;

    private final String name;
    private final DatabaseDirectory directory;
    @SuppressWarnings("unused")
    private final IOContext context;

    private final ByteArrayOutputStream baos = new ByteArrayOutputStream();
    private final Checksum digest = new CRC32();
    private long pos = 0;

    public DatabaseMemoryIndexOutput(final DatabaseDirectory directory, final String name, final IOContext context)
            throws DatabaseDirectoryException {
        super(name);
        this.directory = directory;
        this.name = name;
        this.context = context;
    }

    @Override
    public long getFilePointer() {
        LOGGER.trace("{}.getFilePointer()", this);
        return pos;
    }

    @Override
    public long getChecksum() throws IOException {
        LOGGER.trace("{}.getChecksum()", this);
        return digest.getValue();
    }

    @Override
    public void writeByte(final byte b) throws IOException {
        LOGGER.trace("{}.writeByte({})", this, b);
        baos.write(b);
        digest.update(b);
        pos++;
    }

    @Override
    public void writeBytes(final byte[] b, final int offset, final int length) throws IOException {
        LOGGER.trace("{}.writeBytes({}, {}, {})", this, b, offset, length);
        baos.write(b, offset, length);
        digest.update(b, offset, length);
        pos += length;
    }

    @Override
    public void close() throws IOException {
        LOGGER.trace("{}.close()", this);
        final byte[] buffer = baos.toByteArray();
        handler.saveFile(directory, name, buffer, buffer.length);
    }

    @Override
    public String toString() {
        return new StringBuilder().append(this.getClass().getSimpleName()).append(":").append(directory).append("/")
                .append(name).toString();
    }
}
