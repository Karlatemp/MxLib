/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-spigot-impl.main/TransByMapping.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.spigot.impl;

import io.github.karlatemp.mxlib.format.FormatTemplate;
import io.github.karlatemp.mxlib.format.common.SimpleFormatter;
import io.github.karlatemp.mxlib.translate.AbstractTranslator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class TransByMapping extends AbstractTranslator {
    private final Function<String, String> mapper;

    public TransByMapping(Function<String, String> mapper) {
        this.mapper = mapper;
    }

    @Override
    protected @Nullable FormatTemplate findTranslateTemplate(@NotNull String key) {
        String o = mapper.apply(key);
        if (o != null) {
            return SimpleFormatter.INSTANCE.parse(o);
        }
        return null;
    }
}
