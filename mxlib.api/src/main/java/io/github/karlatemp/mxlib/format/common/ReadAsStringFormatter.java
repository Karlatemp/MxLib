/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-api.main/ReadAsStringFormatter.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.format.common;

import io.github.karlatemp.mxlib.format.FormatTemplate;
import io.github.karlatemp.mxlib.format.Formatter;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.Reader;

public abstract class ReadAsStringFormatter implements Formatter {
    @Override
    public @NotNull FormatTemplate parse(@NotNull Reader template) throws IOException {
        StringBuilder builder = new StringBuilder(1024);
        char[] buffer = new char[1024];
        while (true) {
            int size = template.read(buffer);
            if (size == -1) break;
            builder.append(buffer, 0, size);
        }
        return parse(builder.toString());
    }
}
