/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-api.main/SimpleRender.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.logger.renders;

import io.github.karlatemp.mxlib.logger.MMarket;
import io.github.karlatemp.mxlib.logger.MessageFactory;
import io.github.karlatemp.mxlib.logger.MessageRender;
import io.github.karlatemp.mxlib.utils.StringBuilderFormattable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.SimpleFormatter;

/**
 * Standard message render
 *
 * @see PrefixedRender
 */
public class SimpleRender implements MessageRender {
    private final MessageFactory factory;
    private static final SimpleFormatter SIMPLE_FORMATTER = new SimpleFormatter();

    public SimpleRender(MessageFactory factory) {
        this.factory = factory;
    }

    @Override
    public @NotNull StringBuilder render(
            @Nullable String loggerName, @Nullable MMarket market,
            @Nullable StringBuilderFormattable message,
            boolean isError,
            @Nullable Level level,
            @Nullable LogRecord record
    ) {
        StringBuilder builder = new StringBuilder(255);
        if (message != null) {
            if (message instanceof StringBuilderFormattable.Link) {
                StringBuilderFormattable.Link link = (StringBuilderFormattable.Link) message;
                List<Object> objects = link.getList();
                if (!objects.isEmpty()) {
                    Object last = objects.get(objects.size() - 1);
                    if (last instanceof Throwable) {
                        if (objects.size() == 1) {
                            objects.set(0, factory.dump((Throwable) last, true));
                        } else {
                            objects.set(objects.size() - 1, StringBuilderFormattable.LN);
                            objects.add(factory.dump((Throwable) last, true));
                        }
                    }
                }
            }
            message.formatTo(builder);
        }
        if (record != null) {
            if (message != null) builder.append('\n');
            Throwable thr = record.getThrown();
            builder.append(SIMPLE_FORMATTER.formatMessage(record));
            if (thr != null) {
                builder.append('\n');
                factory.dump(thr, true).formatTo(builder);
            }
        }
        return builder;
    }
}
