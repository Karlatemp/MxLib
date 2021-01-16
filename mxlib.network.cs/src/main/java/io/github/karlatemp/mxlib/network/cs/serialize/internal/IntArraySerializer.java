package io.github.karlatemp.mxlib.network.cs.serialize.internal;

import io.github.karlatemp.mxlib.network.cs.serialize.PacketSerializer;
import io.github.karlatemp.unsafeaccessor.Unsafe;
import io.netty.buffer.ByteBuf;
import org.jetbrains.annotations.NotNull;

public class IntArraySerializer implements PacketSerializer<int[]> {
    private static final Unsafe usf = Unsafe.getUnsafe();
    private static final boolean USE_UNSAFE = Unsafe.ARRAY_INT_INDEX_SCALE == Integer.BYTES;

    @Override
    public void serialize(int[] value, SerializeContext context) {
        ByteBuf buf = context.getBuf();
        buf.ensureWritable(Integer.BYTES * (value.length + 1));
        buf.writeInt(value.length);
        if (buf.hasMemoryAddress() && USE_UNSAFE) {
            long address = buf.memoryAddress();
            usf.copyMemory(value,
                    Unsafe.ARRAY_INT_BASE_OFFSET,
                    null,
                    address + buf.writerIndex(),
                    (long) value.length * Integer.BYTES
            );
            buf.writerIndex(buf.writerIndex() + value.length * Integer.BYTES);
            return;
        }
        for (int i : value) {
            buf.writeInt(i);
        }
    }

    @Override
    public int @NotNull [] deserialize(SerializeContext context) {
        ByteBuf buf = context.getBuf();
        int size = buf.readInt();
        int[] result = new int[size];
        ByteBufKit.ensureReadable(buf, size * Integer.BYTES);
        if (buf.hasMemoryAddress() && USE_UNSAFE) {
            usf.copyMemory(
                    null, buf.memoryAddress() + buf.readerIndex(),
                    result, Unsafe.ARRAY_INT_BASE_OFFSET,
                    (long) size * Integer.BYTES
            );
            buf.skipBytes(size * Integer.BYTES);
            return result;
        }
        for (int i = 0; i < size; i++) {
            result[i] = buf.readInt();
        }
        return result;
    }

    @Override
    public @NotNull Class<int[][]> arrayType() {
        return int[][].class;
    }
}
