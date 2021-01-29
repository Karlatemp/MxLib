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

import io.github.karlatemp.mxlib.logger.renders.PrefixedRender.PrefixSupplier;
import org.jetbrains.annotations.NotNull;

import java.time.format.DateTimeFormatter;
import java.util.function.Supplier;

public class PrefixSupplierBuilder {
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

    public PrefixSupplier complete() {
        if (shouldPlusLast()) {
            current = current.plus(last);
            last = null;
        }
        return current;
    }
}
