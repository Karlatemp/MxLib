package io.github.karlatemp.mxlib.utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.URL;
import java.nio.file.Path;

public interface ClassLocator {
    @Nullable Path findLocate(@NotNull Class<?> type);

    @Nullable ClassLoader context();

    @Nullable Path findLocate(@NotNull String classname);

    @Nullable Path findLocate(@NotNull URL url);
}
