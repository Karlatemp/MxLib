/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-api.main/AbstractLogger.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.logger;

import io.github.karlatemp.mxlib.utils.StringBuilderFormattable;

import java.util.logging.Level;
import java.util.logging.LogRecord;

public abstract class AbstractLogger implements MLogger {

    public boolean isEnabled(MMarket market, Level level) {
        return isEnabled(market) && isEnabled(level);
    }

    @Override
    public boolean isDebugEnabled() {
        return isDebugEnabled(null);
    }

    @Override
    public boolean isWarnEnabled() {
        return isWarnEnabled(null);
    }

    private static StringBuilderFormattable join(String message, Throwable throwable) {
        return join(message == null ? null : StringBuilderFormattable.by(message), throwable);
    }

    private static StringBuilderFormattable join(StringBuilderFormattable message, Throwable thr) {
        if (message == null && thr == null) return null;
        if (message == null) return new StringBuilderFormattable.Link().plusMsg(thr);
        if (thr == null) return message;
        return message.plusMsg(thr);
    }

    @Override
    public void log(MMarket market, Level level, StringBuilderFormattable message, Throwable throwable) {
        log(market, level, join(message, throwable));
    }

    @Override
    public void info(MMarket market, StringBuilderFormattable msg, Throwable throwable) {
        info(market, join(msg, throwable));
    }

    @Override
    public void warn(MMarket market, StringBuilderFormattable msg, Throwable throwable) {
        warn(market, join(msg, throwable));
    }

    @Override
    public void error(MMarket market, StringBuilderFormattable msg, Throwable throwable) {
        error(market, join(msg, throwable));
    }

    @Override
    public void debug(MMarket market, StringBuilderFormattable msg, Throwable throwable) {
        debug(market, join(msg, throwable));
    }

    @Override
    public void verbose(MMarket market, StringBuilderFormattable msg, Throwable throwable) {
        verbose(market, join(msg, throwable));
    }

    @Override
    public void log(LogRecord logRecord) {
        log(null, logRecord);
    }


    @Override
    public void info(StringBuilderFormattable msg) {
        info(null, msg);
    }

    @Override
    public void warn(StringBuilderFormattable msg) {
        warn(null, msg);
    }

    @Override
    public void error(StringBuilderFormattable msg) {
        error(null, msg);
    }

    @Override
    public void debug(StringBuilderFormattable msg) {
        debug(null, msg);
    }

    @Override
    public void verbose(StringBuilderFormattable msg) {
        verbose(null, msg);
    }

    @Override
    public void log(Level level, StringBuilderFormattable message, Throwable throwable) {
        log(level, join(message, throwable));
    }

    @Override
    public void info(StringBuilderFormattable msg, Throwable throwable) {
        info(join(msg, throwable));
    }

    @Override
    public void warn(StringBuilderFormattable msg, Throwable throwable) {
        warn(join(msg, throwable));
    }

    @Override
    public void error(StringBuilderFormattable msg, Throwable throwable) {
        error(join(msg, throwable));
    }

    @Override
    public void debug(StringBuilderFormattable msg, Throwable throwable) {
        debug(join(msg, throwable));
    }

    @Override
    public void verbose(StringBuilderFormattable msg, Throwable throwable) {
        verbose(join(msg, throwable));
    }

    @Override
    public void log(MMarket market, Level level, String message, Throwable throwable) {
        log(market, level, join(message, throwable));
    }

    @Override
    public void info(MMarket market, String msg, Throwable throwable) {
        info(market, join(msg, throwable));
    }

    @Override
    public void warn(MMarket market, String msg, Throwable throwable) {
        warn(market, join(msg, throwable));

    }

    @Override
    public void error(MMarket market, String msg, Throwable throwable) {
        error(market, join(msg, throwable));

    }

    @Override
    public void debug(MMarket market, String msg, Throwable throwable) {
        debug(market, join(msg, throwable));

    }

    @Override
    public void verbose(MMarket market, String msg, Throwable throwable) {
        verbose(market, join(msg, throwable));
    }

    @Override
    public void log(Level level, String message) {
        log(level, StringBuilderFormattable.by(message));
    }

    @Override
    public void info(String msg) {
        info(null, msg);
    }

    @Override
    public void warn(String msg) {
        warn(null, msg);
    }

    @Override
    public void error(String msg) {
        error(null, msg);
    }

    @Override
    public void debug(String msg) {
        debug(null, msg);
    }

    @Override
    public void verbose(String msg) {
        verbose(null, msg);
    }

    @Override
    public void log(Level level, String message, Throwable throwable) {
        log(null, level, message, throwable);
    }

    @Override
    public void info(String msg, Throwable throwable) {
        info(null, msg, throwable);
    }

    @Override
    public void warn(String msg, Throwable throwable) {
        warn(null, msg, throwable);
    }

    @Override
    public void error(String msg, Throwable throwable) {
        error(null, msg, throwable);
    }

    @Override
    public void debug(String msg, Throwable throwable) {
        debug(null, msg, throwable);
    }

    @Override
    public void verbose(String msg, Throwable throwable) {
        verbose(null, msg, throwable);
    }

    @Override
    public void info(MMarket market, String msg) {
        info(market, StringBuilderFormattable.by(msg));
    }

    @Override
    public void log(Level level, StringBuilderFormattable message) {
        log(null, level, message);
    }

    @Override
    public void warn(MMarket market, String msg) {
        warn(market, StringBuilderFormattable.by(msg));
    }

    @Override
    public void error(MMarket market, String msg) {
        error(market, StringBuilderFormattable.by(msg));
    }

    @Override
    public void debug(MMarket market, String msg) {
        debug(market, StringBuilderFormattable.by(msg));
    }

    @Override
    public void verbose(MMarket market, String msg) {
        verbose(market, StringBuilderFormattable.by(msg));
    }

    @Override
    public void info(Throwable throwable) {
        info((String) null, throwable);
    }

    @Override
    public void error(Throwable throwable) {
        error((String) null, throwable);
    }

    @Override
    public void warn(Throwable throwable) {
        warn((String) null, throwable);
    }

    @Override
    public void debug(Throwable throwable) {
        debug((String) null, throwable);
    }

    @Override
    public void verbose(Throwable throwable) {
        verbose((String) null, throwable);
    }


    @Override
    public void info(MMarket market, Throwable throwable) {
        info(market, (String) null, throwable);
    }

    @Override
    public void error(MMarket market, Throwable throwable) {
        error(market, (String) null, throwable);
    }

    @Override
    public void warn(MMarket market, Throwable throwable) {
        warn(market, (String) null, throwable);
    }

    @Override
    public void debug(MMarket market, Throwable throwable) {
        debug(market, (String) null, throwable);
    }

    @Override
    public void verbose(MMarket market, Throwable throwable) {
        verbose(market, (String) null, throwable);
    }
}
