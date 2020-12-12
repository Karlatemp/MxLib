package io.github.karlatemp.mxlib.format;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.Reader;

public interface Formatter {
    @NotNull FormatTemplate parse(@NotNull String template);

    @NotNull FormatTemplate parse(@NotNull Reader template) throws IOException;
}
