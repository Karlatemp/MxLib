/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-logger-log4j2.main/MLoggerLog4j.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.logger.slf4j;

import io.github.karlatemp.mxlib.logger.AwesomeLogger;
import io.github.karlatemp.mxlib.logger.MMarket;
import io.github.karlatemp.mxlib.utils.StringBuilderFormattable;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.LogRecord;
import java.util.logging.SimpleFormatter;

public class MLoggerLog4j extends AwesomeLogger {
    static final Map<java.util.logging.Level, Level> levelMapping = new HashMap<>();
    private static final SimpleFormatter SIMPLE_FORMATTER = new SimpleFormatter();

    static {
        levelMapping.put(java.util.logging.Level.CONFIG, Level.DEBUG);
        levelMapping.put(java.util.logging.Level.FINE, Level.DEBUG);
        levelMapping.put(java.util.logging.Level.FINER, Level.DEBUG);
        levelMapping.put(java.util.logging.Level.FINEST, Level.TRACE);
        for (Map.Entry<Level, java.util.logging.Level> lvm : MLog4jLogger.levelMapping.entrySet()) {
            levelMapping.put(lvm.getValue(), lvm.getKey());
        }
    }

    private final Logger logger;

    public MLoggerLog4j(Logger logger) {
        this.logger = logger;
    }

    @Override
    public boolean isEnabled(MMarket marker, java.util.logging.Level level) {
        return logger.isEnabled(toLv(level), mk(marker));
    }

    static Level toLv(java.util.logging.Level lv) {
        return levelMapping.getOrDefault(lv, Level.INFO);
    }

    static Marker mk(MMarket mt) {
        if (mt == null) return null;
        return MarkerManager.getMarker(mt.getName());
    }

    @Override
    protected void render(StringBuilder message) {
        throw new AssertionError();
    }

    @Override
    protected void log0(MMarket market, java.util.logging.Level level, StringBuilderFormattable message) {
        Throwable thrown = null;
        { // Split thrown
            if (message instanceof StringBuilderFormattable.Link) {
                List<Object> list = ((StringBuilderFormattable.Link) message).getList();
                if (list.size() > 1) {
                    Object last = list.get(list.size() - 1);
                    if (last instanceof Throwable) {
                        thrown = (Throwable) last;
                        list.remove(list.size() - 1);
                    }
                }
            }
        }
        log0(market, toLv(level), message, thrown);
    }

    private void log0(MMarket m, Level lv, StringBuilderFormattable msg, Throwable thrown) {
        if (msg == null) {
            logger.log(lv, mk(m), (String) null, thrown);
            return;
        }
        logger.log(lv, mk(m), msg::to_string, thrown);
    }

    @Override
    protected void log0(MMarket market, LogRecord logRecord) {
        log0(market, toLv(logRecord.getLevel()),
                sb -> sb.append(SIMPLE_FORMATTER.formatMessage(logRecord)),
                logRecord.getThrown()
        );
    }

    @Override
    public String getName() {
        return logger.getName();
    }
}
