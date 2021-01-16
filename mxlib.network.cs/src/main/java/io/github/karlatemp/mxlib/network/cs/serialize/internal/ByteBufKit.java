/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-network-cs.main/ByteBufKit.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

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
