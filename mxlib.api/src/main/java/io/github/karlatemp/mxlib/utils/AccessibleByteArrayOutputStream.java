package io.github.karlatemp.mxlib.utils;

import java.io.ByteArrayOutputStream;

public class AccessibleByteArrayOutputStream extends ByteArrayOutputStream {
    public AccessibleByteArrayOutputStream() {
    }

    public AccessibleByteArrayOutputStream(int size) {
        super(size);
    }

    public byte[] getBuf() {
        return super.buf;
    }

    public void setBuf(byte[] buf) {
        super.buf = buf;
    }

    public int getPosition() {
        return super.count;
    }

    public void setPosition(int position) {
        super.count = position;
    }
}
