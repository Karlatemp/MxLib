/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-api.main/Functions.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.*;

@SuppressWarnings({"unchecked", "rawtypes"})
public class Functions {
    private enum ToStringFunc implements Function<Object, String> {
        I;

        @Override
        public String apply(Object o) {
            return StringBuilderFormattable.toString(o);
        }
    }

    public static @NotNull <T> Function<T, String> toStringFunction() {
        return (Function<T, String>) ToStringFunc.I;
    }

    private enum IdentityFunc implements Function<Object, Object> {
        I;

        @Override
        public Object apply(Object o) {
            return o;
        }
    }

    public static @NotNull <T extends R, R> Function<T, R> identity() {
        return (Function<T, R>) (Function) IdentityFunc.I;
    }

    public static @NotNull <T, E> Function<T, E> constant(@Nullable E value) {
        return $ -> value;
    }

    public static @NotNull <T> ToIntFunction<T> constantInt(int value) {
        return $ -> value;
    }

    public static @NotNull <T> ToDoubleFunction<T> constantDouble(int value) {
        return $ -> value;
    }

    public static @NotNull <T> ToLongFunction<T> constantLong(int value) {
        return $ -> value;
    }

    public static @NotNull <T, R> Function<T, R> forSupplier(@NotNull Supplier<R> supplier) {
        return $ -> supplier.get();
    }

    public static @NotNull <T, R> Function<T, R> alwaysNull() {
        return (Function<T, R>) AlwaysNull.I;
    }

    enum AlwaysNull implements Function<Object, Object> {
        I;

        @Override
        public Object apply(Object o) {
            return null;
        }
    }
}
