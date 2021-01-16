/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-network-cs.main/ShortArraySerializer.java
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

public class ShortArraySerializer implements PacketSerializer<short[]> {
    private static final Unsafe usf = Unsafe.getUnsafe();
    private static final boolean USE_UNSAFE = Unsafe.ARRAY_SHORT_INDEX_SCALE == Short.BYTES;

    @Override
    public void serialize(short[] value, SerializeContext context) {
        ByteBuf buf = context.getBuf();
        buf.ensureWritable(Short.BYTES * (value.length + 1));
        buf.writeInt(value.length);
        if (buf.hasMemoryAddress() && USE_UNSAFE) {
            long address = buf.memoryAddress();
            usf.copyMemory(value,
                    Unsafe.ARRAY_SHORT_BASE_OFFSET,
                    null,
                    address + buf.writerIndex(),
                    (long) value.length * Short.BYTES
            );
            buf.writerIndex(buf.writerIndex() + value.length * Short.BYTES);
            return;
        }
        for (short i : value) {
            buf.writeShort(i);
        }
    }

    @Override
    public short @NotNull [] deserialize(SerializeContext context) {
        ByteBuf buf = context.getBuf();
        int size = buf.readInt();
        short[] result = new short[size];
        ByteBufKit.ensureReadable(buf, size * Short.BYTES);
        if (buf.hasMemoryAddress() && USE_UNSAFE) {
            usf.copyMemory(
                    null, buf.memoryAddress() + buf.readerIndex(),
                    result, Unsafe.ARRAY_SHORT_BASE_OFFSET,
                    (long) size * Short.BYTES
            );
            buf.skipBytes(size * Short.BYTES);
            return result;
        }
        for (int i = 0; i < size; i++) {
            result[i] = buf.readShort();
        }
        return result;
    }

    @Override
    public @NotNull Class<short[][]> arrayType() {
        return short[][].class;
    }
}
