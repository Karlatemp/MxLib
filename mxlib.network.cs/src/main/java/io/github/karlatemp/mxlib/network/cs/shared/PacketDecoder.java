package io.github.karlatemp.mxlib.network.cs.shared;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.DecoderException;

import java.util.List;

public class PacketDecoder extends ByteToMessageDecoder {
    @Override
    protected void ensureNotSharable() {
    }

    @Override
    public boolean isSharable() {
        return true;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        while (in.isReadable(Integer.BYTES)) {
            in.markReaderIndex();

            int size = in.readInt();
            if (size < 0) {
                throw new DecoderException("size(" + size + ") < 0");
            }

            if (in.isReadable(size)) {
                out.add(in.readBytes(size));
            } else {
                in.resetReaderIndex();
                break;
            }
        }
    }
}
