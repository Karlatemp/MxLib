/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-network-cs.main/PacketSerializer.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.network.cs.serialize;

import io.netty.buffer.ByteBuf;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;

@SuppressWarnings("UnnecessaryInterfaceModifier")
public interface PacketSerializer<T> {
    public static interface SerializeContext {
        public abstract @NotNull <T> PacketSerializer<T> findSerializer(Type type);

        public abstract @NotNull ByteBuf getBuf();
    }

    public abstract void serialize(T value, SerializeContext context);

    public abstract @NotNull T deserialize(SerializeContext context);

    public @NotNull Class<T[]> arrayType();
}
