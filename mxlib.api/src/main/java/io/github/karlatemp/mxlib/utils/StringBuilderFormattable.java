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

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public interface StringBuilderFormattable {
    static StringBuilderFormattable LN = by("\n");
    static StringBuilderFormattable LT = by("\t");
    static StringBuilderFormattable EMPTY = $ -> {
    };

    void formatTo(@NotNull StringBuilder builder);

    static String toString(Object object) {
        if (object instanceof StringBuilderFormattable) {
            StringBuilder builder = new StringBuilder();
            ((StringBuilderFormattable) object).formatTo(builder);
            return builder.toString();
        }
        return String.valueOf(object);
    }

    static void append(@NotNull StringBuilder builder, Object value) {
        if (value instanceof StringBuilderFormattable) {
            ((StringBuilderFormattable) value).formatTo(builder);
        } else if (value instanceof CharSequence) {
            builder.append((CharSequence) value);
        } else {
            builder.append(value);
        }
    }

    static StringBuilderFormattable byLazyToString(Object any) {
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

    default String to_string() {
        return toString(this);
    }

    static StringBuilderFormattable by(CharSequence charSequence) {
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

    static StringBuilderFormattable by(Supplier<CharSequence> supplier) {
        return new StringBuilderFormattable() {
            @Override
            public void formatTo(@NotNull StringBuilder builder) {
                builder.append(supplier.get());
            }

            @Override
            public String toString() {
                return supplier.get().toString();
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

        @Override
        public @NotNull StringBuilderFormattable plusMsg(
                @NotNull Object next_
        ) {
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

    static StringBuilderFormattable by(CharSequence charSequence, int start, int end) {
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

    default @NotNull StringBuilderFormattable plusMsg(
            @NotNull Object next
    ) {
        Link link = new Link();
        link.list.add(this);
        link.list.add(next);
        return link;
    }

    default @NotNull StringBuilderFormattable.Link asLink() {
        Link link = new Link();
        link.list.add(this);
        return link;
    }

    default StringBuilderFormattable copy() {
        return this;
    }

    default @NotNull Object lazyToStringBridge() {
        return new Object() {
            @Override
            public String toString() {
                return to_string();
            }
        };
    }
}
