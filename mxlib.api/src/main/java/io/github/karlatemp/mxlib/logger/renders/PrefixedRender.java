/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-api.main/PrefixedRender.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.logger.renders;

import io.github.karlatemp.mxlib.logger.MMarket;
import io.github.karlatemp.mxlib.logger.MessageRender;
import io.github.karlatemp.mxlib.utils.StringBuilderFormattable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class PrefixedRender implements MessageRender {
    private final PrefixSupplier prefixSupplier;
    private final MessageRender render;

    public interface PrefixSupplier {
        @NotNull CharSequence rendPrefix(
                @Nullable String loggerName,
                @Nullable MMarket market,
                @NotNull StringBuilder content,
                boolean isError,
                @Nullable Level level,
                @Nullable LogRecord record
        );

        default @NotNull PrefixSupplier plus(@NotNull PrefixSupplier next) {
            return new PrefixSupplierInternal.PlusImpl(this, next);
        }

        default @NotNull PrefixSupplier plus(@NotNull CharSequence next) {
            return new PrefixSupplierInternal.PlusCharSequence(this, next);
        }

        static @NotNull PrefixSupplier constant(@NotNull CharSequence charSequence) {
            return new PrefixSupplier() {
                @Override
                public @NotNull CharSequence rendPrefix(@Nullable String loggerName, @Nullable MMarket market, @NotNull StringBuilder content, boolean isError, @Nullable Level level, @Nullable LogRecord record) {
                    return new StringBuilder(charSequence);
                }

                @Override
                public @NotNull PrefixSupplier plus(@NotNull PrefixSupplier next) {
                    return new PrefixSupplierInternal.StringBuilderedPlusImpl(this, next);
                }
            };
        }

        static @NotNull PrefixSupplier bySupplier(@NotNull Supplier<CharSequence> supplier) {
            return (loggerName, market, content, isError, level, record) -> supplier.get();
        }

        static @NotNull PrefixSupplier dated(@NotNull DateTimeFormatter formatter) {
            return new DatedSupplier(formatter);
        }

        static @NotNull PrefixSupplier loggerName() {
            return PrefixSupplierInternal.LoggerName.I;
        }

        static @NotNull PrefixSupplier loggerAndRecordName() {
            return PrefixSupplierInternal.LoggerName.LRC;
        }

        static @NotNull PrefixSupplier loggerLevel() {
            return PrefixSupplierInternal.LevelSup.I;
        }

        default @NotNull PrefixSupplier aligned() {
            return AlignedSupplier.by(this);
        }

        default @NotNull PrefixSupplier aligned(@Nullable AlignedSupplier.AlignType type) {
            return AlignedSupplier.by(this, type);
        }
    }

    public static class DatedSupplier implements PrefixSupplier {
        private final DateTimeFormatter formatter;

        public DatedSupplier(@NotNull DateTimeFormatter formatter) {
            this.formatter = formatter;
        }

        @Override
        public @NotNull CharSequence rendPrefix(@Nullable String loggerName, @Nullable MMarket market, @NotNull StringBuilder content, boolean isError, @Nullable Level level, @Nullable LogRecord record) {
            return formatter.format(Instant.now().atZone(ZoneId.systemDefault()));
        }
    }

    public abstract static class AlignedSupplier implements PrefixSupplier {
        public static enum AlignType {
            LEFT {
                @Override
                public StringBuilder apply(int size, int length, CharSequence charSequence) {
                    int emp = size - length;
                    StringBuilder builder = new StringBuilder(Math.max(charSequence.length(), length) + emp);
                    builder.append(charSequence);
                    appendEmpty(builder, emp);
                    return builder;
                }
            }, CENTER {
                @Override
                public StringBuilder apply(int size, int length, CharSequence charSequence) {
                    int emp = size - length;
                    int left = emp / 2;
                    int right = emp - left;
                    StringBuilder builder = new StringBuilder(Math.max(charSequence.length(), length) + emp);
                    appendEmpty(builder, left);
                    builder.append(charSequence);
                    appendEmpty(builder, right);
                    return builder;
                }
            }, RIGHT {
                @Override
                public StringBuilder apply(int size, int length, CharSequence charSequence) {
                    int emp = size - length;
                    StringBuilder builder = new StringBuilder(Math.max(charSequence.length(), length) + emp);
                    appendEmpty(builder, emp);
                    builder.append(charSequence);
                    return builder;
                }
            };

            public abstract StringBuilder apply(int size, int length, CharSequence charSequence);

            public CharSequence apply(int length, AtomicInteger maxLength, CharSequence c) {
                while (true) {
                    int old = maxLength.get();
                    if (old <= length) {
                        if (maxLength.compareAndSet(old, length)) {
                            return c;
                        }
                    } else {
                        if (maxLength.compareAndSet(old, old)) {
                            return apply(old, length, c);
                        }
                    }
                }
            }
        }

        public static @NotNull AlignedSupplier by(@NotNull PrefixSupplier prefixSupplier, @Nullable AlignType alignType) {
            AlignType at = alignType;
            return new AlignedSupplier() {
                {
                    if (at != null) {
                        this.alignType = at;
                    }
                }

                @Override
                protected @NotNull CharSequence rendPrefix(@Nullable String loggerName, @Nullable MMarket market, @NotNull StringBuilder content, boolean isError, @Nullable Level level, @Nullable LogRecord record, int[] length) {
                    return prefixSupplier.rendPrefix(loggerName, market, content, isError, level, record);
                }
            };
        }

        public static @NotNull AlignedSupplier by(@NotNull PrefixSupplier prefixSupplier) {
            return by(prefixSupplier, null);
        }

        private static final char[] emptyLine = new char[255];

        static {
            Arrays.fill(emptyLine, ' ');
        }

        protected static void appendEmpty(StringBuilder builder, int length) {
            while (length > 0) {
                int rec = Math.min(length, emptyLine.length);
                builder.append(emptyLine, 0, rec);
                length -= rec;
            }
        }

        protected final AtomicInteger maxLength = new AtomicInteger(0);
        protected @NotNull AlignType alignType = AlignType.CENTER;

        protected abstract @NotNull CharSequence rendPrefix(
                @Nullable String loggerName,
                @Nullable MMarket market,
                @NotNull StringBuilder content,
                boolean isError,
                @Nullable Level level,
                @Nullable LogRecord record,
                int[] length
        );

        @Override
        public @NotNull CharSequence rendPrefix(
                @Nullable String loggerName,
                @Nullable MMarket market,
                @NotNull StringBuilder content,
                boolean isError,
                @Nullable Level level,
                @Nullable LogRecord record) {
            int[] len = new int[1];
            CharSequence c = rendPrefix(loggerName, market, content, isError, level, record, len);
            int l = len[0];
            if (l == 0) {
                l = c.length();
            }
            return alignType.apply(l, maxLength, c);
        }
    }

    public PrefixedRender(
            @NotNull MessageRender render,
            @NotNull PrefixSupplier prefixSupplier
    ) {
        this.render = render;
        this.prefixSupplier = prefixSupplier;
    }

    @Override
    public @NotNull StringBuilder render(
            @Nullable String loggerName, @Nullable MMarket market,
            @Nullable StringBuilderFormattable message,
            boolean isError,
            @Nullable Level level,
            @Nullable LogRecord record
    ) {
        StringBuilder content = render.render(null, market, message, isError, level, record);
        CharSequence prefix = prefixSupplier.rendPrefix(loggerName, market, content, isError, level, record);
        content.insert(0, prefix);
        int start = prefix.length();
        while (start < content.length()) {
            if (content.charAt(start) == '\n') {
                content.insert(start + 1, prefix);
                start += prefix.length();
            }
            start++;
        }
        return content;
    }
}
