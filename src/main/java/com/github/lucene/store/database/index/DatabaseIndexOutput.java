package com.github.lucene.store.database.index;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

import org.apache.lucene.store.BufferedChecksum;
import org.apache.lucene.store.IOContext;
import org.apache.lucene.store.IndexOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.lucene.store.database.DatabaseDirectory;
import com.github.lucene.store.database.DatabaseDirectoryException;
import com.github.lucene.store.database.handler.DatabaseDirectoryHandler;

public class DatabaseIndexOutput extends IndexOutput {

    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseFileIndexOutput.class);
    private static final DatabaseDirectoryHandler handler = DatabaseDirectoryHandler.INSTANCE;

    private final String name;
    private final DatabaseDirectory directory;
    @SuppressWarnings("unused")
    private final IOContext context;

    private ByteArrayOutputStream baos = new ByteArrayOutputStream();
    private RandomAccessFile file;
    private File tempFile;
    private final Checksum digest = new BufferedChecksum(new CRC32());

    private long pos = 0;

    public DatabaseIndexOutput(final DatabaseDirectory directory, final String name, final IOContext context)
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
        write(new byte[] { b }, 0, 1);
    }

    @Override
    public void writeBytes(final byte[] b, final int offset, final int length) throws IOException {
        LOGGER.trace("{}.writeBytes({}, {}, {})", this, b, offset, length);
        write(b, offset, length);
    }

    private void write(final byte[] b, final int offset, final int length) throws IOException {
        pos += length;
        if (file == null && directory.getConfig().getThreshold() > pos) {
            baos.write(b, offset, length);
        } else {
            if (file == null) {
                tempFile = File.createTempFile(
                        directory.getIndexTableName() + "_" + name + "_" + System.currentTimeMillis(), ".ljt");
                file = new RandomAccessFile(tempFile, "rw");
                file.write(baos.toByteArray(), 0, baos.size());
                baos = null;
            }
            file.write(b, offset, length);
        }
        digest.update(b, offset, length);
    }

    @Override
    public void close() throws IOException {
        LOGGER.trace("{}.close()", this);
        final Object content = getContent();
        final long length = getLength();
        handler.saveFile(directory, name, content, length);
        if (baos != null) {
            baos.close();
        }
        if (file != null) {
            file.close();
            tempFile.delete();
        }
    }

    private Object getContent() throws IOException {
        Object content = null;
        if (file != null) {
            file.seek(0);
            content = new BufferedInputStream(new FileInputStream(file.getFD()));
        } else {
            content = baos.toByteArray();
        }
        return content;
    }

    private long getLength() throws IOException {
        return file != null ? file.length() : baos.size();
    }

    @Override
    public String toString() {
        return new StringBuilder().append(this.getClass().getSimpleName()).append(":").append(directory).append("/")
                .append(name).toString();
    }
}
