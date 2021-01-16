/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-api.main/FormatTemplate.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.format;

import io.github.karlatemp.mxlib.utils.StringBuilderFormattable;
import org.jetbrains.annotations.NotNull;

public interface FormatTemplate {
    void formatTo(@NotNull StringBuilder buffer, @NotNull FormatArguments arguments);

    default StringBuilderFormattable format(@NotNull FormatArguments arguments) {
        return builder -> FormatTemplate.this.formatTo(builder, arguments);
    }
}
