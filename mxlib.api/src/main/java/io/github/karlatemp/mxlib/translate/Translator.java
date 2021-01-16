/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-api.main/Translator.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.translate;

import io.github.karlatemp.mxlib.format.FormatTemplate;
import io.github.karlatemp.mxlib.utils.StringBuilderFormattable;
import org.jetbrains.annotations.NotNull;

public interface Translator {
    @NotNull FormatTemplate getTranslateTemplate(@NotNull String key);

    @NotNull StringBuilderFormattable format(@NotNull String key);

    @NotNull StringBuilderFormattable format(@NotNull String key, Object... arguments);
}
