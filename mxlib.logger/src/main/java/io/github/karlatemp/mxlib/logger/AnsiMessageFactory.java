/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-logger.main/AnsiMessageFactory.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.logger;

import io.github.karlatemp.mxlib.annotations.injector.Inject;
import io.github.karlatemp.mxlib.logger.utils.StackTraceElementUtils;
import io.github.karlatemp.mxlib.utils.ClassLocator;
import io.github.karlatemp.mxlib.utils.StringBuilderFormattable;
import io.github.karlatemp.mxlib.utils.StringUtils;

import java.lang.management.LockInfo;

import static io.github.karlatemp.mxlib.utils.StringBuilderFormattable.by;

/**
 * MessageFactory with Ansi colors
 */
public class AnsiMessageFactory extends RawMessageFactory {
    public static final StringBuilderFormattable
            _0 = by(StringUtils.BkColors._0),
            _1 = by(StringUtils.BkColors._1),
            _2 = by(StringUtils.BkColors._2),
            _3 = by(StringUtils.BkColors._3),
            _4 = by(StringUtils.BkColors._4),
            _5 = by(StringUtils.BkColors._5),
            _6 = by(StringUtils.BkColors._6),
            _7 = by(StringUtils.BkColors._7),
            _8 = by(StringUtils.BkColors._8),
            _9 = by(StringUtils.BkColors._9),
            _A = by(StringUtils.BkColors._A),
            _B = by(StringUtils.BkColors._B),
            _C = by(StringUtils.BkColors._C),
            _D = by(StringUtils.BkColors._D),
            _E = by(StringUtils.BkColors._E),
            _F = by(StringUtils.BkColors._F),
            RESET = by(StringUtils.BkColors.RESET);

    @Inject
    public AnsiMessageFactory() {
    }

    public AnsiMessageFactory(ClassLocator locator) {
        super(locator);
    }

    protected void plusStackTraceElement(StringBuilderFormattable link, StackTraceElement s) {
        link.plusMsg("\t").plusMsg(_6).plusMsg("at ").plusMsg(getStackTraceElementMessage(s));
    }

    @Override
    public StringBuilderFormattable getStackTraceElementMessage$return(
            StackTraceElement stack,
            String file,
            String version
    ) {
        StringBuilderFormattable link = new StringBuilderFormattable.Link();
        {
            boolean append = false;
            String classLoaderName = StackTraceElementUtils.GET_CLASSLOADER_NAME.apply(stack);
            if (classLoaderName != null && !StackTraceElementUtils.DROP_CLASS_LOADER_NAME.test(stack) && !classLoaderName.isEmpty()) {
                link.plusMsg(_2).plusMsg(classLoaderName).plusMsg(RESET).plusMsg("/");
                append = true;
            }
            String moduleName = StackTraceElementUtils.GET_MODULE_NAME.apply(stack);
            if (moduleName != null && !moduleName.isEmpty()) {
                link.plusMsg(_1).plusMsg(moduleName);
                append = true;
                String moduleVersion = StackTraceElementUtils.GET_MODULE_VERSION.apply(stack);
                if (moduleVersion != null && !StackTraceElementUtils.DROP_MODULE_VERSION.test(stack) && !moduleVersion.isEmpty()) {
                    link.plusMsg(RESET).plusMsg("@").plusMsg(_6).plusMsg(moduleVersion);
                }
            }
            if (append) link.plusMsg(RESET).plusMsg("/");
            link.plusMsg(_C).plusMsg(stack.getClassName());
            link.plusMsg(RESET).plusMsg(".");
            link.plusMsg(_E).plusMsg(stack.getMethodName());
            link.plusMsg(RESET).plusMsg("(");
            if (stack.isNativeMethod()) {
                link.plusMsg(_D).plusMsg("Native Method");
            } else {
                int linenum = stack.getLineNumber();
                String filename = stack.getFileName();
                if (filename != null) {
                    link.plusMsg(_2).plusMsg(filename);
                    if (linenum >= 0) {
                        link.plusMsg(RESET).plusMsg(":").plusMsg(_6).plusMsg(linenum);
                    }
                } else {
                    link.plusMsg(_7).plusMsg("Unknown Source");
                }
            }
            link.plusMsg(RESET).plusMsg(")");
        }

        link.plusMsg(" [")
                .plusMsg(_B).plusMsg(file == null ? "?" : file)
                .plusMsg(_6).plusMsg(":")
                .plusMsg(_D).plusMsg(version == null ? "?" : version)
                .plusMsg(RESET).plusMsg("]");

        return link;
    }

    protected void plusLockInfo(StringBuilderFormattable link, LockInfo lockInfo) {
        link.plusMsg(_C).plusMsg(lockInfo.getClassName())
                .plusMsg(RESET).plusMsg("@")
                .plusMsg(_B).plusMsg(Integer.toHexString(lockInfo.getIdentityHashCode()))
        ;
    }
}
