/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-api.main/StringUtils.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.utils;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {
    @Contract(pure = true)
    public static @NotNull String removePrefix(
            @NotNull String string,
            @NotNull String prefix
    ) {
        if (string.startsWith(prefix)) {
            return string.substring(prefix.length());
        }
        return string;
    }

    @Contract(pure = true)
    public static @NotNull String substringBefore(
            @NotNull String string,
            @NotNull String dep,
            @Nullable String missing
    ) {
        if (missing == null) missing = string;
        int index = string.indexOf(dep);
        if (index != -1) {
            return string.substring(0, index);
        }
        return missing;
    }

    @Contract(pure = true)
    public static @NotNull String substringAfter(
            @NotNull String string,
            @NotNull String dep,
            @Nullable String missing
    ) {
        if (missing == null) missing = string;
        int index = string.indexOf(dep);
        if (index != -1) {
            return string.substring(index + dep.length());
        }
        return missing;
    }

    private static final Pattern
            DROP_CSI_PATTERN = Pattern.compile("\\u001b\\[([\\u0030-\\u003F])*?([\\u0020-\\u002F])*?[\\u0040-\\u007E]"),
            DROP_ANSI_PATTERN = Pattern.compile("\\u001b[\\u0040–\\u005F]"),
            UNICODE = Pattern.compile("\\\\u[0-9A-Fa-f]{4}");

    @Contract(pure = true)
    public static @NotNull String dropAnsi(@NotNull String message) {
        return DROP_ANSI_PATTERN.matcher(
                DROP_CSI_PATTERN.matcher(message).replaceAll("")
        ).replaceAll("");
    }

    /**
     * @since 3.0-dev-18
     */
    public static String parseUnicode(String value) {
        Matcher matcher = UNICODE.matcher(value);
        if (matcher.find()) {
            StringBuilder sb = new StringBuilder();
            int latestEnd = 0;
            do {
                sb.append(value, latestEnd, matcher.start());
                int result = Integer.parseInt(value.substring(matcher.start() + 2, matcher.end()), 16);
                sb.append((char) result);
                latestEnd = matcher.end();
            } while (matcher.find());
            return sb.toString();
        }
        return value;
    }

    public static class BkColors {
        @SuppressWarnings("RegExpRedundantEscape")
        private static final Pattern pt = Pattern.compile(
                "\\u001b\\[[0-9\\;]*m"
        );
        private static final String[] mapping = new String[125];


        public static String replaceToBkMessage(String message) {
            StringBuilder builder = new StringBuilder(message.length());
            Matcher matcher = pt.matcher(message);
            int pos = 0;
            while (matcher.find()) {
                int start = matcher.start();
                if (pos < start) {
                    builder.append(message, pos, start);
                }
                pos = matcher.end();
                switch (matcher.group()) {
                    case _1:
                        builder.append("§1");
                        break;
                    case _2:
                        builder.append("§2");
                        break;
                    case _3:
                        builder.append("§3");
                        break;
                    case _4:
                        builder.append("§4");
                        break;
                    case _5:
                        builder.append("§5");
                        break;
                    case _6:
                        builder.append("§6");
                        break;
                    case _7:
                        builder.append("§7");
                        break;
                    case _8:
                        builder.append("§8");
                        break;
                    case _9:
                        builder.append("§9");
                        break;
                    case _0:
                        builder.append("§0");
                        break;
                    case _A:
                        builder.append("§a");
                        break;
                    case _B:
                        builder.append("§b");
                        break;
                    case _C:
                        builder.append("§c");
                        break;
                    case _D:
                        builder.append("§d");
                        break;
                    case _E:
                        builder.append("§e");
                        break;
                    case _F:
                        builder.append("§f");
                        break;
                    case RESET:
                        builder.append("§r");
                        break;
                    default:
                        builder.append(matcher.group());
                        break;
                }
            }
            if (pos < message.length()) {
                builder.append(message, pos, message.length());
            }
            return builder.toString();
        }

        /**
         * @since 3.0-dev-18
         */
        public static String replaceToAnsiMsg(String msg) {
            StringBuilder sb = new StringBuilder(msg.length());
            int current = 0, ocurrent = 0, ln1 = msg.length() - 1;
            while (true) {
                int next = msg.indexOf('§', ocurrent);
                if (next == -1) break;
                if (next >= ln1) break;
                char c = msg.charAt(next + 1);
                ocurrent = next + 1;
                if (c >= mapping.length) continue;
                String replacement = mapping[c];
                if (replacement == null) continue;
                sb.append(msg, current, next);
                sb.append(replacement);
                current = next + 2;
            }
            if (current < msg.length()) {
                sb.append(msg, current, msg.length());
            }
            return sb.toString();
        }

        public static final String RESET = "\033[m",
                _1 = "\033[0;34;22m",
                _2 = "\033[0;32;22m",
                _3 = "\033[0;36;22m",
                _4 = "\033[0;31;22m",
                _5 = "\033[0;35;22m",
                _6 = "\033[0;33;22m",
                _7 = "\033[0;37;22m",
                _8 = "\033[0;30;1m",
                _9 = "\033[0;34;1m",
                _0 = "\033[0;30;22m",
                _A = "\033[0;32;1m",
                _B = "\033[0;36;1m",
                _C = "\033[0;31;1m",
                _D = "\033[0;35;1m",
                _E = "\033[0;33;1m",
                _F = "\033[0;37;1m";

        static {
            mapping['1'] = _1;
            mapping['2'] = _2;
            mapping['3'] = _3;
            mapping['4'] = _4;
            mapping['5'] = _5;
            mapping['6'] = _6;
            mapping['7'] = _7;
            mapping['8'] = _8;
            mapping['9'] = _9;
            mapping['0'] = _0;
            mapping['a'] = mapping['A'] = _A;
            mapping['b'] = mapping['B'] = _B;
            mapping['c'] = mapping['C'] = _C;
            mapping['d'] = mapping['D'] = _D;
            mapping['e'] = mapping['E'] = _E;
            mapping['f'] = mapping['F'] = _F;
            mapping['r'] = mapping['R'] = RESET;
        }
    }
}
