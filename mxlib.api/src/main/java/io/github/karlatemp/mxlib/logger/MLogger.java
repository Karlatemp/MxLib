/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-api.main/MLogger.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.logger;

import io.github.karlatemp.mxlib.utils.StringBuilderFormattable;

import java.util.logging.Level;
import java.util.logging.LogRecord;

public interface MLogger {
    String getName();

    boolean isEnabled(MMarket marker);

    boolean isEnabled(Level level);

    boolean isEnabled(MMarket marker, Level level);

    boolean isDebugEnabled();

    boolean isDebugEnabled(MMarket marker);

    boolean isVerboseEnabled();

    boolean isVerboseEnabled(MMarket marker);

    boolean isInfoEnabled();

    boolean isInfoEnabled(MMarket marker);

    boolean isWarnEnabled();

    boolean isWarnEnabled(MMarket marker);

    boolean isErrorEnabled();

    boolean isErrorEnabled(MMarket marker);

    //////////////////////////////////////////////////////////////

    void log(MMarket marker, LogRecord logRecord);

    void log(MMarket marker, Level level, StringBuilderFormattable message);

    void info(MMarket marker, StringBuilderFormattable msg);

    void warn(MMarket marker, StringBuilderFormattable msg);

    void error(MMarket marker, StringBuilderFormattable msg);

    void debug(MMarket marker, StringBuilderFormattable msg);

    void verbose(MMarket marker, StringBuilderFormattable msg);

    //////////////////////////////////////////////////////////////

    void log(MMarket marker, Level level, StringBuilderFormattable message, Throwable throwable);

    void info(MMarket marker, StringBuilderFormattable msg, Throwable throwable);

    void warn(MMarket marker, StringBuilderFormattable msg, Throwable throwable);

    void error(MMarket marker, StringBuilderFormattable msg, Throwable throwable);

    void debug(MMarket marker, StringBuilderFormattable msg, Throwable throwable);

    void verbose(MMarket marker, StringBuilderFormattable msg, Throwable throwable);

    //////////////////////////////////////////////////////////////

    void log(LogRecord logRecord);

    void log(Level level, StringBuilderFormattable message);

    void info(StringBuilderFormattable msg);

    void warn(StringBuilderFormattable msg);

    void error(StringBuilderFormattable msg);

    void debug(StringBuilderFormattable msg);

    void verbose(StringBuilderFormattable msg);

    void log(Level level, StringBuilderFormattable message, Throwable throwable);

    void info(StringBuilderFormattable msg, Throwable throwable);

    void warn(StringBuilderFormattable msg, Throwable throwable);

    void error(StringBuilderFormattable msg, Throwable throwable);

    void debug(StringBuilderFormattable msg, Throwable throwable);

    void verbose(StringBuilderFormattable msg, Throwable throwable);

    //////////////////////////////////////////////////////////////

    void info(Throwable throwable);

    void warn(Throwable throwable);

    void error(Throwable throwable);

    void debug(Throwable throwable);

    void verbose(Throwable throwable);

    //////////////////////////////////////////////////////////////

    void log(MMarket marker, Level level, String message, Throwable throwable);

    void info(MMarket marker, String msg, Throwable throwable);

    void warn(MMarket marker, String msg, Throwable throwable);

    void error(MMarket marker, String msg, Throwable throwable);

    void debug(MMarket marker, String msg, Throwable throwable);

    void verbose(MMarket marker, String msg, Throwable throwable);

    //////////////////////////////////////////////////////////////

    void info(MMarket marker, Throwable throwable);

    void warn(MMarket marker, Throwable throwable);

    void error(MMarket marker, Throwable throwable);

    void debug(MMarket marker, Throwable throwable);

    void verbose(MMarket marker, Throwable throwable);

    //////////////////////////////////////////////////////////////

    void log(Level level, String message);

    void info(String msg);

    void warn(String msg);

    void error(String msg);

    void debug(String msg);

    void verbose(String msg);

    //////////////////////////////////////////////////////////////

    void info(MMarket marker, String msg);

    void warn(MMarket marker, String msg);

    void error(MMarket marker, String msg);

    void debug(MMarket marker, String msg);

    void verbose(MMarket marker, String msg);

    //////////////////////////////////////////////////////////////


    void log(Level level, String message, Throwable throwable);

    void info(String msg, Throwable throwable);

    void warn(String msg, Throwable throwable);

    void error(String msg, Throwable throwable);

    void debug(String msg, Throwable throwable);

    void verbose(String msg, Throwable throwable);

}
