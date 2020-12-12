package io.github.karlatemp.mxlib.logger;

import io.github.karlatemp.mxlib.utils.StringBuilderFormattable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.logging.Level;
import java.util.logging.LogRecord;

public interface MessageRender {
    @NotNull StringBuilder render(
            @Nullable String loggerName,
            @Nullable MMarket market,
            @Nullable StringBuilderFormattable message,
            boolean isError,
            @Nullable Level level,
            @Nullable LogRecord record
    );
}
