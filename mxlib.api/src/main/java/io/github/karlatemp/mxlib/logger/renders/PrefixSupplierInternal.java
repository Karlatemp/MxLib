/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-api.main/PrefixSupplierInternal.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.logger.renders;

import io.github.karlatemp.mxlib.logger.MMarket;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.logging.Level;
import java.util.logging.LogRecord;

class PrefixSupplierInternal {
    static class StringBuilderedPlusImpl extends PlusImpl {
        public StringBuilderedPlusImpl(PrefixedRender.PrefixSupplier p1, PrefixedRender.PrefixSupplier p2) {
            super(p1, p2);
        }

        @Override
        public @NotNull CharSequence rendPrefix(@Nullable String loggerName, @Nullable MMarket market, @NotNull StringBuilder content, boolean isError, @Nullable Level level, @Nullable LogRecord record) {
            return ((StringBuilder) p1.rendPrefix(loggerName, market, content, isError, level, record))
                    .append(p2.rendPrefix(loggerName, market, content, isError, level, record));
        }

        @Override
        public PrefixedRender.@NotNull PrefixSupplier plus(PrefixedRender.@NotNull PrefixSupplier next) {
            return new StringBuilderedPlusImpl(this, next);
        }
    }

    static class PlusImpl implements PrefixedRender.PrefixSupplier {
        final PrefixedRender.PrefixSupplier p1;
        final PrefixedRender.PrefixSupplier p2;

        public PlusImpl(PrefixedRender.PrefixSupplier p1, PrefixedRender.PrefixSupplier p2) {
            this.p1 = p1;
            this.p2 = p2;
        }

        @Override
        public @NotNull CharSequence rendPrefix(@Nullable String loggerName, @Nullable MMarket market, @NotNull StringBuilder content, boolean isError, @Nullable Level level, @Nullable LogRecord record) {
            CharSequence prefix = p1.rendPrefix(loggerName, market, content, isError, level, record);
            CharSequence nt = p2.rendPrefix(loggerName, market, content, isError, level, record);
            if (prefix instanceof StringBuilder) {
                ((StringBuilder) prefix).append(nt);
                return prefix;
            }
            if (nt instanceof StringBuilder) {
                ((StringBuilder) nt).insert(0, prefix);
                return nt;
            }
            return new StringBuilder(prefix).append(nt);
        }

        @Override
        public PrefixedRender.@NotNull PrefixSupplier plus(PrefixedRender.@NotNull PrefixSupplier next) {
            return new StringBuilderedPlusImpl(this, next);
        }

        @Override
        public PrefixedRender.@NotNull PrefixSupplier plus(@NotNull CharSequence next) {
            return new StringBuilderedSeq(this, next);
        }
    }

    static class StringBuilderedSeq extends PlusCharSequence {
        StringBuilderedSeq(PrefixedRender.PrefixSupplier prefixSupplier, @NotNull CharSequence next) {
            super(prefixSupplier, next);
        }

        @Override
        public @NotNull CharSequence rendPrefix(@Nullable String loggerName, @Nullable MMarket market, @NotNull StringBuilder content, boolean isError, @Nullable Level level, @Nullable LogRecord record) {
            return ((StringBuilder) p.rendPrefix(loggerName, market, content, isError, level, record)).append(next);
        }
    }

    static class PlusCharSequence implements PrefixedRender.PrefixSupplier {
        final CharSequence next;
        final PrefixedRender.PrefixSupplier p;

        PlusCharSequence(PrefixedRender.PrefixSupplier prefixSupplier, @NotNull CharSequence next) {
            this.p = prefixSupplier;
            this.next = next;
        }

        @Override
        public @NotNull CharSequence rendPrefix(@Nullable String loggerName, @Nullable MMarket market, @NotNull StringBuilder content, boolean isError, @Nullable Level level, @Nullable LogRecord record) {
            CharSequence prefix = p.rendPrefix(loggerName, market, content, isError, level, record);
            if (prefix instanceof StringBuilder)
                return ((StringBuilder) prefix).append(next);
            return new StringBuilder(prefix).append(next);
        }

        @Override
        public PrefixedRender.@NotNull PrefixSupplier plus(PrefixedRender.@NotNull PrefixSupplier next) {
            return new StringBuilderedPlusImpl(this, next);
        }

        @Override
        public PrefixedRender.@NotNull PrefixSupplier plus(@NotNull CharSequence next) {
            return new StringBuilderedSeq(this, next);
        }
    }

    enum LoggerName implements PrefixedRender.PrefixSupplier {
        I(false), LRC(true);
        private boolean record;

        LoggerName(boolean rc) {
            this.record = rc;
        }

        @Override
        public @NotNull CharSequence rendPrefix(
                @Nullable String loggerName,
                @Nullable MMarket market,
                @NotNull StringBuilder content,
                boolean isError,
                @Nullable Level level,
                @Nullable LogRecord record
        ) {
            if (this.record && record != null) {
                String recordLoggerName = record.getLoggerName();
                if (recordLoggerName != null) {
                    return recordLoggerName;
                }
            }
            if (loggerName == null) return "unknown";
            return loggerName;
        }
    }

    enum LevelSup implements PrefixedRender.PrefixSupplier {
        I;

        @Override
        public @NotNull CharSequence rendPrefix(@Nullable String loggerName, @Nullable MMarket market, @NotNull StringBuilder content, boolean isError, @Nullable Level level, @Nullable LogRecord record) {
            if (level == null) return "INFO";
            return level.getName();
        }
    }
}
