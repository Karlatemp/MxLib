/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-network-cs.main/ListLikeSerializer.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.network.cs.serialize.internal;

import com.sun.jmx.remote.internal.ArrayQueue;
import io.github.karlatemp.mxlib.network.cs.serialize.PacketSerializer;
import io.github.karlatemp.mxlib.reflect.Reflections;
import io.github.karlatemp.unsafeaccessor.Root;
import io.netty.buffer.ByteBuf;
import org.jetbrains.annotations.NotNull;

import java.lang.invoke.MethodHandle;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.IntFunction;

@SuppressWarnings({"unchecked", "rawtypes"})
public abstract class ListLikeSerializer<T, V> implements PacketSerializer<T> {
    protected final Class<T> type;
    private final PacketSerializer<V> serializer;

    public static class WithCollection<T extends Collection<V>, V> extends ListLikeSerializer<T, V> {

        private final IntFunction<T> supplier;

        public static <T> IntFunction<T> newSupplier(Class<?> type) {
            if (type.isInterface()) {
                throw new IllegalArgumentException("Interface is not supported: " + type);
            }
            if (Modifier.isAbstract(type.getModifiers())) {
                throw new IllegalArgumentException("Abstract class is not supported: " + type);
            }
            IntFunction<T> sup;
            try {
                try {
                    Constructor constructor = type.getConstructor(int.class);
                    Reflections.openAccess().apply(constructor);
                    MethodHandle handle = Root.getTrusted().unreflectConstructor(constructor);
                    sup = size -> {
                        try {
                            return (T) handle.invoke(size);
                        } catch (Throwable throwable) {
                            throw new RuntimeException(throwable);
                        }
                    };
                } catch (NoSuchMethodException e) {
                    try {
                        Constructor constructor = type.getConstructor();
                        Reflections.openAccess().apply(constructor);
                        MethodHandle handle = Root.getTrusted().unreflectConstructor(constructor);
                        sup = size -> {
                            try {
                                return (T) handle.invoke();
                            } catch (Throwable throwable) {
                                throw new RuntimeException(throwable);
                            }
                        };
                    } catch (NoSuchMethodException e2) {
                        RuntimeException re = new RuntimeException("No constructor found for " + type);
                        re.addSuppressed(e);
                        re.addSuppressed(e2);
                        throw re;
                    }
                }
            } catch (IllegalAccessException e) {
                throw new AssertionError(e);
            }
            return sup;
        }

        @SuppressWarnings("UnnecessaryLocalVariable")
        public static <T extends Collection<V>, V> WithCollection<T, V> newSerializer(Class<T> type, PacketSerializer<V> serializer) {
            if (!Collection.class.isAssignableFrom(type)) {
                throw new IllegalArgumentException(type + " is not a collection type");
            }
            Class rawTypeType = type; // compiler error
            Class rawType;
            if (type.isInterface()) {
                if (rawTypeType == List.class) {
                    rawType = ArrayList.class;
                } else if (rawTypeType == Collection.class) {
                    rawType = ArrayList.class;
                } else if (rawTypeType == Set.class) {
                    rawType = HashSet.class;
                } else if (rawTypeType == Deque.class) {
                    rawType = ArrayDeque.class;
                } else if (rawTypeType == Queue.class) {
                    rawType = ArrayQueue.class;
                } else {
                    throw new IllegalArgumentException("Interface type is not supported: " + type);
                }
            } else {
                rawType = type;
            }

            return new WithCollection<>(type, serializer, newSupplier(rawType));
        }

        public WithCollection(Class<T> type, PacketSerializer<V> serializer, IntFunction<T> supplier) {
            super(type, serializer);
            this.supplier = supplier;
        }

        @Override
        protected int getSize(T value) {
            return value.size();
        }

        @Override
        protected Iterator<V> iterator(T value) {
            return value.iterator();
        }

        @Override
        protected T newCollection(int size) {
            return supplier.apply(size);
        }

        @Override
        protected void add(T collection, V value) {
            collection.add(value);
        }
    }

    public static class WithMap<T extends Map<K, V>, K, V> extends ListLikeSerializer<T, Map.Entry<K, V>> {
        @SuppressWarnings("UnnecessaryLocalVariable")
        public static <T extends Map<K, V>, K, V> WithMap<T, K, V> newSerializer(
                Class<T> type,
                PacketSerializer<K> keySerializer,
                PacketSerializer<V> valueSerializer
        ) {
            if (!Map.class.isAssignableFrom(type)) {
                throw new IllegalArgumentException(type + " is not a map type");
            }
            Class rawTypeType = type; // compiler error
            Class rawType;
            if (type.isInterface()) {
                if (rawTypeType == Map.class) {
                    rawType = LinkedHashMap.class;
                } else if (rawTypeType == ConcurrentMap.class) {
                    rawType = ConcurrentHashMap.class;
                } else {
                    throw new IllegalArgumentException("Interface type is not supported: " + type);
                }
            } else if (rawTypeType == HashMap.class) {
                rawType = LinkedHashMap.class;
            } else {
                rawType = type;
            }
            return new WithMap<>(type, new MapEntrySerializer<>(keySerializer, valueSerializer), WithCollection.newSupplier(rawType));
        }

        private final IntFunction<T> supplier;

        public WithMap(Class<T> type, PacketSerializer<Map.Entry<K, V>> serializer, IntFunction<T> supplier) {
            super(type, serializer);
            this.supplier = supplier;
        }

        @Override
        protected int getSize(T value) {
            return value.size();
        }

        @Override
        protected Iterator<Map.Entry<K, V>> iterator(T value) {
            return value.entrySet().iterator();
        }

        @Override
        protected T newCollection(int size) {
            return supplier.apply(size);
        }

        @Override
        protected void add(T collection, Map.Entry<K, V> value) {
            collection.put(value.getKey(), value.getValue());
        }
    }

    public ListLikeSerializer(
            Class<T> type,
            PacketSerializer<V> serializer
    ) {
        this.type = type;
        this.serializer = serializer;
    }

    protected abstract int getSize(T value);

    protected abstract Iterator<V> iterator(T value);

    protected abstract T newCollection(int size);

    protected abstract void add(T collection, V value);

    @Override
    public void serialize(T value, SerializeContext context) {
        ByteBuf buf = context.getBuf();
        buf.writeInt(getSize(value));
        Iterator<V> iterator = iterator(value);
        while (iterator.hasNext()) {
            serializer.serialize(iterator.next(), context);
        }
    }

    @Override
    public @NotNull T deserialize(SerializeContext context) {
        ByteBuf buf = context.getBuf();
        int size;
        T list = newCollection(size = buf.readInt());
        while (size-- > 0) {
            add(list, serializer.deserialize(context));
        }
        return list;
    }

    @Override
    public @NotNull Class<T[]> arrayType() {
        return (Class<T[]>) Array.newInstance(type, 0).getClass();
    }
}
