/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-api.main/LineWriter.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.utils;

import org.jetbrains.annotations.Contract;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * A Toolkit to write lines.
 *
 * @since 2.5
 */
public interface LineWriter
        extends AutoCloseable /* @since 3.0-dev-20 */ {
    void println(String line);

    default void println(Object line) {
        println(StringBuilderFormattable.toString(line));
    }

    /* @since 3.0-dev-20 */
    default void close() throws Exception {
    }

    @Contract("null -> null")
    static LineWriter from(Object out) {
        if (out == null) return null;
        if (out instanceof LineWriter) return (LineWriter) out;
        if (out instanceof PrintStream) {
            PrintStream ps = (PrintStream) out;
            return new LineWriter() {
                @Override
                public void println(String line) {
                    ps.println(line);
                }

                @Override
                public void close() throws IOException {
                    ps.close();
                }
            };
        }
        if (out instanceof PrintWriter) {
            PrintWriter pw = (PrintWriter) out;
            return new LineWriter() {
                @Override
                public void println(String line) {
                    pw.println(line);
                }

                @Override
                public void close() throws IOException {
                    pw.close();
                }
            };
        }
        if (out instanceof RandomAccessFile) {
            RandomAccessFile file = (RandomAccessFile) out;
            byte[] ln = new byte[]{(byte) '\n'};
            return new LineWriter() {
                @Override
                public void println(String line) {
                    try {
                        ByteBuffer encoded = StandardCharsets.UTF_8.encode(line);
                        // If unset position then external editor cannot edit the file.
                        FileChannel channel = file.getChannel().position(file.length());
                        channel.write(encoded);
                        channel.write(ByteBuffer.wrap(ln));
                        file.seek(file.length());
                    } catch (IOException e) {
                        throw new IOError(e);
                    }
                }

                @Override
                public void close() throws IOException {
                    file.close();
                }
            };
        }
        if (out instanceof Appendable) {
            Appendable wr = (Appendable) out;
            return new LineWriter() {
                @Override
                public void println(String x) {
                    try {
                        wr.append(x).append('\n');
                    } catch (IOException e) {
                        throw new IOError(e);
                    }
                }

                @Override
                public void close() throws Exception {
                    if (wr instanceof AutoCloseable) {
                        ((AutoCloseable) wr).close();
                    }
                }
            };
        }
        if (out instanceof OutputStream) {
            return from(new OutputStreamWriter((OutputStream) out, StandardCharsets.UTF_8));
        }
        if (out instanceof DataOutput) {
            DataOutput dox = (DataOutput) out;
            return new LineWriter() {
                @Override
                public void println(String x) {
                    ByteBuffer encoded = StandardCharsets.UTF_8.encode(x);
                    byte[] array = new byte[encoded.remaining()];
                    encoded.get(array);
                    try {
                        dox.write(array);
                        dox.write('\n');
                    } catch (IOException e) {
                        throw new IOError(e);
                    }
                }

                @Override
                public void close() throws Exception {
                    if (out instanceof AutoCloseable)
                        ((AutoCloseable) out).close();
                }
            };
        }
        if (out instanceof WritableByteChannel) {
            WritableByteChannel channel = (WritableByteChannel) out;
            final ByteBuffer b = ByteBuffer.allocate(1);
            return new LineWriter() {
                @Override
                public void println(String x) {
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
                }

                @Override
                public void close() throws Exception {
                    channel.close();
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
