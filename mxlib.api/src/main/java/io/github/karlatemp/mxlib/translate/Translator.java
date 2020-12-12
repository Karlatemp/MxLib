package io.github.karlatemp.mxlib.translate;

import io.github.karlatemp.mxlib.format.FormatTemplate;
import io.github.karlatemp.mxlib.utils.StringBuilderFormattable;
import org.jetbrains.annotations.NotNull;

public interface Translator {
    @NotNull FormatTemplate getTranslateTemplate(@NotNull String key);

    @NotNull StringBuilderFormattable format(@NotNull String key);

    @NotNull StringBuilderFormattable format(@NotNull String key, Object... arguments);
}
