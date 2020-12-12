/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: MessageFactory.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

package io.github.karlatemp.mxlib.logger;

import io.github.karlatemp.mxlib.utils.StringBuilderFormattable;

import java.lang.management.LockInfo;
import java.lang.management.ThreadInfo;

/**
 * 日志信息格式化工厂
 */
public interface MessageFactory {
    StringBuilderFormattable getStackTraceElementMessage(StackTraceElement stack);

    StringBuilderFormattable getStackTraceElementMessage$return(StackTraceElement stack, String file, String version);

    StringBuilderFormattable dump(ThreadInfo inf, int frames);

    StringBuilderFormattable CIRCULAR_REFERENCE(Throwable throwable);

    StringBuilderFormattable dump(Throwable thr, boolean hasFrames);

}
