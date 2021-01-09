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
