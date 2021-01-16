package io.github.karlatemp.mxlib.network.cs.serialize.internal;

import io.github.karlatemp.mxlib.network.cs.serialize.PacketSerializer;
import io.github.karlatemp.unsafeaccessor.Unsafe;
import io.netty.buffer.ByteBuf;
import org.jetbrains.annotations.NotNull;

public class CharArraySerializer implements PacketSerializer<char[]> {
    private static final Unsafe usf = Unsafe.getUnsafe();
    private static final boolean USE_UNSAFE = Unsafe.ARRAY_CHAR_INDEX_SCALE == Character.BYTES;

    @Override
    public void serialize(char[] value, SerializeContext context) {
        ByteBuf buf = context.getBuf();
        buf.writeInt(value.length);
        buf.ensureWritable(Character.BYTES * value.length);
        if (buf.hasMemoryAddress() && USE_UNSAFE) {
            long address = buf.memoryAddress();
            usf.copyMemory(value,
                    Unsafe.ARRAY_CHAR_BASE_OFFSET,
                    null,
                    address + buf.writerIndex(),
                    (long) value.length * Character.BYTES
            );
            buf.writerIndex(buf.writerIndex() + value.length * Character.BYTES);
            return;
        }
        for (char i : value) {
            buf.writeChar(i);
        }
    }

    @Override
    public char @NotNull [] deserialize(SerializeContext context) {
        ByteBuf buf = context.getBuf();
        int size = buf.readInt();
        char[] result = new char[size];
        ByteBufKit.ensureReadable(buf, size * Character.BYTES);
        if (buf.hasMemoryAddress() && USE_UNSAFE) {
            usf.copyMemory(
                    null, buf.memoryAddress() + buf.readerIndex(),
                    result, Unsafe.ARRAY_CHAR_BASE_OFFSET,
                    (long) size * Character.BYTES
            );
            buf.skipBytes(size * Character.BYTES);
            return result;
        }
        for (int i = 0; i < size; i++) {
            result[i] = buf.readChar();
        }
        return result;
    }

    @Override
    public @NotNull Class<char[][]> arrayType() {
        return char[][].class;
    }
}
