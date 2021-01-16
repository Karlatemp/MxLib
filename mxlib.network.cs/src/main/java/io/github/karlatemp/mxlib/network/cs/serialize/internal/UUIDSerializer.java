/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-network-cs.main/UUIDSerializer.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.network.cs.serialize.internal;

import io.github.karlatemp.mxlib.network.cs.serialize.PacketSerializer;
import io.netty.buffer.ByteBuf;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class UUIDSerializer implements PacketSerializer<UUID> {
    @Override
    public void serialize(UUID value, SerializeContext context) {
        ByteBuf buf = context.getBuf()
                .ensureWritable(Long.BYTES * 2)
                .writeLong(value.getMostSignificantBits())
                .writeLong(value.getLeastSignificantBits());
    }

    @Override
    public @NotNull UUID deserialize(SerializeContext context) {
        ByteBuf buf = context.getBuf();
        return new UUID(buf.readLong(), buf.readLong());
    }

    @Override
    public @NotNull Class<UUID[]> arrayType() {
        return UUID[].class;
    }
}
