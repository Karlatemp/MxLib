package io.github.karlatemp.mxlib.spigot.impl;

import io.github.karlatemp.mxlib.logger.MMarket;
import io.github.karlatemp.mxlib.logger.renders.PrefixedRender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public enum PlLoggerName implements PrefixedRender.PrefixSupplier {
    I;

    @Override
    public @NotNull CharSequence rendPrefix(
            @Nullable String loggerName,
            @Nullable MMarket market,
            @NotNull StringBuilder content,
            boolean isError,
            @Nullable Level level,
            @Nullable LogRecord record
    ) {
        String sm = null;
        if (record != null) {
            String name = record.getLoggerName();
            if (name != null) sm = name;
        }
        if (sm == null && loggerName != null)
            sm = loggerName;
        if (sm != null) {
            try {
                JavaPlugin plugin = JavaPlugin.getProvidingPlugin(Class.forName(sm));
                Logger logger = plugin.getLogger();
                if (logger instanceof PlLogger) return ((PlLogger) logger).getPluginName();
                return plugin.getName();
            } catch (Throwable ignore) {
            }
        }
        if (sm == null) return "TopLevel";
        return sm;
    }
}
