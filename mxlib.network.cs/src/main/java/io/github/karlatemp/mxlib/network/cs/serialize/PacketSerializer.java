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
