package io.github.karlatemp.mxlib.network.cs.serialize.internal;

import io.github.karlatemp.mxlib.network.cs.serialize.PacketSerializer;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;

@SuppressWarnings("unchecked")
public class ArraySerializer<T> implements PacketSerializer<T[]> {
    private final PacketSerializer<T> serializer;

    public ArraySerializer(PacketSerializer<T> serializer) {
        this.serializer = serializer;
    }

    @Override
    public void serialize(T[] value, SerializeContext context) {
        context.getBuf().writeInt(value.length);
        for (T v : value) {
            serializer.serialize(v, context);
        }
    }

    @Override
    public @NotNull Class<T[][]> arrayType() {
        return (Class<T[][]>) Array.newInstance(serializer.arrayType(), 0).getClass();
    }

    @Override
    public T @NotNull [] deserialize(SerializeContext context) {
        T[] buf = (T[]) Array.newInstance(serializer.arrayType().getComponentType(), context.getBuf().readInt());
        int size = buf.length;
        for (int i = 0; i < size; i++) {
            buf[i] = serializer.deserialize(context);
        }
        return buf;
    }

}
