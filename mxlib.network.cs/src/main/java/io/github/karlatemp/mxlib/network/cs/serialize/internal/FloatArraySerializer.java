/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-network-cs.main/FloatArraySerializer.java
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

public class FloatArraySerializer implements PacketSerializer<float[]> {
    private static final Unsafe usf = Unsafe.getUnsafe();
    private static final boolean USE_UNSAFE = Unsafe.ARRAY_INT_INDEX_SCALE == Float.BYTES;

    @Override
    public void serialize(float[] value, SerializeContext context) {
        ByteBuf buf = context.getBuf();
        buf.ensureWritable(Float.BYTES * (value.length + 1));
        buf.writeInt(value.length);
        if (buf.hasMemoryAddress() && USE_UNSAFE) {
            long address = buf.memoryAddress();
            usf.copyMemory(value,
                    Unsafe.ARRAY_FLOAT_BASE_OFFSET,
                    null,
                    address + buf.writerIndex(),
                    (long) value.length * Float.BYTES
            );
            buf.writerIndex(buf.writerIndex() + value.length * Float.BYTES);
            return;
        }
        for (float i : value) {
            buf.writeFloat(i);
        }
    }

    @Override
    public float @NotNull [] deserialize(SerializeContext context) {
        ByteBuf buf = context.getBuf();
        int size = buf.readInt();
        float[] result = new float[size];
        ByteBufKit.ensureReadable(buf, size * Float.BYTES);
        if (buf.hasMemoryAddress() && USE_UNSAFE) {
            usf.copyMemory(
                    null, buf.memoryAddress() + buf.readerIndex(),
                    result, Unsafe.ARRAY_FLOAT_BASE_OFFSET,
                    (long) size * Float.BYTES
            );
            buf.skipBytes(size * Float.BYTES);
            return result;
        }
        for (int i = 0; i < size; i++) {
            result[i] = buf.readFloat();
        }
        return result;
    }

    @Override
    public @NotNull Class<float[][]> arrayType() {
        return float[][].class;
    }
}
