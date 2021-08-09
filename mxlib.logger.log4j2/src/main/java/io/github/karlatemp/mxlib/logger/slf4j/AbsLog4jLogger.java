/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-logger-log4j2.main/AbsLog4jLogger.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.logger.slf4j;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.MessageFactory;
import org.apache.logging.log4j.spi.AbstractLogger;

abstract class AbsLog4jLogger extends AbstractLogger {
    AbsLog4jLogger() {
    }

    AbsLog4jLogger(String name) {
        super(name);
    }

    AbsLog4jLogger(String name, MessageFactory mf) {
        super(name, mf);
    }

    private static Message wrp(Object msg) {
        return new MLog4jMessageFactory.WrpMsg(msg);
    }

    @Override
    protected void logMessage(String fqcn, Level level, Marker marker, String message) {
        logMessage(fqcn, level, marker, wrp(message), null);
    }

    @Override
    protected void logMessage(String fqcn, Level level, Marker marker, Object message, Throwable throwable) {
        logMessage(fqcn, level, marker, wrp(message), throwable);
    }

    @Override
    public boolean isEnabled(Level level, Marker marker, Message message, Throwable t) {
        return isEnabled(level, marker, (Object) message, t);
    }

    @Override
    public boolean isEnabled(Level level, Marker marker, CharSequence message, Throwable t) {
        return isEnabled(level, marker, (Object) message, t);
    }

    @Override
    public boolean isEnabled(Level level, Marker marker, String message, Throwable t) {
        return isEnabled(level, marker, (Object) message, t);
    }

    @Override
    public boolean isEnabled(Level level, Marker marker, String message) {
        return isEnabled(level, marker, message, (Throwable) null);
    }

    @Override
    public boolean isEnabled(Level level, Marker marker, String message, Object... params) {
        return isEnabled(level, marker, message);
    }

    @Override
    public boolean isEnabled(Level level, Marker marker, String message, Object p0) {
        return isEnabled(level, marker, message);
    }

    @Override
    public boolean isEnabled(Level level, Marker marker, String message, Object p0, Object p1) {
        return isEnabled(level, marker, message);
    }

    @Override
    public boolean isEnabled(Level level, Marker marker, String message, Object p0, Object p1, Object p2) {
        return isEnabled(level, marker, message);
    }

    @Override
    public boolean isEnabled(Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3) {
        return isEnabled(level, marker, message);
    }

    @Override
    public boolean isEnabled(Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4) {
        return isEnabled(level, marker, message);
    }

    @Override
    public boolean isEnabled(Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
        return isEnabled(level, marker, message);
    }

    @Override
    public boolean isEnabled(Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
        return isEnabled(level, marker, message);
    }

    @Override
    public boolean isEnabled(Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) {
        return isEnabled(level, marker, message);
    }

    @Override
    public boolean isEnabled(Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {
        return isEnabled(level, marker, message);
    }

    @Override
    public boolean isEnabled(Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
        return isEnabled(level, marker, message);
    }

}
