/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-network-cs.main/ByteArraySerializer.java
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

public class ByteArraySerializer implements PacketSerializer<byte[]> {
    @Override
    public void serialize(byte[] value, SerializeContext context) {
        ByteBuf buf = context.getBuf();
        buf.ensureWritable(value.length + Integer.BYTES);
        buf.writeInt(value.length);
        buf.writeBytes(value);
    }

    @Override
    public byte @NotNull [] deserialize(SerializeContext context) {
        ByteBuf buf = context.getBuf();
        int size = buf.readInt();
        byte[] result = new byte[size];
        buf.readBytes(result);
        return result;
    }

    @Override
    public @NotNull Class<byte[][]> arrayType() {
        return byte[][].class;
    }
}
