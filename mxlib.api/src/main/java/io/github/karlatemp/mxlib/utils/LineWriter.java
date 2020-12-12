/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: LineWriter.java@author: karlatemp@vip.qq.com: 19-11-9 下午2:14@version: 2.0
 */

package io.github.karlatemp.mxlib.utils;

import org.jetbrains.annotations.Contract;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * A Toolkit to write lines.
 *
 * @since 2.5
 */
public interface LineWriter {
    void println(String line);

    default void println(Object line) {
        println(String.valueOf(line));
    }

    @Contract("null -> null")
    static LineWriter from(Object out) {
        if (out == null) return null;
        if (out instanceof LineWriter) return (LineWriter) out;
        if (out instanceof PrintStream) {
            return ((PrintStream) out)::println;
        }
        if (out instanceof PrintWriter) {
            return ((PrintWriter) out)::println;
        }
        if (out instanceof RandomAccessFile) {
            RandomAccessFile file = (RandomAccessFile) out;
            return (line) -> {
                try {
                    ByteBuffer encoded = StandardCharsets.UTF_8.encode(line);
                    // If unset position then external editor cannot edit the file.
                    file.getChannel().position(file.length()).write(encoded);
                    file.seek(file.length());
                    file.write('\n');
                } catch (IOException e) {
                    throw new IOError(e);
                }
            };
        }
        if (out instanceof DataOutput) {
            DataOutput dox = (DataOutput) out;
            return x -> {
                ByteBuffer encoded = StandardCharsets.UTF_8.encode(x);
                byte[] array = new byte[encoded.remaining()];
                encoded.get(array);
                try {
                    dox.write(array);
                    dox.write('\n');
                } catch (IOException e) {
                    throw new IOError(e);
                }
            };
        }
        if (out instanceof Appendable) {
            Appendable wr = (Appendable) out;
            return (x) -> {
                try {
                    wr.append(x).append('\n');
                } catch (IOException e) {
                    throw new IOError(e);
                }
            };
        }
        if (out instanceof OutputStream) {
            return from(new OutputStreamWriter((OutputStream) out, StandardCharsets.UTF_8));
        }
        if (out instanceof WritableByteChannel) {
            WritableByteChannel channel = (WritableByteChannel) out;
            final ByteBuffer b = ByteBuffer.allocate(1);
            return (x) -> {
                ByteBuffer encoded = StandardCharsets.UTF_8.encode(x);
                try {
                    channel.write(encoded);
                    b.clear();
                    b.put((byte) 10);
                    b.flip();
                    channel.write(b);
                } catch (IOException e) {
                    throw new IOError(e);
                }
            };
        }
        try {
            if (out instanceof File) {
                return from(new OutputStreamWriter(new FileOutputStream((File) out), StandardCharsets.UTF_8));
            }
            if (out instanceof Path) {
                return from(Files.newBufferedWriter((Path) out));
            }
        } catch (IOException e) {
            throw new IOError(e);
        }

        throw new UnsupportedOperationException("Unsupported stream " + out);
    }
}
