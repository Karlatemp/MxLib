/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-api.main/PrefixSupplierParser.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.internal;

import io.github.karlatemp.mxlib.logger.renders.PrefixSupplierBuilder;
import io.github.karlatemp.mxlib.logger.renders.PrefixedRender;
import io.github.karlatemp.mxlib.utils.StringUtils;

import java.util.Arrays;
import java.util.Scanner;

public class PrefixSupplierParser {
    static class Options {
        boolean BkColor;
        boolean unicode;
    }

    public static PrefixedRender.PrefixSupplier parse(Scanner scanner) {
        return parse(scanner, new Options());
    }

    static PrefixedRender.PrefixSupplier parse(Scanner scanner, Options options) {
        PrefixSupplierBuilder builder = new PrefixSupplierBuilder();
        top:
        while (scanner.hasNextLine()) {
            String nextline = scanner.nextLine();
            if (nextline.trim().isEmpty()) continue;
            if (nextline.charAt(0) == '#') {
                int split = nextline.indexOf(' ');
                String cmd, opt;
                if (split == -1) {
                    cmd = nextline;
                    opt = null;
                } else {
                    cmd = nextline.substring(0, split);
                    opt = nextline.substring(split + 1);
                }
                switch (cmd) {
                    case "##":
                        break;
                    case "#enable": {
                        //noinspection ConstantConditions
                        switch (opt) {
                            case "bk-color":
                                options.BkColor = true;
                                break;
                            case "unicode":
                                options.unicode = true;
                                break;
                        }
                        break;
                    }
                    case "#date": {
                        builder.dated(opt);
                        break;
                    }
                    case "#logger-name": {
                        builder.loggerName();
                        break;
                    }
                    case "#top":
                    case "#end":
                        break top;
                    case "#logger-and-record-name":
                        builder.loggerAndRecordName();
                        break;
                    case "#logger-level":
                        builder.loggerLevel();
                        break;
                    case "#space": {
                        //noinspection ConstantConditions
                        char[] buf = new char[Integer.parseInt(opt)];
                        Arrays.fill(buf, ' ');
                        builder.append(new String(buf));
                        break;
                    }
                    case "#aligned": {
                        PrefixedRender.PrefixSupplier parse = parse(scanner, options);
                        if (opt == null) builder.append(parse.aligned());
                        else builder.append(parse.aligned(PrefixedRender.AlignedSupplier.AlignType.valueOf(opt)));
                        break;
                    }
                    case "#thread":
                    case "#thread-name":
                    case "#current-thread":
                    case "#current-thread-name": {
                        builder.threadName();
                        break;
                    }
                }
            } else {
                if (options.BkColor) nextline = StringUtils.BkColors.replaceToAnsiMsg(nextline);
                if (options.unicode) nextline = StringUtils.parseUnicode(nextline);
                builder.append(nextline);
            }
        }
        return builder.complete();
    }
}
