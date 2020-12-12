package io.github.karlatemp.mxlib.utils;

import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

@SuppressWarnings("unchecked")
public class Predicates {
    private static final Predicate<Object> FALSE = new Predicate<Object>() {
        @Override
        public boolean test(Object o) {
            return false;
        }

        @NotNull
        @Override
        public Predicate<Object> and(@NotNull Predicate<? super Object> other) {
            return this;
        }

        @NotNull
        @Override
        public Predicate<Object> or(@NotNull Predicate<? super Object> other) {
            return other;
        }

        @NotNull
        @Override
        public Predicate<Object> negate() {
            return TRUE;
        }
    };
    private static final Predicate<Object> TRUE = new Predicate<Object>() {
        @Override
        public boolean test(Object o) {
            return true;
        }

        @NotNull
        @Override
        public Predicate<Object> and(@NotNull Predicate<? super Object> other) {
            return other;
        }

        @NotNull
        @Override
        public Predicate<Object> or(@NotNull Predicate<? super Object> other) {
            return this;
        }

        @NotNull
        @Override
        public Predicate<Object> negate() {
            return FALSE;
        }
    };

    public static <T> Predicate<T> alwaysTrue() {
        return (Predicate<T>) TRUE;
    }

    public static <T> Predicate<T> alwaysFalse() {
        return (Predicate<T>) FALSE;
    }

    public static <T> Predicate<T> always(boolean status) {
        return (Predicate<T>) (status ? TRUE : FALSE);
    }

    public static <T> Predicate<T> constant(boolean status) {
        return (Predicate<T>) (status ? TRUE : FALSE);
    }

    public static Predicate<Object> isNull() {
        return NullPredicate.IS_NULL;
    }

    public static Predicate<Object> notNull() {
        return NullPredicate.NOT_NULL;
    }

    public static Predicate<Object> instanceOf(Class<?> type) {
        return new InstanceOf(type);
    }

    public static Predicate<Object> notInstanceOf(Class<?> type) {
        return new NotInstanceOf(type);
    }

    enum NullPredicate implements Predicate<Object> {
        IS_NULL {
            @Override
            public boolean test(Object o) {
                return o == null;
            }

            @NotNull
            @Override
            public Predicate<Object> negate() {
                return NOT_NULL;
            }
        }, NOT_NULL {
            @Override
            public boolean test(Object o) {
                return o != null;
            }

            @NotNull
            @Override
            public Predicate<Object> negate() {
                return IS_NULL;
            }
        }
    }

    static class NotInstanceOf implements Predicate<Object> {
        private final Class<?> type;

        NotInstanceOf(Class<?> type) {
            this.type = type;
        }

        @Override
        public boolean test(Object obj) {
            return !type.isInstance(obj);
        }

        @NotNull
        @Override
        public Predicate<Object> negate() {
            return new InstanceOf(type);
        }
    }

    static class InstanceOf implements Predicate<Object> {
        private final Class<?> type;

        InstanceOf(Class<?> type) {
            this.type = type;
        }

        @Override
        public boolean test(Object obj) {
            return type.isInstance(obj);
        }

        @NotNull
        @Override
        public Predicate<Object> negate() {
            return new NotInstanceOf(type);
        }
    }
}
