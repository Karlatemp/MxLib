package io.github.karlatemp.mxlib.network.cs.serialize;

import io.github.karlatemp.mxlib.network.cs.serialize.internal.*;
import io.netty.buffer.ByteBuf;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

@SuppressWarnings({"unchecked", "rawtypes"})
public class SimpleSerializeContext implements PacketSerializer.SerializeContext {

    public static Map<Type, PacketSerializer<?>> newSerializerMap() {
        Map<Type, PacketSerializer<?>> map = new HashMap<>();
        map.put(String.class, new StringSerializer());
        map.put(BitSet.class, new BitSetSerializer());
        map.put(byte[].class, new ByteArraySerializer());
        map.put(char[].class, new CharArraySerializer());
        map.put(double[].class, new DoubleArraySerializer());
        map.put(int[].class, new IntArraySerializer());
        map.put(long[].class, new LongArraySerializer());
        map.put(short[].class, new ShortArraySerializer());
        map.put(float[].class, new FloatArraySerializer());
        map.put(UUID.class, new UUIDSerializer());
        return map;
    }

    protected final ByteBuf buf;
    protected final Map<Type, PacketSerializer<?>> serializerMap;
    protected boolean registerNewSerializers;

    public SimpleSerializeContext(
            ByteBuf buf,
            Map<Type, PacketSerializer<?>> serializerMap
    ) {
        this(buf, serializerMap, true);
    }

    public SimpleSerializeContext(
            ByteBuf buf,
            Map<Type, PacketSerializer<?>> serializerMap,
            boolean registerNewSerializers
    ) {
        this.buf = buf;
        this.serializerMap = serializerMap;
        this.registerNewSerializers = registerNewSerializers;
    }

    protected <T> PacketSerializer<T> newSerializer(Class<T> type) {
        if (type.isArray()) {
            return new ArraySerializer(findSerializer(type.getComponentType()));
        }
        return new ReflectSerializer<>(type);
    }

    protected PacketSerializer<?> newSerializer(Type type) {
        if (type instanceof Class<?>) {
            return newSerializer((Class<?>) type);
        }
        if (type instanceof ParameterizedType) {
            ParameterizedType ptype = (ParameterizedType) type;
            Class<?> rawType = (Class<?>) ptype.getRawType();
            if (Collection.class.isAssignableFrom(rawType)) {
                return ListLikeSerializer.WithCollection.newSerializer(
                        (Class) rawType, findSerializer(ptype.getActualTypeArguments()[0])
                );
            }
            if (Map.class.isAssignableFrom(rawType)) {
                Type[] arguments = ptype.getActualTypeArguments();
                return ListLikeSerializer.WithMap.newSerializer(
                        (Class) rawType, findSerializer(arguments[0]), findSerializer(arguments[1])
                );
            }
        }
        throw new UnsupportedOperationException("NotSupported: " + type + ", " + type.getClass()); // TODO
    }

    @Override
    public @NotNull <T> PacketSerializer<T> findSerializer(Type type) {
        PacketSerializer<?> serializer = serializerMap.get(type);
        if (serializer != null) {
            return (PacketSerializer<T>) serializer;
        } else {
            PacketSerializer<?> newSerializer = newSerializer(type);
            if (registerNewSerializers) {
                serializerMap.put(type, newSerializer);
            }
            return (PacketSerializer<T>) newSerializer;
        }
    }

    @Override
    public @NotNull ByteBuf getBuf() {
        return buf;
    }
}
