/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-network-cs.main/LongArraySerializer.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.network.cs.serialize.internal;

import io.github.karlatemp.mxlib.network.cs.serialize.PacketSerializer;
import io.github.karlatemp.unsafeaccessor.Unsafe;
import io.netty.buffer.ByteBuf;
import org.jetbrains.annotations.NotNull;

public class LongArraySerializer implements PacketSerializer<long[]> {
    private static final Unsafe usf = Unsafe.getUnsafe();
    private static final boolean USE_UNSAFE = Unsafe.ARRAY_LONG_INDEX_SCALE == Long.BYTES;

    @Override
    public void serialize(long[] value, SerializeContext context) {
        ByteBuf buf = context.getBuf();
        buf.writeInt(value.length);
        buf.ensureWritable(Long.BYTES * value.length + 1);
        if (buf.hasMemoryAddress() && USE_UNSAFE) {
            long address = buf.memoryAddress();
            usf.copyMemory(value,
                    Unsafe.ARRAY_INT_BASE_OFFSET,
                    null,
                    address + buf.writerIndex(),
                    (long) value.length * Long.BYTES
            );
            buf.writerIndex(buf.writerIndex() + value.length * Long.BYTES);
            return;
        }
        for (long i : value) {
            buf.writeLong(i);
        }
    }

    @Override
    public long @NotNull [] deserialize(SerializeContext context) {
        ByteBuf buf = context.getBuf();
        int size = buf.readInt();
        long[] result = new long[size];
        ByteBufKit.ensureReadable(buf, size * Long.BYTES);
        if (buf.hasMemoryAddress() && USE_UNSAFE) {
            usf.copyMemory(
                    null, buf.memoryAddress() + buf.readerIndex(),
                    result, Unsafe.ARRAY_LONG_BASE_OFFSET,
                    (long) size * Long.BYTES
            );
            buf.skipBytes(size * Long.BYTES);
            return result;
        }
        for (int i = 0; i < size; i++) {
            result[i] = buf.readLong();
        }
        return result;
    }

    @Override
    public @NotNull Class<long[][]> arrayType() {
        return long[][].class;
    }
}
