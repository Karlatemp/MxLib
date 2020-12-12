package io.github.karlatemp.mxlib.utils;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

@SuppressWarnings({"rawtypes", "unchecked"})
public class Suppliers {
    enum NULL implements Supplier {
        I;

        @Override
        public Object get() {
            return null;
        }
    }

    @Contract(pure = true)
    public static <T> @NotNull Supplier<@Nullable T> alwaysNull() {
        return (Supplier<T>) NULL.I;
    }

    @Contract(pure = true)
    public static <T> @NotNull Supplier<T> constant(T value) {
        if (value == null) return alwaysNull();
        return new Constant<>(value);
    }

    static class Constant<T> implements Supplier<T>, Lazy<T> {
        private final T v;

        Constant(T v) {
            this.v = v;
        }

        public T get() {
            return v;
        }

        @Override
        public String toString() {
            return String.valueOf(v);
        }
    }
}

