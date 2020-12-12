package io.github.karlatemp.mxlib.injector;

import io.github.karlatemp.mxlib.exception.ValueInitializedException;
import io.github.karlatemp.mxlib.exception.ValueNotInitializedException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class Injected<T> {
    protected final Class<T> type;
    protected final String name;
    protected T value;
    protected boolean initialized;

    public Injected(@NotNull Class<T> type, @Nullable String name) {
        this.type = type;
        this.name = name;
    }

    public Injected(@NotNull Class<T> type) {
        this(type, null);
    }

    public Class<?> getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public @Nullable T getValueDirect() {
        return value;
    }

    public abstract T getValue();

    public abstract void initialize(T value);

    public abstract boolean isNullable();

    public boolean isInitialized() {
        return initialized;
    }

    public static class Nillable<T> extends Injected<T> {
        public Nillable(@NotNull Class<T> type, @Nullable String name) {
            super(type, name);
        }

        public Nillable(@NotNull Class<T> type) {
            super(type);
        }

        @Override
        public @Nullable T getValue() {
            return value;
        }

        @Override
        public void initialize(T value) {
            if (initialized) throw new ValueInitializedException();
            synchronized (this) {
                if (initialized) throw new ValueInitializedException();
                initialized = true;
                this.value = value;
            }
        }

        @Override
        public boolean isNullable() {
            return true;
        }
    }

    public static class Nonnull<T> extends Injected<T> {
        public Nonnull(@NotNull Class<T> type, @Nullable String name) {
            super(type, name);
        }

        public Nonnull(@NotNull Class<T> type) {
            super(type);
        }

        @Override
        public @NotNull T getValue() {
            if (!initialized) throw new ValueNotInitializedException();
            assert value != null;
            return value;
        }

        @Override
        public void initialize(@NotNull T value) {
            if (initialized) throw new ValueInitializedException();
            synchronized (this) {
                if (initialized) throw new ValueInitializedException();
                initialized = true;
                this.value = value;
            }
        }

        @Override
        public boolean isNullable() {
            return false;
        }
    }
}
