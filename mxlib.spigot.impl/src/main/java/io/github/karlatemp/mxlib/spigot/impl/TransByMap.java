/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-spigot-impl.main/TransByMap.java
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

import java.util.Map;

@SuppressWarnings("rawtypes")
public class TransByMap extends AbstractTranslator {
    private final Map map;

    public TransByMap(Map map) {
        this.map = map;
    }

    @Override
    protected @Nullable FormatTemplate findTranslateTemplate(@NotNull String key) {
        Object o = map.get(key);
        if (o != null) {
            return SimpleFormatter.INSTANCE.parse(o.toString());
        }
        return null;
    }
}
