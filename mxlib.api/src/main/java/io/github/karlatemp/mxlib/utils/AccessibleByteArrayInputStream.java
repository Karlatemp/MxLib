package io.github.karlatemp.mxlib.utils;

import java.io.ByteArrayInputStream;

public class AccessibleByteArrayInputStream extends ByteArrayInputStream {
    public AccessibleByteArrayInputStream(byte[] buf) {
        super(buf);
    }

    public AccessibleByteArrayInputStream(byte[] buf, int offset, int length) {
        super(buf, offset, length);
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
