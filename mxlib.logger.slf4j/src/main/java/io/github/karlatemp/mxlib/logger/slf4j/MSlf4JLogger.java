/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-logger-slf4j.main/MSlf4JLogger.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.logger.slf4j;

import io.github.karlatemp.mxlib.logger.MLogger;
import io.github.karlatemp.mxlib.utils.StringBuilderFormattable;
import org.slf4j.Logger;
import org.slf4j.Marker;

public class MSlf4JLogger implements Logger {
    private final MLogger delegate;
    private final MSlf4jLoggerFactory factory;

    public MSlf4JLogger(MLogger delegate, MSlf4jLoggerFactory factory) {
        this.delegate = delegate;
        this.factory = factory;
    }

    @Override
    public String getName() {
        return delegate.getName();
    }

    @Override
    public boolean isTraceEnabled() {
        return delegate.isDebugEnabled();
    }

    @Override
    public void trace(String msg) {
        delegate.verbose(msg);
    }

    @Override
    public void trace(String format, Object arg) {
        delegate.verbose(
                StringBuilderFormattable.by(() -> String.format(format, arg))
        );
    }

    @Override
    public void trace(String format, Object arg1, Object arg2) {
        delegate.verbose(
                StringBuilderFormattable.by(() -> String.format(format, arg1, arg2))
        );
    }

    @Override
    public void trace(String format, Object... arguments) {
        delegate.verbose(
                StringBuilderFormattable.by(() -> String.format(format, arguments))
        );
    }

    @Override
    public void trace(String msg, Throwable t) {
        delegate.verbose(msg, t);
    }

    @Override
    public boolean isTraceEnabled(Marker marker) {
        return delegate.isDebugEnabled(factory.toMMarket(marker));
    }

    @Override
    public void trace(Marker marker, String msg) {
        delegate.verbose(factory.toMMarket(marker), msg);
    }

    @Override
    public void trace(Marker marker, String format, Object arg) {
        delegate.verbose(
                factory.toMMarket(marker),
                StringBuilderFormattable.by(() -> String.format(format, arg))
        );
    }

    @Override
    public void trace(Marker marker, String format, Object arg1, Object arg2) {
        delegate.verbose(
                factory.toMMarket(marker),
                StringBuilderFormattable.by(() -> String.format(format, arg1, arg2))
        );
    }

    @Override
    public void trace(Marker marker, String format, Object... argArray) {
        delegate.verbose(
                factory.toMMarket(marker),
                StringBuilderFormattable.by(() -> String.format(format, argArray))
        );
    }

    @Override
    public void trace(Marker marker, String msg, Throwable t) {
        delegate.verbose(
                factory.toMMarket(marker),
                msg, t
        );
    }

    @Override
    public boolean isDebugEnabled() {
        return delegate.isDebugEnabled();
    }

    @Override
    public void debug(String msg) {
        delegate.debug(msg);
    }

    @Override
    public void debug(String format, Object arg) {
        delegate.debug(
                StringBuilderFormattable.by(() -> String.format(format, arg))
        );
    }

    @Override
    public void debug(String format, Object arg1, Object arg2) {
        delegate.debug(
                StringBuilderFormattable.by(() -> String.format(format, arg1, arg2))
        );
    }

    @Override
    public void debug(String format, Object... arguments) {
        delegate.debug(
                StringBuilderFormattable.by(() -> String.format(format, arguments))
        );
    }

    @Override
    public void debug(String msg, Throwable t) {
        delegate.debug(msg, t);
    }

    @Override
    public boolean isDebugEnabled(Marker marker) {
        return delegate.isDebugEnabled(factory.toMMarket(marker));
    }

    @Override
    public void debug(Marker marker, String msg) {
        delegate.debug(
                factory.toMMarket(marker),
                msg
        );
    }

    @Override
    public void debug(Marker marker, String format, Object arg) {
        delegate.debug(
                factory.toMMarket(marker),
                StringBuilderFormattable.by(() -> String.format(format, arg))
        );
    }

    @Override
    public void debug(Marker marker, String format, Object arg1, Object arg2) {
        delegate.debug(
                factory.toMMarket(marker),
                StringBuilderFormattable.by(() -> String.format(format, arg1, arg2))
        );
    }

    @Override
    public void debug(Marker marker, String format, Object... arguments) {
        delegate.debug(
                factory.toMMarket(marker),
                StringBuilderFormattable.by(() -> String.format(format, arguments))
        );
    }

    @Override
    public void debug(Marker marker, String msg, Throwable t) {
        delegate.debug(
                factory.toMMarket(marker),
                msg, t
        );
    }

    @Override
    public boolean isInfoEnabled() {
        return delegate.isInfoEnabled();
    }

    @Override
    public void info(String msg) {
        delegate.info(msg);
    }

    @Override
    public void info(String format, Object arg) {
        delegate.info(
                StringBuilderFormattable.by(() -> String.format(format, arg))
        );
    }

    @Override
    public void info(String format, Object arg1, Object arg2) {
        delegate.info(
                StringBuilderFormattable.by(() -> String.format(format, arg1, arg2))
        );
    }

    @Override
    public void info(String format, Object... arguments) {
        delegate.info(
                StringBuilderFormattable.by(() -> String.format(format, arguments))
        );
    }

    @Override
    public void info(String msg, Throwable t) {
        delegate.info(msg, t);
    }

    @Override
    public boolean isInfoEnabled(Marker marker) {
        return delegate.isInfoEnabled(factory.toMMarket(marker));
    }

    @Override
    public void info(Marker marker, String msg) {
        delegate.info(
                factory.toMMarket(marker),
                msg
        );
    }

    @Override
    public void info(Marker marker, String format, Object arg) {
        delegate.info(
                factory.toMMarket(marker),
                StringBuilderFormattable.by(() -> String.format(format, arg))
        );
    }

    @Override
    public void info(Marker marker, String format, Object arg1, Object arg2) {
        delegate.info(
                factory.toMMarket(marker),
                StringBuilderFormattable.by(() -> String.format(format, arg1, arg2))
        );
    }

    @Override
    public void info(Marker marker, String format, Object... arguments) {
        delegate.info(
                factory.toMMarket(marker),
                StringBuilderFormattable.by(() -> String.format(format, arguments))
        );
    }

    @Override
    public void info(Marker marker, String msg, Throwable t) {
        delegate.info(
                factory.toMMarket(marker),
                msg, t
        );
    }

    @Override
    public boolean isWarnEnabled() {
        return delegate.isWarnEnabled();
    }

    @Override
    public void warn(String msg) {
        delegate.warn(msg);
    }

    @Override
    public void warn(String format, Object arg) {
        delegate.warn(
                StringBuilderFormattable.by(() -> String.format(format, arg))
        );
    }

    @Override
    public void warn(String format, Object... arguments) {
        delegate.warn(
                StringBuilderFormattable.by(() -> String.format(format, arguments))
        );
    }

    @Override
    public void warn(String format, Object arg1, Object arg2) {
        delegate.warn(
                StringBuilderFormattable.by(() -> String.format(format, arg1, arg2))
        );
    }

    @Override
    public void warn(String msg, Throwable t) {
        delegate.warn(
                msg, t
        );
    }

    @Override
    public boolean isWarnEnabled(Marker marker) {
        return delegate.isWarnEnabled(factory.toMMarket(marker));
    }

    @Override
    public void warn(Marker marker, String msg) {
        delegate.warn(
                factory.toMMarket(marker),
                msg
        );
    }

    @Override
    public void warn(Marker marker, String format, Object arg) {
        delegate.warn(
                factory.toMMarket(marker),
                StringBuilderFormattable.by(() -> String.format(format, arg))
        );
    }

    @Override
    public void warn(Marker marker, String format, Object arg1, Object arg2) {
        delegate.warn(
                factory.toMMarket(marker),
                StringBuilderFormattable.by(() -> String.format(format, arg1, arg2))
        );
    }

    @Override
    public void warn(Marker marker, String format, Object... arguments) {
        delegate.warn(
                factory.toMMarket(marker),
                StringBuilderFormattable.by(() -> String.format(format, arguments))
        );
    }

    @Override
    public void warn(Marker marker, String msg, Throwable t) {
        delegate.warn(
                factory.toMMarket(marker),
                msg, t
        );
    }

    @Override
    public boolean isErrorEnabled() {
        return delegate.isErrorEnabled();
    }

    @Override
    public void error(String msg) {
        delegate.error(msg);
    }

    @Override
    public void error(String format, Object arg) {
        delegate.error(
                StringBuilderFormattable.by(() -> String.format(format, arg))
        );
    }

    @Override
    public void error(String format, Object arg1, Object arg2) {
        delegate.error(
                StringBuilderFormattable.by(() -> String.format(format, arg1, arg2))
        );
    }

    @Override
    public void error(String format, Object... arguments) {
        delegate.error(
                StringBuilderFormattable.by(() -> String.format(format, arguments))
        );
    }

    @Override
    public void error(String msg, Throwable t) {
        delegate.error(msg, t);
    }

    @Override
    public boolean isErrorEnabled(Marker marker) {
        return delegate.isErrorEnabled(factory.toMMarket(marker));
    }

    @Override
    public void error(Marker marker, String msg) {
        delegate.error(
                factory.toMMarket(marker),
                msg
        );
    }

    @Override
    public void error(Marker marker, String format, Object arg) {
        delegate.error(
                factory.toMMarket(marker),
                StringBuilderFormattable.by(() -> String.format(format, arg))
        );
    }

    @Override
    public void error(Marker marker, String format, Object arg1, Object arg2) {
        delegate.error(
                factory.toMMarket(marker),
                StringBuilderFormattable.by(() -> String.format(format, arg1, arg2))
        );
    }

    @Override
    public void error(Marker marker, String format, Object... arguments) {
        delegate.error(
                factory.toMMarket(marker),
                StringBuilderFormattable.by(() -> String.format(format, arguments))
        );
    }

    @Override
    public void error(Marker marker, String msg, Throwable t) {
        delegate.error(
                factory.toMMarket(marker),
                msg, t
        );
    }
}
