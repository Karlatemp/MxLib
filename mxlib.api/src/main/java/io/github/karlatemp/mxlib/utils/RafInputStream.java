/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-api.main/RafInputStream.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

public class RafInputStream extends InputStream {
    private RandomAccessFile raf;
    private final boolean closeOnClose;

    public RafInputStream(RandomAccessFile raf) {
        this(raf, true);
    }

    public RafInputStream(
            RandomAccessFile raf,
            boolean closeOnClose
    ) {
        this.raf = raf;
        this.closeOnClose = closeOnClose;
    }

    @Override
    public int read() throws IOException {
        return raf.read();
    }

    @Override
    public int read(byte[] b) throws IOException {
        return raf.read(b);
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        return raf.read(b, off, len);
    }

    @Override
    public void close() throws IOException {
        RandomAccessFile raf = this.raf;
        this.raf = null;
        if (raf == null) return;
        if (closeOnClose) raf.close();
    }

    @Override
    public int available() throws IOException {
        long len = raf.length(), pos = raf.getFilePointer();
        if (len > pos) {
            int ps = (int) (len - pos);
            if (ps <= 0) {
                return Integer.MAX_VALUE;
            }
            return ps;
        }
        return 0;
    }
}
