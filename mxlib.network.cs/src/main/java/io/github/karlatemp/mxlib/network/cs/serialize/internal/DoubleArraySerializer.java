/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-network-cs.main/DoubleArraySerializer.java
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

public class DoubleArraySerializer implements PacketSerializer<double[]> {
    private static final Unsafe usf = Unsafe.getUnsafe();
    private static final boolean USE_UNSAFE = Unsafe.ARRAY_DOUBLE_INDEX_SCALE == Double.BYTES;

    @Override
    public void serialize(double[] value, SerializeContext context) {
        ByteBuf buf = context.getBuf();
        buf.ensureWritable(Double.BYTES * (value.length + 1));
        buf.writeInt(value.length);
        if (buf.hasMemoryAddress() && USE_UNSAFE) {
            long address = buf.memoryAddress();
            usf.copyMemory(value,
                    Unsafe.ARRAY_DOUBLE_BASE_OFFSET,
                    null,
                    address + buf.writerIndex(),
                    (long) value.length * Double.BYTES
            );
            buf.writerIndex(buf.writerIndex() + value.length * Double.BYTES);
            return;
        }
        for (double i : value) {
            buf.writeDouble(i);
        }
    }

    @Override
    public double @NotNull [] deserialize(SerializeContext context) {
        ByteBuf buf = context.getBuf();
        int size = buf.readInt();
        double[] result = new double[size];
        ByteBufKit.ensureReadable(buf, size * Double.BYTES);
        if (buf.hasMemoryAddress() && USE_UNSAFE) {
            usf.copyMemory(
                    null, buf.memoryAddress() + buf.readerIndex(),
                    result, Unsafe.ARRAY_SHORT_BASE_OFFSET,
                    (long) size * Double.BYTES
            );
            buf.skipBytes(size * Double.BYTES);
            return result;
        }
        for (int i = 0; i < size; i++) {
            result[i] = buf.readDouble();
        }
        return result;
    }

    @Override
    public @NotNull Class<double[][]> arrayType() {
        return double[][].class;
    }
}
