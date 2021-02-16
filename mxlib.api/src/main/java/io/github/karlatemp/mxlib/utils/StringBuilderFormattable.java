/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-api.main/StringBuilderFormattable.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.utils;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * Objects that implement this interface can be converted to text,
 * ideally without allocating temporary objects.
 *
 * @apiNote Always use {@link #to_string()} not {@link #toString()}
 */
public interface StringBuilderFormattable {
    /**
     * Constant {@code "\n"}
     */
    @NotNull
    static StringBuilderFormattable LN = by("\n");
    /**
     * Constant {@code "\t"}
     */
    @NotNull
    static StringBuilderFormattable LT = by("\t");
    /**
     * Constant {@code "null"}
     *
     * @since 3.0-dev-15
     */
    @NotNull
    static StringBuilderFormattable NULL = by("null");
    /**
     * Constant {@code ""}
     */
    @NotNull
    static StringBuilderFormattable EMPTY = new StringBuilderFormattable() {
        @Override
        public void formatTo(@NotNull StringBuilder builder) {
        }

        @Override
        public String to_string() {
            return "";
        }

        @Override
        public String toString() {
            return "";
        }

        @Override
        public @NotNull StringBuilderFormattable.Link asLink() {
            return new Link();
        }

        @Override
        public @NotNull StringBuilderFormattable plusMsg(@NotNull Object next) {
            if (next instanceof StringBuilderFormattable) return (StringBuilderFormattable) next;
            return byLazyToString(next);
        }

        @Override
        public @NotNull Object lazyToStringBridge() {
            return this;
        }

        @Override
        public StringBuilderFormattable copy() {
            return this;
        }
    };

    /**
     * Writes a text representation of this object into the specified {@code StringBuilder}, ideally without allocating
     * temporary objects.
     *
     * @param builder the StringBuilder to write into
     */
    void formatTo(@NotNull StringBuilder builder);

    /**
     * Returns the string representation of the Object argument.
     *
     * @see #formatTo(StringBuilder)
     * @see String#valueOf(Object)
     */
    static String toString(Object object) {
        if (object instanceof StringBuilderFormattable) {
            StringBuilder builder = new StringBuilder();
            ((StringBuilderFormattable) object).formatTo(builder);
            return builder.toString();
        }
        return String.valueOf(object);
    }

    /**
     * Append a value into StringBuilder
     */
    static void append(@NotNull StringBuilder builder, Object value) {
        if (value instanceof StringBuilderFormattable) {
            ((StringBuilderFormattable) value).formatTo(builder);
        } else if (value instanceof CharSequence) {
            builder.append((CharSequence) value);
        } else {
            builder.append(value);
        }
    }

    /**
     * Get {@link StringBuilderFormattable} from any object
     */
    @Contract(pure = true)
    @NotNull
    static StringBuilderFormattable byLazyToString(Object any) {
        if (any == null) return NULL;
        if (any instanceof StringBuilderFormattable) return (StringBuilderFormattable) any;

        return new StringBuilderFormattable() {
            @Override
            public void formatTo(@NotNull StringBuilder builder) {
                append(builder, any);
            }

            @Override
            public String toString() {
                return any.toString();
            }

            @Override
            public @NotNull Object lazyToStringBridge() {
                return this;
            }
        };
    }

    /**
     * Returns the string representation of the Object argument.
     * <p>
     * Use this method for toString not {@link #toString()}
     */
    default String to_string() {
        return toString(this);
    }

    /**
     * Get {@link StringBuilderFormattable} by a {@link CharSequence}
     */
    @Contract(pure = true)
    @NotNull
    static StringBuilderFormattable by(CharSequence charSequence) {
        if (charSequence == null) {
            //noinspection ConstantConditions
            return NULL;
        }
        if (charSequence instanceof String && charSequence.length() == 0) {
            //noinspection ConstantConditions
            return EMPTY;
        }
        return new StringBuilderFormattable() {
            @Override
            public void formatTo(@NotNull StringBuilder builder) {
                builder.append(charSequence);
            }

            @Override
            public String toString() {
                return charSequence.toString();
            }

            @Override
            public @NotNull Object lazyToStringBridge() {
                return this;
            }

            @Override
            public String to_string() {
                return charSequence.toString();
            }
        };
    }

    /**
     * Get {@link StringBuilderFormattable} by a {@link Supplier<String>}
     */
    @Contract(pure = true)
    @NotNull
    static StringBuilderFormattable by(Supplier<CharSequence> supplier) {
        return new StringBuilderFormattable() {
            @Override
            public void formatTo(@NotNull StringBuilder builder) {
                builder.append(supplier.get());
            }

            @Override
            public String toString() {
                return String.valueOf(supplier.get());
            }

            @Override
            public @NotNull Object lazyToStringBridge() {
                return this;
            }

            @Override
            public String to_string() {
                return toString();
            }
        };
    }

    @Contract(pure = true, value = "null -> null; !null -> !null")
    static StringBuilderFormattable by(StringBuilderFormattable msg) {
        return msg;
    }

    /**
     * A link that with many contents
     */
    static class Link implements StringBuilderFormattable {
        protected List<Object> list = new ArrayList<>();

        public List<Object> getList() {
            return list;
        }

        @Override
        public void formatTo(@NotNull StringBuilder builder) {
            for (Object s : list) {
                append(builder, s);
            }
        }

        /**
         * Add a content into this link.
         *
         * @return this
         */
        @Override
        public @NotNull StringBuilderFormattable plusMsg(
                @NotNull Object next_
        ) {
            if (next_ == EMPTY) return this;
            list.add(next_);
            return this;
        }

        @Override
        public @NotNull StringBuilderFormattable.Link asLink() {
            return this;
        }

        @Override
        public String toString() {
            return to_string();
        }

        /**
         * Copy a new link
         */
        public Link copy() {
            Link l = new Link();
            l.list.addAll(list);
            return l;
        }

        @Override
        public @NotNull Object lazyToStringBridge() {
            return this;
        }
    }

    /**
     * Get {@link StringBuilderFormattable} by a {@link CharSequence}
     */
    static StringBuilderFormattable by(CharSequence charSequence, int start, int end) {
        if (charSequence == null) {
            if (start == 0) {
                if (end == 0) {
                    return EMPTY;
                }
                if (end < 0) {
                    return NULL;
                }
            }
            throw new NullPointerException("charSequence");
        }
        if (start == end) {
            return EMPTY;
        }
        return new StringBuilderFormattable() {
            @Override
            public void formatTo(@NotNull StringBuilder builder) {
                builder.append(charSequence, start, end);
            }

            @Override
            public String toString() {
                return charSequence.subSequence(start, end).toString();
            }

            @Override
            public String to_string() {
                return toString();
            }

            @Override
            public @NotNull Object lazyToStringBridge() {
                return this;
            }
        };
    }

    /**
     * Plus a content
     *
     * @apiNote If this is a {@link Link}, content will be plus into this and return this
     * @see #copy()
     * @see Link#plusMsg(Object)
     * @see Link
     */
    default @NotNull StringBuilderFormattable plusMsg(
            @NotNull Object next
    ) {
        if (next == EMPTY) return this;
        Link link = new Link();
        link.list.add(this);
        link.list.add(next);
        return link;
    }

    /**
     * Convert this into a {@link Link}
     */
    default @NotNull StringBuilderFormattable.Link asLink() {
        Link link = new Link();
        link.list.add(this);
        return link;
    }

    /**
     * Copy a new link
     *
     * @apiNote No effect if this is not a link
     */
    default StringBuilderFormattable copy() {
        return this;
    }

    /**
     * Get a object that {@link Object#toString()} returns {@link #to_string()}
     */
    @Contract(pure = true)
    default @NotNull Object lazyToStringBridge() {
        return new Object() {
            @Override
            public String toString() {
                return to_string();
            }
        };
    }
}
