/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-api.main/MethodOverridenDeterminer.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.utils;

import io.github.karlatemp.mxlib.internal.MethodOverriddenDeterminers;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.util.function.Function;

/**
 * Test a class override one method or not
 *
 * @since 3.0-dev-16
 */
public interface MethodOverriddenDeterminer {
    boolean isOverridden(Class<?> klass);

    default MethodOverriddenDeterminer withCache() {
        return MethodOverriddenDeterminers.withCache(this);
    }

    static MethodOverriddenDeterminer of(
            MethodHandles.Lookup lookup,
            Method method
    ) throws NoSuchMethodException, IllegalAccessException {
        return MethodOverriddenDeterminers.of(lookup, method);
    }

    static MethodOverriddenDeterminer of(
            MethodHandles.Lookup lookup,
            Class<?> base,
            String name,
            MethodType type
    ) throws NoSuchMethodException, IllegalAccessException {
        return MethodOverriddenDeterminers.of(lookup, base, name, type);
    }

    static MethodOverriddenDeterminer of(
            Function<Class<?>, MethodHandles.Lookup> lookup,
            Method method
    ) throws NoSuchMethodException, IllegalAccessException {
        return MethodOverriddenDeterminers.of(lookup, method);
    }

    static MethodOverriddenDeterminer of(
            Function<Class<?>, MethodHandles.Lookup> lookup,
            Class<?> base,
            String name,
            MethodType type
    ) throws NoSuchMethodException, IllegalAccessException {
        return MethodOverriddenDeterminers.of(lookup, base, name, type);
    }
}
