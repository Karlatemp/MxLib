/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-common.main/ByteBufferKit.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.reflect;

import io.github.karlatemp.unsafeaccessor.Root;
import io.github.karlatemp.unsafeaccessor.Unsafe;
import org.jetbrains.annotations.NotNull;

import java.lang.invoke.MethodHandle;
import java.nio.ByteBuffer;

public class ByteBufferKit {
    private static final Unsafe UNSAFE = Unsafe.getUnsafe();
    private static final MethodHandle ADDRESS_MH;

    static {
        try {
            ADDRESS_MH = Root.getTrusted().findGetter(ByteBuffer.class, "address", long.class);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public static long getAddress(@NotNull ByteBuffer buffer) {
        try {
            return (long) ADDRESS_MH.invoke(buffer);
        } catch (Throwable throwable) {
            UNSAFE.throwException(throwable);
            throw new InternalError(throwable);
        }
    }
}
