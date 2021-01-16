/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-api.main/SimpleBeanManager.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.bean;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

@SuppressWarnings("unchecked")
public class SimpleBeanManager implements IBeanManager {
    private final IBeanManager parent;
    private static final Object NIL_PLACEHOLDER = new Object();
    private final Map<Class<?>, Map<Object, Object>> registries = new ConcurrentHashMap<>();

    public SimpleBeanManager(IBeanManager parent) {
        this.parent = parent;
    }

    public SimpleBeanManager() {
        this(null);
    }

    @Override
    public @Nullable IBeanManager getParent() {
        return parent;
    }

    @Override
    public @NotNull <T> Optional<T> getBy(@NotNull Class<T> type) {
        if (type == IBeanManager.class) {
            return (Optional<T>) Optional.of(this);
        }
        Map<Object, Object> map = registries.get(type);
        if (map != null) {
            Object nil = map.get(NIL_PLACEHOLDER);
            if (nil != null) {
                return Optional.of((T) nil);
            }
            Iterator<Object> iterator = map.values().iterator();
            if (iterator.hasNext()) {
                return Optional.of((T) iterator.next());
            }
        }
        if (parent != null) return parent.getBy(type);
        return Optional.empty();
    }

    @Override
    public @NotNull <T> Optional<T> getBy(@NotNull Class<T> type, @Nullable String name) {
        if (type == IBeanManager.class && name == null) {
            return (Optional<T>) Optional.of(this);
        }

        Map<Object, Object> map = registries.get(type);
        if (map != null) {
            Object nil = map.get(key(name));
            if (nil != null) {
                return Optional.of((T) nil);
            }
            Iterator<Object> iterator = map.values().iterator();
            if (iterator.hasNext()) {
                Object next = iterator.next();
                if (!iterator.hasNext())
                    return Optional.of((T) next);
            }
        }

        if (parent != null) return parent.getBy(type, name);
        return Optional.empty();
    }

    private static Object key(Object k) {
        if (k == null) return NIL_PLACEHOLDER;
        return k;
    }

    @Override
    public @NotNull <T> Stream<T> getAll(@NotNull Class<T> type) {
        Stream<?> s1 = null;
        Map<Object, Object> map = registries.get(type);
        if (map != null) {
            s1 = map.values().stream();
        }
        if (parent != null) {
            if (s1 == null) return parent.getAll(type);
            return (Stream<T>) Stream.concat(s1, parent.getAll(type));
        }
        if (s1 == null) return Stream.empty();
        return (Stream<T>) s1;
    }

    @Override
    public <T> void register(@NotNull Class<T> type, @NotNull T value) {
        register(type, null, value);
    }

    @Override
    public <T> void register(@NotNull Class<T> type, @Nullable String name, @NotNull T value) {
        register0(type, key(name), value);
    }

    private void register0(Class<?> type, Object key, @NotNull Object value) {
        Map<Object, Object> regs = registries.computeIfAbsent(type, $ -> new ConcurrentHashMap<>());
        regs.putIfAbsent(key, value);
    }

    @Override
    public <T> void unregister(@NotNull Class<T> type) {
        registries.remove(type);
    }

    @Override
    public <T> void unregister(@NotNull Class<T> type, @Nullable String name) {
        Map<Object, Object> map = registries.get(type);
        if (map != null) {
            map.remove(key(name));
            if (map.isEmpty()) {
                registries.remove(type, map);
            }
        }
    }

    @Override
    public @NotNull IBeanManager newSubScope() {
        SimpleBeanManager newScope = new SimpleBeanManager(this);
        for (Map.Entry<Class<?>, Map<Object, Object>> entry : this.registries.entrySet()) {
            Map<Object, Object> entryValue = entry.getValue();
            for (Map.Entry<Object, Object> eev : entryValue.entrySet()) {
                Object value = eev.getValue();
                if (value instanceof ContextBean) {
                    newScope.register0(
                            entry.getKey(),
                            eev.getKey(),
                            ((ContextBean) value).copy(newScope)
                    );
                }
            }
        }
        return newScope;
    }
}
