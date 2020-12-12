package io.github.karlatemp.mxlib.utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.ByteChannel;
import java.util.Locale;

@SuppressWarnings("NullableProblems")
public class NilStreams {
    public static final @NotNull InputStream INPUT_STREAM = new InputStream() {
        @Override
        public int read() {
            return -1;
        }

        @Override
        public int read(byte[] b) {
            return -1;
        }

        @Override
        public int read(byte[] b, int off, int len) {
            return -1;
        }

        @Override
        public void close() {
        }

        @Override
        public int available() {
            return 0;
        }

        @Override
        public long skip(long n) {
            return 0;
        }

        @Override
        public void skipNBytes(long n) {
        }

        // @Override
        public byte[] readAllBytes() {
            return new byte[0];
        }

        // @Override
        public byte[] readNBytes(int len) {
            return new byte[0];
        }

        // @Override
        public int readNBytes(byte[] b, int off, int len) {
            return 0;
        }

        // @Override
        public long transferTo(OutputStream out) {
            return 0L;
        }
    };
    public static final @NotNull BufferedInputStream BUFFERED_INPUT_STREAM = new BufferedInputStream(INPUT_STREAM, 1) {
        @Override
        public int read() {
            return -1;
        }

        @Override
        public int read(byte[] b) {
            return -1;
        }

        @Override
        public int read(byte[] b, int off, int len) {
            return -1;
        }

        @Override
        public void close() {
        }

        @Override
        public int available() {
            return 0;
        }

        @Override
        public long skip(long n) {
            return 0;
        }

        @Override
        public void skipNBytes(long n) {
        }

        // @Override
        public byte[] readAllBytes() {
            return new byte[0];
        }

        // @Override
        public byte[] readNBytes(int len) {
            return new byte[0];
        }

        // @Override
        public int readNBytes(byte[] b, int off, int len) {
            return 0;
        }

        // @Override
        public long transferTo(OutputStream out) {
            return 0L;
        }
    };

    public static final @NotNull OutputStream OUTPUT_STREAM = new OutputStream() {
        @Override
        public void write(int b) {
        }

        @Override
        public void flush() {
        }

        @Override
        public void close() {
        }

        @Override
        public void write(byte[] b, int off, int len) {
        }

        @Override
        public void write(byte[] b) {
        }
    };
    public static final @NotNull BufferedOutputStream BUFFERED_OUTPUT_STREAM = new BufferedOutputStream(OUTPUT_STREAM, 1) {
        @Override
        public void write(int b) {
        }

        @Override
        public void flush() {
        }

        @Override
        public void close() {
        }

        @Override
        public void write(byte[] b, int off, int len) {
        }

        @Override
        public void write(byte[] b) {
        }
    };

    public static final @NotNull Reader READER = new Reader() {
        @Override
        public int read(char[] cbuf, int off, int len) {
            return -1;
        }

        @Override
        public int read() {
            return -1;
        }

        @Override
        public int read(char[] cbuf) {
            return -1;
        }

        @Override
        public int read(@NotNull CharBuffer target) {
            return -1;
        }

        @Override
        public void close() {
        }

        @Override
        public long skip(long n) {
            return 0L;
        }

        @Override
        public boolean ready() {
            return true;
        }

        //@Override
        public long transferTo(Writer out) {
            return 0L;
        }
    };
    public static final @NotNull BufferedReader BUFFERED_READER = new BufferedReader(READER) {
        @Override
        public int read(char[] cbuf, int off, int len) {
            return -1;
        }

        @Override
        public int read() {
            return -1;
        }

        @Override
        public int read(char[] cbuf) {
            return -1;
        }

        @Override
        public int read(@NotNull CharBuffer target) {
            return -1;
        }

        @Override
        public void close() {
        }

        @Override
        public long skip(long n) {
            return 0L;
        }

        @Override
        public boolean ready() {
            return true;
        }

        //@Override
        public long transferTo(Writer out) {
            return 0L;
        }
    };

    public static final @NotNull Writer WRITER = new Writer() {
        @Override
        public void write(char[] cbuf, int off, int len) {
        }

        @Override
        public void flush() {
        }

        @Override
        public Writer append(CharSequence csq, int start, int end) {
            return this;
        }

        @Override
        public Writer append(char c) {
            return this;
        }

        @Override
        public Writer append(CharSequence csq) {
            return this;
        }

        @Override
        public void write(String str, int off, int len) {
        }

        @Override
        public void close() {
        }

        @Override
        public void write(char[] cbuf) {
        }

        @Override
        public void write(String str) {
        }

        @Override
        public void write(int c) {
        }
    };
    public static final @NotNull BufferedWriter BUFFERED_WRITER = new BufferedWriter(WRITER, 1) {
        @Override
        public void write(char[] cbuf, int off, int len) {
        }

        @Override
        public void flush() {
        }

        @Override
        public Writer append(CharSequence csq, int start, int end) {
            return this;
        }

        @Override
        public Writer append(char c) {
            return this;
        }

        @Override
        public Writer append(CharSequence csq) {
            return this;
        }

        @Override
        public void write(String str, int off, int len) {
        }

        @Override
        public void close() {
        }

        @Override
        public void write(char[] cbuf) {
        }

        @Override
        public void write(String str) {
        }

        @Override
        public void write(int c) {
        }
    };
    public static final @NotNull Appendable APPENDABLE = WRITER;

    public static final @NotNull ByteChannel BYTE_CHANNEL = new ByteChannel() {
        @Override
        public int read(ByteBuffer dst) {
            return -1;
        }

        @Override
        public int write(ByteBuffer src) {
            int s = src.remaining();
            src.position(src.position() + s);
            return s;
        }

        @Override
        public boolean isOpen() {
            return true;
        }

        @Override
        public void close() {
        }
    };

    public static final @NotNull PrintStream PRINT_STREAM = new PrintStream(OUTPUT_STREAM) {
        {
            super.close();
        }

        @Override
        public PrintStream append(char c) {
            return this;
        }

        @Override
        public void println(int x) {
        }

        @Override
        public void println(char x) {
        }

        @Override
        public void println(long x) {
        }

        @Override
        public void println(float x) {
        }

        @Override
        public void println(@NotNull char[] x) {
        }

        @Override
        public void println(double x) {
        }

        @Override
        public void println(@Nullable Object x) {
        }

        @Override
        public void println(@Nullable String x) {
        }

        @Override
        public void println(boolean x) {
        }

        @Override
        public void print(int i) {
        }

        @Override
        public void print(char c) {
        }

        @Override
        public void print(long l) {
        }

        @Override
        public void print(float f) {
        }

        @Override
        public void print(@NotNull char[] s) {
        }

        @Override
        public void print(double d) {
        }

        @Override
        public void print(@Nullable String s) {
        }

        @Override
        public void print(boolean b) {
        }

        @Override
        public void print(@Nullable Object obj) {
        }

        @Override
        protected void clearError() {
        }

        @Override
        protected void setError() {
        }

        @Override
        public PrintStream append(CharSequence csq) {
            return this;
        }

        @Override
        public PrintStream append(CharSequence csq, int start, int end) {
            return this;
        }

        @Override
        public PrintStream format(@NotNull String format, Object... args) {
            return this;
        }

        @Override
        public PrintStream format(Locale l, @NotNull String format, Object... args) {
            return this;
        }

        @Override
        public PrintStream printf(@NotNull String format, Object... args) {
            return this;
        }

        @Override
        public PrintStream printf(Locale l, @NotNull String format, Object... args) {
            return this;
        }

        @Override
        public boolean checkError() {
            return false;
        }

        @Override
        public void close() {
        }

        @Override
        public void flush() {
        }

        @Override
        public void println() {
        }

        @Override
        public void write(int b) {
        }

        @Override
        public void write(@NotNull byte[] b) {
        }

        @Override
        public void write(@NotNull byte[] buf, int off, int len) {
        }
    };
    public static final @NotNull PrintWriter PRINT_WRITER = new PrintWriter(WRITER) {
        {
            super.close();
        }

        @Override
        public PrintWriter append(char c) {
            return this;
        }

        @Override
        public PrintWriter append(CharSequence csq) {
            return this;
        }

        @Override
        public PrintWriter append(CharSequence csq, int start, int end) {
            return this;
        }

        @Override
        public PrintWriter format(@NotNull String format, Object... args) {
            return this;
        }

        @Override
        public PrintWriter format(Locale l, @NotNull String format, Object... args) {
            return this;
        }

        @Override
        public void println(int x) {
        }

        @Override
        public void println(char x) {
        }

        @Override
        public void println(long x) {
        }

        @Override
        public void println(float x) {
        }

        @Override
        public void println(@NotNull char[] x) {
        }

        @Override
        public void println(double x) {
        }

        @Override
        public void println(Object x) {
        }

        @Override
        public void println(String x) {
        }

        @Override
        public void println(boolean x) {
        }

        @Override
        public void print(int i) {
        }

        @Override
        public void print(char c) {
        }

        @Override
        public void print(long l) {
        }

        @Override
        public void print(float f) {
        }

        @Override
        public void print(@NotNull char[] s) {
        }

        @Override
        public void print(double d) {
        }

        @Override
        public void print(String s) {
        }

        @Override
        public void print(boolean b) {
        }

        @Override
        public void print(Object obj) {
        }

        @Override
        protected void clearError() {
        }

        @Override
        public PrintWriter printf(@NotNull String format, Object... args) {
            return this;
        }

        @Override
        public PrintWriter printf(Locale l, @NotNull String format, Object... args) {
            return this;
        }

        @Override
        protected void setError() {
        }

        @Override
        public boolean checkError() {
            return false;
        }

        @Override
        public void close() {
        }

        @Override
        public void flush() {
        }

        @Override
        public void println() {
        }

        @Override
        public void write(int c) {
        }

        @Override
        public void write(@NotNull String s) {
        }

        @Override
        public void write(@NotNull char[] buf) {
        }

        @Override
        public void write(@NotNull String s, int off, int len) {
        }

        @Override
        public void write(@NotNull char[] buf, int off, int len) {
        }
    };
}
