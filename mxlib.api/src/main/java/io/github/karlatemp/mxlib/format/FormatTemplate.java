package io.github.karlatemp.mxlib.format;

import io.github.karlatemp.mxlib.utils.StringBuilderFormattable;
import org.jetbrains.annotations.NotNull;

public interface FormatTemplate {
    void formatTo(@NotNull StringBuilder buffer, @NotNull FormatArguments arguments);

    default StringBuilderFormattable format(@NotNull FormatArguments arguments) {
        return builder -> FormatTemplate.this.formatTo(builder, arguments);
    }
}
