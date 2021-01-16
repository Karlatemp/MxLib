/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-api.main/Formatter.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.format;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.Reader;

public interface Formatter {
    @NotNull FormatTemplate parse(@NotNull String template);

    @NotNull FormatTemplate parse(@NotNull Reader template) throws IOException;
}
