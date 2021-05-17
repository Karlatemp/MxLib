/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-api.main/MethodOverriddenDeterminers.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.internal;

import io.github.karlatemp.mxlib.utils.MethodOverriddenDeterminer;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.WeakHashMap;
import java.util.function.Function;

public class MethodOverriddenDeterminers {
    private static final MethodOverriddenDeterminer ALWAYS_FALSE = new MethodOverriddenDeterminer() {
        @Override
        public boolean isOverridden(Class<?> $) {
            return false;
        }

        @Override
        public MethodOverriddenDeterminer withCache() {
            return this;
        }
    };

    private interface MODI extends MethodOverriddenDeterminer {
        Class<?> base();
    }

    public static MethodOverriddenDeterminer of(
            MethodHandles.Lookup lookup,
            Method base
    ) throws NoSuchMethodException, IllegalAccessException {
        return of($ -> lookup, base);
    }

    public static MethodOverriddenDeterminer of(
            Function<Class<?>, MethodHandles.Lookup> lookup,
            Method base
    ) throws NoSuchMethodException, IllegalAccessException {
        if (Modifier.isFinal(base.getModifiers() | base.getDeclaringClass().getModifiers())) return ALWAYS_FALSE;
        if (Modifier.isStatic(base.getModifiers())) return ALWAYS_FALSE;
        return of(lookup, base.getDeclaringClass(), base.getName(), MethodType.methodType(base.getReturnType(), base.getParameterTypes()));
    }

    public static MethodOverriddenDeterminer withCache(MethodOverriddenDeterminer delegate) {
        if (delegate == null || delegate == ALWAYS_FALSE) return delegate;
        class WithCache implements MethodOverriddenDeterminer {
            private final WeakHashMap<Class<?>, Boolean> map = new WeakHashMap<>();
            private final Function<Class<?>, Boolean> mapping = delegate::isOverridden;

            @Override
            public boolean isOverridden(Class<?> klass) {
                if (klass == null) return false;
                return map.computeIfAbsent(klass, mapping);
            }

            @Override
            public MethodOverriddenDeterminer withCache() {
                return this;
            }
        }
        if (delegate instanceof WithCache) return delegate;
        if (delegate instanceof MODI) {
            Class<?> base = ((MODI) delegate).base();
            return new WithCache() {
                @Override
                public boolean isOverridden(Class<?> klass) {
                    if (klass == base || klass == null) return false;
                    if (!base.isAssignableFrom(klass)) return false;
                    return super.isOverridden(klass);
                }
            };
        }
        return new WithCache();
    }

    public static MethodOverriddenDeterminer of(
            MethodHandles.Lookup lookup,
            Class<?> base,
            String name,
            MethodType type
    ) throws NoSuchMethodException, IllegalAccessException {
        return of($ -> lookup, base, name, type);
    }

    public static MethodOverriddenDeterminer of(
            Function<Class<?>, MethodHandles.Lookup> lookupF,
            Class<?> base,
            String name,
            MethodType type
    ) throws NoSuchMethodException, IllegalAccessException {
        if (Modifier.isFinal(base.getModifiers())) return ALWAYS_FALSE;
        MethodHandles.Lookup blp = lookupF.apply(base);
        int modifiers = blp.revealDirect(
                blp.findVirtual(base, name, type)
        ).getModifiers();
        if (Modifier.isPrivate(modifiers)) return ALWAYS_FALSE;
        if (Modifier.isFinal(modifiers)) return ALWAYS_FALSE;
        return new MODI() {
            @Override
            public Class<?> base() {
                return base;
            }

            @Override
            public boolean isOverridden(Class<?> klass) {
                if (klass == null || klass == base) return false;
                if (!base.isAssignableFrom(klass)) return false;
                try {
                    MethodHandles.Lookup lk = lookupF.apply(klass);
                    return lk.revealDirect(lk.findVirtual(klass, name, type)).getDeclaringClass() != base;
                } catch (NoSuchMethodException e) {
                    throw new AssertionError(e);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }
}
