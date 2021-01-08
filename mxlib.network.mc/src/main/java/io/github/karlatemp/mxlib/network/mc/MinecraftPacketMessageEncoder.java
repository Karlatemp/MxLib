/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/02/15 24:50:41
 *
 * MXLib/mxlib.message/MinecraftPacketMessageEncoder.java
 */

package io.github.karlatemp.mxlib.network.mc;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * Packet encoder for Minecraft.
 *
 * @since 2.9
 */
public class MinecraftPacketMessageEncoder extends MessageToByteEncoder<ByteBuf> {
    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf msg, ByteBuf out) throws Exception {
        int bytes = msg.readableBytes();
        int int_size = PacketDataSerializer.$unknown_a(bytes);

        if (int_size > 3) {
            throw new IllegalArgumentException("unable to fit " + int_size + " into " + 3);
        } else {
            PacketDataSerializer packetdataserializer = PacketDataSerializer.fromByteBuf(out);
            packetdataserializer.ensureWritable(bytes + int_size);
            packetdataserializer.writeVarInt(bytes);
            packetdataserializer.writeBytes(msg, msg.readerIndex(), bytes);
        }
    }
}
