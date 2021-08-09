/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-api.main/NopLogger.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.logger;

import io.github.karlatemp.mxlib.utils.StringBuilderFormattable;

import java.util.logging.Level;
import java.util.logging.LogRecord;

public class NopLogger implements MLogger {
    private final String name;

    public NopLogger(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isEnabled(MMarket marker) {
        return false;
    }

    @Override
    public boolean isEnabled(Level level) {
        return false;
    }

    @Override
    public boolean isEnabled(MMarket marker, Level level) {
        return false;
    }

    @Override
    public boolean isDebugEnabled() {
        return false;
    }

    @Override
    public boolean isDebugEnabled(MMarket marker) {
        return false;
    }

    @Override
    public boolean isVerboseEnabled() {
        return false;
    }

    @Override
    public boolean isVerboseEnabled(MMarket marker) {
        return false;
    }

    @Override
    public boolean isInfoEnabled() {
        return false;
    }

    @Override
    public boolean isInfoEnabled(MMarket marker) {
        return false;
    }

    @Override
    public boolean isWarnEnabled() {
        return false;
    }

    @Override
    public boolean isWarnEnabled(MMarket marker) {
        return false;
    }

    @Override
    public boolean isErrorEnabled() {
        return false;
    }

    @Override
    public boolean isErrorEnabled(MMarket marker) {
        return false;
    }

    @Override
    public void log(MMarket marker, LogRecord logRecord) {

    }

    @Override
    public void log(MMarket marker, Level level, StringBuilderFormattable message) {

    }

    @Override
    public void info(MMarket marker, StringBuilderFormattable msg) {

    }

    @Override
    public void warn(MMarket marker, StringBuilderFormattable msg) {

    }

    @Override
    public void error(MMarket marker, StringBuilderFormattable msg) {

    }

    @Override
    public void debug(MMarket marker, StringBuilderFormattable msg) {

    }

    @Override
    public void verbose(MMarket marker, StringBuilderFormattable msg) {

    }

    @Override
    public void log(MMarket marker, Level level, StringBuilderFormattable message, Throwable throwable) {

    }

    @Override
    public void info(MMarket marker, StringBuilderFormattable msg, Throwable throwable) {

    }

    @Override
    public void warn(MMarket marker, StringBuilderFormattable msg, Throwable throwable) {

    }

    @Override
    public void error(MMarket marker, StringBuilderFormattable msg, Throwable throwable) {

    }

    @Override
    public void debug(MMarket marker, StringBuilderFormattable msg, Throwable throwable) {

    }

    @Override
    public void verbose(MMarket marker, StringBuilderFormattable msg, Throwable throwable) {

    }

    @Override
    public void log(LogRecord logRecord) {

    }

    @Override
    public void log(Level level, StringBuilderFormattable message) {

    }

    @Override
    public void info(StringBuilderFormattable msg) {

    }

    @Override
    public void warn(StringBuilderFormattable msg) {

    }

    @Override
    public void error(StringBuilderFormattable msg) {

    }

    @Override
    public void debug(StringBuilderFormattable msg) {

    }

    @Override
    public void verbose(StringBuilderFormattable msg) {

    }

    @Override
    public void log(Level level, StringBuilderFormattable message, Throwable throwable) {

    }

    @Override
    public void info(StringBuilderFormattable msg, Throwable throwable) {

    }

    @Override
    public void warn(StringBuilderFormattable msg, Throwable throwable) {

    }

    @Override
    public void error(StringBuilderFormattable msg, Throwable throwable) {

    }

    @Override
    public void debug(StringBuilderFormattable msg, Throwable throwable) {

    }

    @Override
    public void verbose(StringBuilderFormattable msg, Throwable throwable) {

    }

    @Override
    public void info(Throwable throwable) {

    }

    @Override
    public void warn(Throwable throwable) {

    }

    @Override
    public void error(Throwable throwable) {

    }

    @Override
    public void debug(Throwable throwable) {

    }

    @Override
    public void verbose(Throwable throwable) {

    }

    @Override
    public void log(MMarket marker, Level level, String message, Throwable throwable) {

    }

    @Override
    public void info(MMarket marker, String msg, Throwable throwable) {

    }

    @Override
    public void warn(MMarket marker, String msg, Throwable throwable) {

    }

    @Override
    public void error(MMarket marker, String msg, Throwable throwable) {

    }

    @Override
    public void debug(MMarket marker, String msg, Throwable throwable) {

    }

    @Override
    public void verbose(MMarket marker, String msg, Throwable throwable) {

    }

    @Override
    public void info(MMarket marker, Throwable throwable) {

    }

    @Override
    public void warn(MMarket marker, Throwable throwable) {

    }

    @Override
    public void error(MMarket marker, Throwable throwable) {

    }

    @Override
    public void debug(MMarket marker, Throwable throwable) {

    }

    @Override
    public void verbose(MMarket marker, Throwable throwable) {

    }

    @Override
    public void log(Level level, String message) {

    }

    @Override
    public void info(String msg) {

    }

    @Override
    public void warn(String msg) {

    }

    @Override
    public void error(String msg) {

    }

    @Override
    public void debug(String msg) {

    }

    @Override
    public void verbose(String msg) {

    }

    @Override
    public void info(MMarket marker, String msg) {

    }

    @Override
    public void warn(MMarket marker, String msg) {

    }

    @Override
    public void error(MMarket marker, String msg) {

    }

    @Override
    public void debug(MMarket marker, String msg) {

    }

    @Override
    public void verbose(MMarket marker, String msg) {

    }

    @Override
    public void log(Level level, String message, Throwable throwable) {

    }

    @Override
    public void info(String msg, Throwable throwable) {

    }

    @Override
    public void warn(String msg, Throwable throwable) {

    }

    @Override
    public void error(String msg, Throwable throwable) {

    }

    @Override
    public void debug(String msg, Throwable throwable) {

    }

    @Override
    public void verbose(String msg, Throwable throwable) {

    }
}
