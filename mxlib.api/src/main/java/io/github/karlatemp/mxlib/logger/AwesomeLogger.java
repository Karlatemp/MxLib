/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-api.main/AwesomeLogger.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.logger;

import io.github.karlatemp.mxlib.utils.StringBuilderFormattable;

import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public abstract class AwesomeLogger extends AbstractLogger {
    public static class Awesome extends AwesomeLogger {
        private final Consumer<StringBuilder> printer;
        private final String name;

        public Awesome(String name, Consumer<StringBuilder> printer, MessageRender render) {
            this.render = render;
            this.printer = printer;
            this.name = name;
        }

        @Override
        protected void render(StringBuilder message) {
            printer.accept(message);
        }

        @Override
        public String getName() {
            return name;
        }
    }

    protected MessageRender render;

    protected AwesomeLogger() {
    }

    protected AwesomeLogger(MessageRender render) {
        this.render = render;
    }

    @Override
    public boolean isEnabled(MMarket market) {
        return true;
    }

    public boolean isEnabled(Level leve) {
        return true;
    }

    @Override
    public boolean isWarnEnabled(MMarket market) {
        return isEnabled(market, Level.WARNING);
    }

    @Override
    public boolean isDebugEnabled(MMarket market) {
        return isEnabled(market, Level.FINE);
    }

    @Override
    public boolean isVerboseEnabled() {
        return isEnabled(Level.FINER);
    }

    @Override
    public boolean isVerboseEnabled(MMarket market) {
        return isEnabled(market, Level.FINER);
    }

    @Override
    public boolean isInfoEnabled() {
        return isEnabled(Level.INFO);
    }

    @Override
    public boolean isInfoEnabled(MMarket market) {
        return isEnabled(market, Level.INFO);
    }

    @Override
    public boolean isErrorEnabled() {
        return isEnabled(Level.SEVERE);
    }

    @Override
    public boolean isErrorEnabled(MMarket market) {
        return isEnabled(market, Level.SEVERE);
    }

    @Override
    public void log(MMarket market, LogRecord logRecord) {
        if (isEnabled(market, logRecord.getLevel()))
            log0(market, logRecord);
    }

    @Override
    public void log(MMarket market, Level level, StringBuilderFormattable message) {
        if (isEnabled(market, level))
            log0(market, level, message);
    }

    @Override
    public void info(MMarket market, StringBuilderFormattable msg) {
        if (isEnabled(market, Level.INFO))
            info0(market, msg);
    }

    @Override
    public void warn(MMarket market, StringBuilderFormattable msg) {
        if (isEnabled(market, Level.WARNING))
            warn0(market, msg);
    }

    @Override
    public void error(MMarket market, StringBuilderFormattable msg) {
        if (isEnabled(market, Level.SEVERE))
            error0(market, msg);
    }

    @Override
    public void debug(MMarket market, StringBuilderFormattable msg) {
        if (isEnabled(market, Level.FINE))
            debug0(market, msg);
    }

    @Override
    public void verbose(MMarket market, StringBuilderFormattable msg) {
        if (isEnabled(market, Level.FINER))
            verbose0(market, msg);
    }

    protected boolean isError(Level level) {
        return level == Level.WARNING || level == Level.SEVERE;
    }

    protected void log0(MMarket market, LogRecord logRecord) {
        render(render.render(
                getName(), market,
                null,
                isError(logRecord.getLevel()),
                logRecord.getLevel(),
                logRecord
        ));
    }

    protected abstract void render(StringBuilder message);

    protected void log0(MMarket market, Level level, StringBuilderFormattable message) {
        render(render.render(getName(), market, message, isError(level), level, null));
    }

    protected void info0(MMarket market, StringBuilderFormattable msg) {
        log0(market, Level.INFO, msg);
    }

    protected void warn0(MMarket market, StringBuilderFormattable msg) {
        log0(market, Level.WARNING, msg);
    }

    protected void error0(MMarket market, StringBuilderFormattable msg) {
        log0(market, Level.SEVERE, msg);
    }

    protected void debug0(MMarket market, StringBuilderFormattable msg) {
        log0(market, Level.FINE, msg);
    }

    protected void verbose0(MMarket market, StringBuilderFormattable msg) {
        log0(market, Level.FINER, msg);
    }
}
