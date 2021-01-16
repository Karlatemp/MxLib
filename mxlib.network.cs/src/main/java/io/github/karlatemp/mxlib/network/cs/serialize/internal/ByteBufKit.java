package io.github.karlatemp.mxlib.network.cs.serialize.internal;

import io.netty.buffer.ByteBuf;

class ByteBufKit {
    static void ensureReadable(ByteBuf buf, int minReadableBytes) {
        if (!buf.isReadable(minReadableBytes)) {
            throw new IndexOutOfBoundsException(String.format(
                    "minReadableBytes(%d) exceeds readableBytes(%d): %s",
                    minReadableBytes, buf.readableBytes(), buf));
        }
    }
}
