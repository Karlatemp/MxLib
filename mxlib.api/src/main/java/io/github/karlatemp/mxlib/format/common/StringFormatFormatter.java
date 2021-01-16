/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-api.main/StringFormatFormatter.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.format.common;

import io.github.karlatemp.mxlib.format.FormatArguments;
import io.github.karlatemp.mxlib.format.FormatTemplate;
import io.github.karlatemp.mxlib.utils.StringBuilderFormattable;
import org.jetbrains.annotations.NotNull;

import java.util.Formatter;

public class StringFormatFormatter extends ReadAsStringFormatter {
    public static final StringFormatFormatter INSTANCE = new StringFormatFormatter();

    @Override
    public @NotNull FormatTemplate parse(@NotNull String template) {
        return new FormatTemplate() {
            @Override
            public void formatTo(@NotNull StringBuilder buffer, @NotNull FormatArguments arguments) {
                 new Formatter(
                        buffer
                ).format(template, toArgs(arguments));
            }

            private Object[] toArgs(FormatArguments arguments) {
                int size = arguments.arguments();
                Object[] args = new Object[size];
                for (int i = 0; i < size; i++) {
                    args[i] = arguments.get(i);
                    if (args[i] instanceof StringBuilderFormattable) {
                        args[i] = ((StringBuilderFormattable) args[i]).lazyToStringBridge();
                    }
                }
                return args;
            }
        };
    }
}
