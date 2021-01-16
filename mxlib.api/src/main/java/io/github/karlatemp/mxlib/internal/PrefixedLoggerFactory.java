/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-api.main/PrefixedLoggerFactory.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.internal;

import io.github.karlatemp.mxlib.logger.MLogger;
import io.github.karlatemp.mxlib.logger.MLoggerFactory;
import org.jetbrains.annotations.NotNull;

public class PrefixedLoggerFactory implements MLoggerFactory {
    private final String prefix;
    private final MLoggerFactory parent;

    public PrefixedLoggerFactory(MLoggerFactory parent, String prefix) {
        this.parent = parent;
        this.prefix = prefix;
    }

    @Override
    public @NotNull MLogger getLogger(String name) {
        return parent.getLogger(prefix + name);
    }

    @Override
    public @NotNull MLoggerFactory withPrefix(String prefix) {
        if (prefix == null || prefix.isEmpty()) return this;
        return new PrefixedLoggerFactory(parent, prefix + this.prefix);
    }
}
