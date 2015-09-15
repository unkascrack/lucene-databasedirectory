package com.github.lucene.store.database.index;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
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
 * An <code>IndexOutput</code> implemenation that writes all the data to a temporary file, and when closed, flushes the
 * file to the database.
 * <p/>
 * Usefull for large files that are known in advance to be larger then the acceptable threshold configured.
 *
 */
public class DatabaseFileIndexOutput extends IndexOutput {

    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseFileIndexOutput.class);
    private static final DatabaseDirectoryHandler handler = DatabaseDirectoryHandler.INSTANCE;

    private final String name;
    private final DatabaseDirectory directory;
    @SuppressWarnings("unused")
    private final IOContext context;

    private final RandomAccessFile file;
    private final File tempFile;
    private final Checksum digest = new CRC32();
    private long pos = 0;

    public DatabaseFileIndexOutput(final DatabaseDirectory directory, final String name, final IOContext context)
            throws DatabaseDirectoryException {
        super(name);
        this.directory = directory;
        this.name = name;
        this.context = context;
        try {
            tempFile = File.createTempFile(
                    directory.getIndexTableName() + "_" + name + "_" + System.currentTimeMillis(), ".ljt");
            file = new RandomAccessFile(tempFile, "rw");
        } catch (final IOException e) {
            throw new DatabaseDirectoryException(e);
        }
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
        file.write(b);
        digest.update(b);
        pos++;
    }

    @Override
    public void writeBytes(final byte[] b, final int offset, final int length) throws IOException {
        LOGGER.trace("{}.writeBytes({}, {}, {})", this, b, offset, length);
        file.write(b, offset, length);
        digest.update(b, offset, length);
        pos += length;
    }

    @Override
    public void close() throws IOException {
        LOGGER.trace("{}.close()", this);
        file.seek(0);
        final InputStream stream = new BufferedInputStream(new FileInputStream(file.getFD()));
        handler.saveStream(directory, name, stream, file.length());
        file.close();
        tempFile.delete();
    }

    @Override
    public String toString() {
        return new StringBuilder().append(this.getClass().getSimpleName()).append(":").append(directory).append("/")
                .append(name).toString();
    }
}
