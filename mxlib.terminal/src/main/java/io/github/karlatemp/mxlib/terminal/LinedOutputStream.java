/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-terminal.main/LinedOutputStream.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.terminal;

import org.jline.reader.LineReader;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

public class LinedOutputStream extends OutputStream {

    public interface StringAllocator {
        String string(byte[] buffer, int offset, int size, Charset charset);
    }

    public static final StringAllocator DEFAULT_STRING_ALLOCATOR = String::new;

    private final byte[] buffer;
    private final LineReader reader;
    private final Charset charset;
    private final StringAllocator allocator;
    private int counter;
    private final int bufferLen1;

    public LinedOutputStream(LineReader reader, int size, Charset charset) {
        this(reader, size, charset, DEFAULT_STRING_ALLOCATOR);
    }

    public LinedOutputStream(LineReader reader, int size, Charset charset, StringAllocator allocator) {
        if (size < 1) throw new IllegalArgumentException();
        this.reader = reader;
        this.buffer = new byte[size];
        bufferLen1 = size - 1;
        this.charset = charset;
        this.allocator = allocator;
    }

    @Override
    public synchronized void write(int b) throws IOException {
        if ((b & 0xFF) == 10) {
            flush();
        } else if (counter == bufferLen1) {
            flush();
            buffer[counter++] = (byte) b;
        }
    }

    @Override
    public synchronized void write(byte[] b, int off, int len) throws IOException {
        int ed = off + len;
        if (off < 0 || len < 0 || ed > b.length) throw new IllegalArgumentException();
        while (off < ed) {
            int to = -1;
            for (int i = off; i < ed; i++) {
                if ((b[i] & 0xFF) == 10) {
                    to = i;
                    break;
                }
            }
            boolean newLine = false;
            boolean skipLn = false;
            if (to == -1) {
                to = ed;
            } else {
                newLine = true;
                skipLn = true;
            }
            int writeSize = to - off;
            int overloads = counter + writeSize - buffer.length;
            if (overloads > 0) {
                newLine = true;
                skipLn = false;
                to = off + buffer.length - counter;
            }
            if (to > off) {
                int cpc = to - off;
                System.arraycopy(b, off, buffer, counter, cpc);
                counter += cpc;
            }
            if (newLine) flush();
            off = to;
            if (skipLn) {
                off++;
            }
        }
    }

    @Override
    public synchronized void flush() throws IOException {
        if (counter == 0) return;
        reader.printAbove(allocator.string(buffer, 0, counter, charset));
        counter = 0;
    }
}
