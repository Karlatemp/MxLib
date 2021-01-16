/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-network-mc.main/MinecraftPacketMessageDecoder.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.network.mc;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.CorruptedFrameException;

import java.util.List;

/**
 * Packet message decoder for Minecraft.
 *
 * @since 2.9
 */
public class MinecraftPacketMessageDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        root:
        while (in.isReadable()) {
            in.markReaderIndex();
            byte[] b = new byte[3];
            for (int i = 0; i < 3; i++) {
                if (!in.isReadable()) {
                    in.resetReaderIndex();
                    return;
                }
                byte w = b[i] = in.readByte();
                if (w > 0) {
                    int size = PacketDataSerializer.fromByteBuf(Unpooled.wrappedBuffer(b)).readVarInt();
                    if (size > in.readableBytes()) {
                        in.resetReaderIndex();
                        return;
                    }
                    out.add(in.readBytes(size));
                    in.markReaderIndex();
                    continue root;
                }
            }
            throw new CorruptedFrameException("length wider than 21-bit");
        }
    }
}
