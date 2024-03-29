/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-api.main/PrefixSupplierBuilder.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.logger.renders;

import io.github.karlatemp.mxlib.internal.PrefixSupplierParser;
import io.github.karlatemp.mxlib.logger.renders.PrefixedRender.PrefixSupplier;
import org.jetbrains.annotations.NotNull;

import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.function.Supplier;

/**
 * The builder for building {@link PrefixSupplier}
 *
 * <pre>{@code
 * PrefixSupplier supplier = new PrefixSupplierBuilder()
 *                 .append(StringUtils.BkColors._B)
 *                 .dated(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
 *                 .append(" " + StringUtils.BkColors._5)
 *                 .dated(DateTimeFormatter.ofPattern("HH:mm:ss"))
 *                 .append(StringUtils.BkColors.RESET + " [" + StringUtils.BkColors._6)
 *                 .loggerName().aligned(PrefixedRender.AlignedSupplier.AlignType.LEFT)
 *                 .append(StringUtils.BkColors.RESET + "] [" + StringUtils.BkColors._B)
 *                 .loggerLevel().aligned(PrefixedRender.AlignedSupplier.AlignType.CENTER)
 *                 .append(StringUtils.BkColors.RESET + "] ")
 *                 .complete()
 * }</pre>
 */
public class PrefixSupplierBuilder {
    /**
     * Parse from file,
     * <p>
     * Example:
     * <pre>
     * {@code
     * #enable bk-color
     * #enable unicode
     *
     * §b
     * #date yyyy-MM-dd
     *  §5
     * #date HH:mm:ss
     * §r [§6
     *
     * #aligned LEFT
     *
     * ## #logger-name
     * #logger-and-record-name
     *
     * #end-aligned
     *
     * §r] [§b
     *
     * #aligned CENTER
     * #logger-level
     * #end
     *
     * §r]
     * #space 1
     * }
     * </pre>
     *
     * @since 3.0-dev-18
     */
    public static PrefixSupplier parseFrom(Scanner scanner) {
        return PrefixSupplierParser.parse(scanner);
    }

    protected PrefixSupplier current;
    protected PrefixSupplier last;

    public PrefixSupplierBuilder() {
        this(PrefixSupplier.EMPTY);
    }

    public PrefixSupplierBuilder(PrefixSupplier supplier) {
        current = PrefixSupplier.EMPTY;
        this.last = supplier;
    }

    private boolean shouldPlusLast() {
        return last != null && last != PrefixSupplier.EMPTY;
    }

    public PrefixSupplierBuilder append(@NotNull PrefixSupplier next) {
        if (shouldPlusLast()) {
            current = current.plus(last);
        }
        last = next;
        return this;
    }

    public PrefixSupplierBuilder append(@NotNull CharSequence next) {
        if (next.length() == 0) return this;
        if (shouldPlusLast()) {
            current = current.plus(last);
        }

        current = current.plus(next);
        last = null;
        return this;
    }

    public PrefixSupplierBuilder append(@NotNull Supplier<CharSequence> supplier) {
        return append(PrefixSupplier.bySupplier(supplier));
    }

    public PrefixSupplierBuilder dated(DateTimeFormatter formatter) {
        return append(PrefixSupplier.dated(formatter));
    }

    public PrefixSupplierBuilder dated(String pattern) {
        return dated(DateTimeFormatter.ofPattern(pattern));
    }

    public PrefixSupplierBuilder loggerName() {
        return append(PrefixSupplier.loggerName());
    }

    public PrefixSupplierBuilder loggerAndRecordName() {
        return append(PrefixSupplier.loggerAndRecordName());
    }

    public PrefixSupplierBuilder loggerLevel() {
        return append(PrefixSupplier.loggerLevel());
    }

    public PrefixSupplierBuilder aligned() {
        if (last != null) {
            last = last.aligned();
        }
        return this;
    }

    public PrefixSupplierBuilder aligned(@NotNull PrefixedRender.AlignedSupplier.AlignType type) {
        if (last != null) {
            last = last.aligned(type);
        }
        return this;
    }

    /**
     * @since 3.0-dev-21
     */
    public PrefixSupplierBuilder threadName() {
        return append(PrefixSupplier.threadName());
    }

    public PrefixSupplier complete() {
        if (shouldPlusLast()) {
            current = current.plus(last);
            last = null;
        }
        return current;
    }
}
