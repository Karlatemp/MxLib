/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/02/15 24:50:41
 *
 * MXLib/mxlib.message/MinecraftPacketMessageDecoder.java
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
