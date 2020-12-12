package io.github.karlatemp.mxlib.internal;

import io.github.karlatemp.mxlib.logger.MLogger;
import io.github.karlatemp.mxlib.logger.MLoggerFactory;
import org.jetbrains.annotations.NotNull;

public class PrefixedLoggerFactory implements MLoggerFactory {
    private final String prefix;
    private final MLoggerFactory parent;

    public PrefixedLoggerFactory(MLoggerFactory parent, String prefix) {
        this.parent = parent;
        this.prefix = prefix;
    }

    @Override
    public @NotNull MLogger getLogger(String name) {
        return parent.getLogger(prefix + name);
    }

    @Override
    public @NotNull MLoggerFactory withPrefix(String prefix) {
        if (prefix == null || prefix.isEmpty()) return this;
        return new PrefixedLoggerFactory(parent, prefix + this.prefix);
    }
}
