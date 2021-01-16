/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-api.main/MLoggerFactory.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.logger;

import io.github.karlatemp.mxlib.internal.PrefixedLoggerFactory;
import org.jetbrains.annotations.NotNull;

public interface MLoggerFactory {
    @NotNull MLogger getLogger(String name);

    default @NotNull MLoggerFactory withPrefix(String prefix) {
        if (prefix == null || prefix.isEmpty()) return this;
        return new PrefixedLoggerFactory(this, prefix);
    }
}
