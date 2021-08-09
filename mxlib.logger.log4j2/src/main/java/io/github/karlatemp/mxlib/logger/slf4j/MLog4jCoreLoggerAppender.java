/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-logger-log4j2.main/MLog4jCoreLoggerAppender.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.logger.slf4j;

import io.github.karlatemp.mxlib.logger.MLogger;
import io.github.karlatemp.mxlib.logger.MLoggerFactory;
import io.github.karlatemp.mxlib.utils.StringBuilderFormattable;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.Property;

import java.io.Serializable;

public class MLog4jCoreLoggerAppender extends AbstractAppender {
    private final MLoggerFactory factory;

    public MLog4jCoreLoggerAppender(
            String name,
            Filter filter,
            Layout<? extends Serializable> layout,
            boolean ignoreExceptions,
            Property[] properties,
            MLoggerFactory factory
    ) {
        super(name, filter, layout, ignoreExceptions, properties);
        this.factory = factory;
    }

    @Override
    public void append(LogEvent event) {
        StringBuilderFormattable msg = MLog4jMessageFactory.of(event.getMessage());
        MLogger logger = factory.getLogger(event.getLoggerName());
        Throwable thrown = event.getThrown();
        if (ignoreExceptions()) thrown = null;
        logger.log(Log4jMarker.of(event.getMarker()), MLog4jLogger.toLv(event.getLevel()), msg, thrown);
    }
}
