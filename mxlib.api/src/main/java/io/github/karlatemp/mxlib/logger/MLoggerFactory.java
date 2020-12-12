package io.github.karlatemp.mxlib.logger;

import io.github.karlatemp.mxlib.internal.PrefixedLoggerFactory;
import org.jetbrains.annotations.NotNull;

public interface MLoggerFactory {
    @NotNull MLogger getLogger(String name);

    default @NotNull MLoggerFactory withPrefix(String prefix) {
        if (prefix == null || prefix.isEmpty()) return this;
        return new PrefixedLoggerFactory(this, prefix);
    }
}
