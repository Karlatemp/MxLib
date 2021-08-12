/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-api.main/Toolkit.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.utils;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import java.util.stream.Collector;

/**
 * 一个工具集合
 */
public class Toolkit {
    /**
     * @param k   The entry key
     * @param v   The entry value
     * @param <K> The key type
     * @param <V> The value type.
     * @return A new Entry.
     * @since 2.11
     */
    public static <K, V> Map.Entry<K, V> entry(K k, V v) {
        return new Map.Entry<K, V>() {
            V vx = v;

            @Override
            public K getKey() {
                return k;
            }

            @Override
            public V getValue() {
                return vx;
            }

            @Override
            public V setValue(V value) {
                V vv = vx;
                vx = value;
                return vv;
            }
        };
    }

    public static <T, V, M extends Map<T, V>> Collector<Map.Entry<T, V>, M, M> toMapCollector(Supplier<M> mapCreator) {
        return Collector.of(mapCreator,
                (map, entry) -> map.put(entry.getKey(), entry.getValue()),
                (m1, m2) -> {
                    m1.putAll(m2);
                    return m1;
                });
    }

    /**
     * @param cb  The function to invoke.
     * @param <T> The type of the callable
     * @return The callable need check
     * @since 2.5
     */
    @SuppressWarnings("unchecked")
    @Contract(pure = true, value = "null -> null")
    public static <T> Callable<T> toCallable(Object cb) {
        if (cb == null) return null;
        if (cb instanceof Callable) {
            return (Callable<T>) cb;
        }
        if (cb instanceof Runnable) {
            return () -> {
                ((Runnable) cb).run();
                return null;
            };
        }
        if (cb instanceof Supplier) {
            return ((Supplier<T>) cb)::get;
        }
        if (cb instanceof Throwable) {
            if (cb instanceof Exception) {
                return () -> {
                    throw (Exception) cb;
                };
            } else if (cb instanceof Error) {
                return () -> {
                    throw (Error) cb;
                };
            } else {
                return toCallable(new RuntimeException((Throwable) cb));
            }
        }
        throw new UnsupportedOperationException("Unsupported " + cb);
    }

    /**
     * Execute actions and return first successful result
     *
     * @since 3.0-dev-21
     */
    @SafeVarargs
    public static <V> V exec(Callable<V>... actions) throws Throwable {
        if (actions == null || actions.length == 0)
            throw new IllegalArgumentException("No action");
        Throwable throwable = null;
        for (Callable<V> action : actions) {
            if (action == null) continue;
            try {
                return action.call();
            } catch (Throwable throwable1) {
                if (throwable == null) throwable = throwable1;
                else throwable.addSuppressed(throwable1);
            }
        }
        if (throwable == null)
            throw new NoSuchElementException("No response and exceptions");
        throw throwable;
    }

    /**
     * Get class's package name.<br>
     * 截取类名的包名字
     *
     * @param name 类的全名
     * @return 类的包名
     */
    @NotNull
    @Contract(pure = true)
    public static String getPackageByClassName(String name) {
        if (name == null) return "";
        int x = name.lastIndexOf('.');
        if (x != -1) {
            return name.substring(0, x);
        }
        return "";
    }

    /**
     * Get class's simple name<br>
     * 获取类名的精简名
     *
     * @param name 类全名
     * @return 类精简名
     */
    @Contract(pure = true)
    public static String getClassSimpleName(String name) {
        int last = name.lastIndexOf('.');
        if (last == -1) return name;
        return name.substring(last);
    }

    private static final Comparator<String> PACKAGE_COMPARATOR = Comparator.comparing(Toolkit::getPackageByClassName);
    private static final Function<String, String> PACKAGE_BY_CLASS_NAME = Toolkit::getPackageByClassName;

    /**
     * @return The function of {@link #getPackageByClassName(String)}
     * @see #getPackageByClassName(String)
     */
    @Contract(pure = true)
    public static Function<String, String> getPackageByClassName() {
        return PACKAGE_BY_CLASS_NAME;
    }

    /**
     * Get the package name comparator.<br>
     * 获取包名比较器
     *
     * @return The comparator of package.
     * @see #getPackageByClassName(String)
     */
    @Contract(pure = true)
    public static Comparator<String> getPackageComparator() {
        return PACKAGE_COMPARATOR;
    }

    /**
     * @param <T> 对象类型
     * @return 对象的类|null
     * @see Object#getClass()
     */
    @Contract(value = "null -> null", pure = true)
    @SuppressWarnings("unchecked")
    public static <T> Class<T> getClass(T obj) {
        if (obj == null) return null;
        return (Class<T>) obj.getClass();
    }

    @Contract(pure = true)
    public static boolean isNum(String s) {
        if (s.isEmpty()) return false;
        for (char c : s.toCharArray()) {
            if (!(c >= '0' && c <= '9')) {
                return false;
            }
        }
        return true;
    }

    private static final Pattern CLASS_NAME_CHECKER = Pattern.compile(
            "^[a-z_$][a-z_$0-9]*(\\.[a-z$_][a-z$_0-9]*)$", Pattern.CASE_INSENSITIVE | Pattern.UNIX_LINES);

    /**
     * Check if the given string is a valid class name<br>
     * 检查给定的字符串是否是有效的类名
     *
     * @param check The name of check
     * @return true if the class name valid
     * @since 2.2
     */
    @Contract(value = "null -> false", pure = true)
    public static boolean isClassName(String check) {
        if (check != null) {
            return CLASS_NAME_CHECKER.matcher(check).find();
        }
        return false;
    }

    /**
     * fill the array, 填充数组
     *
     * @param args     The array 数组
     * @param supplier The supplier 获取器
     * @param <T>      The type of array.
     * @since 2.4
     */
    public static <T> void fill(@NotNull T[] args, @NotNull Supplier<T> supplier) {
        fill(args, 0, args.length, supplier);
    }

    /**
     * Fill the array 填充数组
     *
     * @param args     The array 数组
     * @param off      The start 起始点
     * @param length   length 长度限制
     * @param supplier The supplier 获取器
     * @param <T>      The type of array
     * @since 2.4
     */
    public static <T> void fill(@NotNull T[] args, int off, int length, @NotNull Supplier<T> supplier) {
        for (int i = off; i < args.length && length-- > 0; i++) {
            args[i] = supplier.get();
        }
    }

    public static <T> void fill(@NotNull T[] args, @Nullable T o) {
        Arrays.fill(args, o);
    }

    /**
     * File the array with same value. 使用同一个值填充数组
     *
     * @param args   The array 数组
     * @param off    start position 起始点
     * @param length The fill size of array 填充的数组长度
     * @param o      The value 填充值
     * @param <T>    The type of array
     * @since 2.4
     */
    public static <T> void fill(T[] args, int off, int length, @Nullable T o) {
        for (int i = off; i < args.length && length-- > 0; i++) {
            args[i] = o;
        }
    }

    /**
     * 把字符串转为 ByteBuffer(UTF_8)
     *
     * @param x 字符串
     * @return 编码后的ByteBuffer, 如果需要附加字符串长度请使用 position(0)
     * @since 2.4
     */
    @Contract(pure = true)
    public static ByteBuffer ofUTF8ByteBuffer(String x) {
        final ByteBuffer encode = StandardCharsets.UTF_8.encode(x);
        ByteBuffer cp = ByteBuffer.allocateDirect(encode.remaining() + Short.BYTES);
        cp.putShort((short) encode.remaining()).put(encode).flip().position(Short.BYTES);
        return cp;
    }

    /**
     * return value's boolean value
     *
     * @param o The value
     * @return The boolean match
     * @since 2.5
     */
    @Contract(value = "null -> false", pure = true)
    public static boolean asBoolean(Object o) {
        if (o == null) return false;
        if (o instanceof String) {
            String s = String.valueOf(o);
            switch (s) {
                case "true":
                case "1":
                case "[]":
                case "{}":
                case "yes":
                case "y":
                case "Y":
                    return true;
                case "false":
                case "no":
                case "n":
                case "N":
                case "0":
                    return false;
            }
            return !s.trim().isEmpty();
        }
        if (o instanceof Number) {
            if (o instanceof Double) {
                return Double.doubleToRawLongBits((Double) o) != 0;
            }
            return ((Number) o).intValue() != 0;
        }
        if (o instanceof Boolean) return (Boolean) o;
        return false;
    }


    /**
     * @since 3.0-dev-21
     */
    public static boolean isClassAvailable(String name, ClassLoader classLoader) {
        try {
            Class.forName(name, false, classLoader);
            return true;
        } catch (ClassNotFoundException ignore) {
            return false;
        }
    }

    /**
     * IO 数据操作
     *
     * @since 2.5
     */
    public static class IO {

        private static final File NULL_FILE = new File(
                (System.getProperty("os.name")
                        .startsWith("Windows") ? "NUL" : "/dev/null")
        );
        /**
         * Indicates that subprocess output will be discarded.
         * A typical implementation discards the output by writing to
         * an operating system specific "null file".
         *
         * <p>It will always be true that
         * <pre> {@code
         * Redirect.DISCARD.file() is the filename appropriate for the operating system
         * and may be null &&
         * Redirect.DISCARD.type() == Redirect.Type.WRITE
         * }</pre>
         *
         * @since 9
         */
        public static final ProcessBuilder.Redirect REDIRECT_DISCARD = ProcessBuilder.Redirect.to(NULL_FILE);

        public static long writeTo(@NotNull InputStream is, @NotNull OutputStream os) throws IOException {
            return writeTo(is, os, null);
        }

        public static long writeTo(@NotNull InputStream is, @NotNull OutputStream os, @Nullable byte[] buffer) throws IOException {
            return writeTo(is, os, buffer, 0);
        }

        public static long writeTo(@NotNull InputStream is, @NotNull OutputStream os, @Nullable byte[] buffer, long length) throws IOException {
            long buf = 0;
            if (buffer == null) {
                buffer = new byte[1024];
            }
            if (length == 0) {
                while (true) {
                    int leng = is.read(buffer);
                    if (leng == -1) {
                        break;
                    }
                    os.write(buffer, 0, leng);
                    buf += leng;
                }
            } else {
                final int bl = buffer.length;
                while (length > 0) {
                    int leng = is.read(buffer, 0, Math.max(0, Math.min((int) length, bl)));
                    if (leng == -1) {
                        break;
                    }
                    os.write(buffer, 0, leng);
                    buf += leng;
                    length -= leng;
                }
            }
            return buf;
        }

        /**
         * get bytes from bits
         *
         * @param bits  Bits of cut
         * @param bytes byte array size
         * @return The array of bits
         * @see #toBytes(long, int)
         * @since 2.7
         */
        public static byte[] toBytes(int bits, int bytes) {
            return toBytes(Integer.toUnsignedLong(bits), bytes);
        }

        /**
         * get bytes from bits
         *
         * @param bits  Bits of cut
         * @param bytes byte array size
         * @return The array of bits
         * @since 2.7
         */
        public static byte[] toBytes(long bits, int bytes) {
            byte[] ref = new byte[bytes];
            for (int i = 0; i < bytes; i++) {
                ref[i] = (byte) ((bits >> (i * Byte.SIZE)) & 0xFF);
            }
            return ref;

        }

        /**
         * Insert data to file
         *
         * @param position The insert position
         * @param data     The data insert
         * @param access   The random access file.(writable and readable)
         * @throws IOException The runtime exception.
         * @since 2.7
         */
        public static void insertData(long position, ByteBuffer data, @NotNull RandomAccessFile access) throws IOException {
            insertData(position, data, access, false);
        }

        /**
         * Insert data to file
         *
         * @param position The insert position
         * @param data     The data insert
         * @param access   The random access file.(writable and readable)
         * @param raw      Set write all data.(Unsafe)
         * @throws IOException The runtime exception.
         * @since 2.7
         */
        public static void insertData(long position, ByteBuffer data, @NotNull RandomAccessFile access, boolean raw) throws IOException {
            if (!data.hasRemaining()) return;
            /*
            long pos = access.getFilePointer();
            access.seek(position);
            byte[] write = new byte[size], read = new byte[size], tmp;
            int writeSize = size;
            do {
                int w;
                long pp = access.getFilePointer();
                if ((w = access.read(read)) < 0) {
                    if (writeSize > 0) {
                        access.write(write, 0, writeSize);
                    }
                    break;
                }
                access.seek(pp);
                access.write(write, 0, writeSize);
                tmp = write;
                write = read;
                read = tmp;
                writeSize = w;
            } while (true);
            access.seek(pos);
            */
            ByteBuffer r, w, tmp;
            if (raw) {
                if (data.position() != 0 || data.limit() != data.capacity()) {
                    throw new IOException("Cannot write a limited raw byte buffer.");
                }
                r = ByteBuffer.allocateDirect(data.capacity());
                w = data;
                data.clear();
            } else {
                w = ByteBuffer.allocateDirect(data.remaining());
                w.put(data);
                w.flip();
                r = ByteBuffer.allocateDirect(w.capacity());
            }
            final FileChannel channel = access.getChannel();
            final long at = channel.position(), aw = access.getFilePointer();
            channel.position(position);
            do {
                long pos = channel.position();
                if (channel.read(r) < 0) {
                    if (w.hasRemaining()) {
                        channel.write(w);
                    }
                    break;
                }
                channel.position(pos);
                channel.write(w);
                tmp = w;
                w = r;
                r = tmp;
                w.flip();
                r.clear();
            } while (true);
            channel.position(at);
            access.seek(aw);
        }

        /**
         * Insert empty data to position
         *
         * @param position The insert position
         * @param size     The empty size
         * @param access   The random access file.(writable and readable)
         * @throws IOException The runtime exception.
         * @since 2.7
         */
        public static void insertEmpty(long position, int size, @NotNull RandomAccessFile access) throws IOException {
            if (size < 1) return;
            insertData(position, ByteBuffer.allocateDirect(size), access, true);
        }

        /**
         * Quick calculation of file/directory
         *
         * @param md   The meddage digest
         * @param file The file or dir
         * @return the digest
         * @throws IOException Throw when make any exception.
         * @since 2.11
         */
        public static byte[] digest(@NotNull MessageDigest md, @NotNull File file) throws IOException {
            if (file.isDirectory()) {
                int root = file.toString().length();
                FilesIterator files = new FilesIterator(file, -1, false, false);
                ByteArrayInputStream EMPTY = new ByteArrayInputStream(new byte[0]);
                return digest(md, new IteratorSupplier<InputStream>(
                        new Iterator<InputStream>() {
                            private File current;

                            @Override
                            public boolean hasNext() {
                                if (current == null)
                                    return files.hasNext();
                                return true;
                            }

                            @Override
                            public InputStream next() {
                                if (current != null) {
                                    File f = current;
                                    current = null;
                                    if (f.isDirectory()) return EMPTY;
                                    try {
                                        return new FileInputStream(f);
                                    } catch (FileNotFoundException e) {
                                        throw new IOError(e);
                                    }
                                } else {
                                    if (files.hasNext()) {
                                        current = files.next();
                                        return new ByteArrayInputStream(('.' + current.toString().substring(root)).getBytes(StandardCharsets.UTF_8));
                                    }
                                    return null;
                                }
                            }
                        }
                ), null);
            }
            return digest(md, new IteratorSupplier<>(
                    Collections.singletonList(new FileInputStream(file)).iterator()
            ), null);
        }

        /**
         * Quick calculation of input streams.
         *
         * @param md     The message digest using.
         * @param data   Streams.
         * @param buffer reading buffer.
         * @return Result
         * @throws IOException Stream Reading Error.
         */
        public static byte[] digest(@NotNull MessageDigest md, @NotNull Supplier<? extends InputStream> data, @Nullable byte[] buffer) throws IOException {
            if (buffer == null) {
                buffer = new byte[2048];
            }
            while (true) {
                InputStream next = data.get();
                if (next == null) break;
                try (InputStream is = next) {
                    do {
                        int length = is.read(buffer);
                        if (length == -1) break;
                        md.update(buffer, 0, length);
                    } while (true);
                }
            }
            return md.digest();
        }

    }
}
