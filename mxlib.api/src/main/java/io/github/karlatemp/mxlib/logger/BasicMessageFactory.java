/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-api.main/BasicMessageFactory.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.logger;

import io.github.karlatemp.mxlib.utils.Lazy;
import io.github.karlatemp.mxlib.utils.StringBuilderFormattable;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.management.ThreadInfo;

public class BasicMessageFactory implements MessageFactory {
    @Override
    public StringBuilderFormattable getStackTraceElementMessage(StackTraceElement stack) {
        return StringBuilderFormattable.byLazyToString(stack);
    }

    @Override
    public StringBuilderFormattable getStackTraceElementMessage$return(StackTraceElement stack, String file, String version) {
        return StringBuilderFormattable.byLazyToString(stack);
    }

    @Override
    public StringBuilderFormattable dump(ThreadInfo inf, int frames) {
        return StringBuilderFormattable.byLazyToString(Lazy.publication(inf::toString));
    }

    @Override
    public StringBuilderFormattable CIRCULAR_REFERENCE(Throwable throwable) {
        StringBuilderFormattable link = StringBuilderFormattable.by("\t[CIRCULAR REFERENCE:").asLink();
        link.plusMsg(throwable);
        return link.plusMsg("]");
    }

    @Override
    public StringBuilderFormattable dump(Throwable thr, boolean hasFrames) {
        return StringBuilderFormattable.byLazyToString(Lazy.lazy(() -> {
            StringWriter writer = new StringWriter();
            PrintWriter pw;
            thr.printStackTrace(pw = new PrintWriter(writer));
            pw.flush();
            return writer;
        }));
    }
}
