package io.github.karlatemp.mxlib.utils;

import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;

public class RafOutputStream extends OutputStream {
    private RandomAccessFile raf;
    private final boolean closeOnClose;
    private final boolean setLengthOnClose;

    public RafOutputStream(
            RandomAccessFile raf
    ) {
        this(raf, true, true);
    }

    public RafOutputStream(
            RandomAccessFile raf,
            boolean closeOnClose,
            boolean setLengthOnClose
    ) {
        this.raf = raf;
        this.closeOnClose = closeOnClose;
        this.setLengthOnClose = setLengthOnClose;
    }

    @Override
    public void write(int b) throws IOException {
        raf.write(b);
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        raf.write(b, off, len);
    }

    @Override
    public void close() throws IOException {
        if (raf == null) return;
        if (setLengthOnClose) {
            raf.setLength(raf.getFilePointer());
        }
        if (closeOnClose) {
            raf.close();
        }
        raf = null;
    }

    @Override
    public void write(byte[] b) throws IOException {
        raf.write(b);
    }
}
