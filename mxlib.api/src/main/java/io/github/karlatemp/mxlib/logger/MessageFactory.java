/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-api.main/MessageFactory.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.logger;

import io.github.karlatemp.mxlib.utils.StringBuilderFormattable;

import java.lang.management.ThreadInfo;

/**
 * The message factory for {@link io.github.karlatemp.mxlib.logger.renders.SimpleRender}
 */
public interface MessageFactory {
    StringBuilderFormattable getStackTraceElementMessage(StackTraceElement stack);

    StringBuilderFormattable getStackTraceElementMessage$return(StackTraceElement stack, String file, String version);

    StringBuilderFormattable dump(ThreadInfo inf, int frames);

    StringBuilderFormattable CIRCULAR_REFERENCE(Throwable throwable);

    StringBuilderFormattable dump(Throwable thr, boolean hasFrames);

}
