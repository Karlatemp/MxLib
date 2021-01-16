/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-spigot-impl.main/PlLogger.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.spigot.impl;

import io.github.karlatemp.mxlib.MxLib;
import io.github.karlatemp.unsafeaccessor.Root;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginLogger;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.util.logging.Filter;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class PlLogger extends PluginLogger {
    private static final Field $logger;

    static {
        try {
            $logger = Root.openAccess(JavaPlugin.class.getDeclaredField("logger"));
        } catch (NoSuchFieldException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public static void inject(JavaPlugin inject) {
        try {
            Object logger = $logger.get(inject);
            if (logger instanceof PlLogger) return;
            $logger.set(inject, new PlLogger(inject));
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    private final String pluginName;

    public PlLogger(Plugin context) {
        super(context);
        String prefix = context.getDescription().getPrefix();
        pluginName = prefix != null ? prefix : context.getDescription().getName();
    }

    public String getPluginName() {
        return pluginName;
    }

    @Override
    public void log(LogRecord record) {
        if (!isLoggable(record.getLevel())) {
            return;
        }
        Filter theFilter = getFilter();
        if (theFilter != null && !theFilter.isLoggable(record)) {
            return;
        }

        record.setLoggerName(pluginName);
        MxLib.getJdkLogger().log(record);
        if (getUseParentHandlers()) {
            Handler[] handlers = getHandlers();
            if (handlers != null) for (Handler h : handlers) {
                if (h != null) h.publish(record);
            }
        }
    }
}
