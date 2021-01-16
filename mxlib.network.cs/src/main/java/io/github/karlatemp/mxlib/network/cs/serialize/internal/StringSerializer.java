/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-network-cs.main/StringSerializer.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.network.cs.serialize.internal;

import io.github.karlatemp.mxlib.network.cs.serialize.PacketSerializer;
import io.netty.buffer.ByteBuf;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;

public class StringSerializer implements PacketSerializer<String> {
    @Override
    public void serialize(String value, SerializeContext context) {
        ByteBuf buf = context.getBuf();
        byte[] bytes = value.getBytes(StandardCharsets.UTF_8);
        buf.ensureWritable(Integer.BYTES + bytes.length);
        buf.writeInt(bytes.length);
        buf.writeBytes(bytes);
    }

    @Override
    public @NotNull String deserialize(SerializeContext context) {
        ByteBuf buf = context.getBuf();
        int size = buf.readInt();
        buf.skipBytes(size);
        return buf.toString(buf.readerIndex() - size, size, StandardCharsets.UTF_8);
    }

    @Override
    public @NotNull Class<String[]> arrayType() {
        return String[].class;
    }
}
