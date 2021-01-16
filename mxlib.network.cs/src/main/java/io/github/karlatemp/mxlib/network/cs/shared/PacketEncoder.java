/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-network-cs.main/PacketEncoder.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.network.cs.shared;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.EncoderException;
import io.netty.handler.codec.MessageToByteEncoder;

public class PacketEncoder extends MessageToByteEncoder<ByteBuf> {
    @Override
    public boolean isSharable() {
        return true;
    }

    @Override
    protected void ensureNotSharable() {
    }

    @Override
    public boolean acceptOutboundMessage(Object msg) throws Exception {
        return msg instanceof ByteBuf;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf msg, ByteBuf out) throws Exception {
        int size = msg.readableBytes();
        if (size < 0) {
            throw new EncoderException("size(" + size + ") < 0");
        }
        out.ensureWritable(Integer.BYTES + size);
        out.writeInt(size);
        out.writeBytes(msg);
    }
}
