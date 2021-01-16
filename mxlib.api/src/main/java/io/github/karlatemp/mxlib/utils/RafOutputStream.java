/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-api.main/RafOutputStream.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

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
