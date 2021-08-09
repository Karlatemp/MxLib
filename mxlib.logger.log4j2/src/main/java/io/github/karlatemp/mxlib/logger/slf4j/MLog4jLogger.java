/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-logger-log4j2.main/MLog4jLogger.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.logger.slf4j;

import io.github.karlatemp.mxlib.logger.MLogger;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.message.Message;

import java.util.HashMap;
import java.util.Map;

public class MLog4jLogger extends AbsLog4jLogger {
    private final MLogger logger;
    static final Map<Level, java.util.logging.Level> levelMapping = new HashMap<>();

    static {
        levelMapping.put(Level.OFF, java.util.logging.Level.OFF);
        levelMapping.put(Level.FATAL, java.util.logging.Level.SEVERE);
        levelMapping.put(Level.ERROR, java.util.logging.Level.SEVERE);
        levelMapping.put(Level.WARN, java.util.logging.Level.WARNING);
        levelMapping.put(Level.INFO, java.util.logging.Level.INFO);
        levelMapping.put(Level.DEBUG, java.util.logging.Level.FINER);
        levelMapping.put(Level.TRACE, java.util.logging.Level.FINEST);
        levelMapping.put(Level.ALL, java.util.logging.Level.ALL);
    }

    public MLog4jLogger(MLogger logger) {
        super(logger.getName());
        this.logger = logger;
    }

    static java.util.logging.Level toLv(Level lv) {
        return levelMapping.getOrDefault(lv, java.util.logging.Level.INFO);
    }

    @Override
    public boolean isEnabled(Level level, Marker marker, Object message, Throwable t) {
        return logger.isEnabled(Log4jMarker.of(marker), toLv(level));
    }

    @Override
    public void logMessage(String fqcn, Level level, Marker marker, Message message, Throwable t) {
        logger.log(Log4jMarker.of(marker),toLv(level), MLog4jMessageFactory.of(message), t);
    }

    @Override
    public Level getLevel() {
        return Level.ALL; // unknown
    }
}
