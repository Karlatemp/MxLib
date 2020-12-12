package io.github.karlatemp.mxlib.format;

import io.github.karlatemp.mxlib.utils.StringBuilderFormattable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public interface FormatArguments {
    boolean containsKey(int key);

    void append(@NotNull StringBuilder buffer, int key);

    @Nullable Object get(int key);

    default Object magicValue() {
        return null;
    }

    int arguments();

    abstract class AbstractArgument implements FormatArguments {

        @Override
        public boolean containsKey(int key) {
            return true;
        }

        @Override
        public int arguments() {
            return 0;
        }

        @Override
        public void append(@NotNull StringBuilder buffer, int key) {
            StringBuilderFormattable.append(buffer, get(key));
        }

        @Override
        public @Nullable Object get(int key) {
            return null;
        }
    }

    static @NotNull FormatArguments EMPTY = new FormatArguments() {
        @Override
        public boolean containsKey(int key) {
            return false;
        }

        @Override
        public void append(@NotNull StringBuilder buffer, int key) {
        }

        @Override
        public int arguments() {
            return 0;
        }

        @Override
        public @Nullable Object get(int key) {
            return null;
        }
    };

    static @NotNull FormatArguments by(Object... arguments) {
        if (arguments == null) return EMPTY;
        return by(Arrays.asList(arguments));
    }

    static @NotNull FormatArguments by(List<?> arguments) {
        if (arguments == null || arguments.isEmpty()) {
            return EMPTY;
        }
        class F implements FormatArguments {
            @Override
            public boolean containsKey(int key) {
                return key >= 0 && key < arguments.size();
            }

            @Override
            public void append(@NotNull StringBuilder buffer, int key) {
                StringBuilderFormattable.append(buffer, get(key));
            }

            @Override
            public int arguments() {
                return arguments.size();
            }

            @Override
            public @Nullable Object get(int key) {
                if (containsKey(key)) {
                    return arguments.get(key);
                }
                return null;
            }
        }
        return new F();
    }
}
