package io.github.karlatemp.mxlib.bean;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Stream;

public interface IBeanManager {
    @Nullable IBeanManager getParent();

    <T> @NotNull Optional<T> getBy(@NotNull Class<T> type);

    default @NotNull <T> T getBeanNonNull(@NotNull Class<T> type) {
        return getBy(type).orElseThrow(NoSuchElementException::new);
    }

    default @NotNull <T> T getBeanNonNull(@NotNull Class<T> type, @Nullable String name) {
        return getBy(type, name).orElseThrow(NoSuchElementException::new);
    }

    <T> @NotNull Optional<T> getBy(@NotNull Class<T> type, @Nullable String name);

    <T> @NotNull Stream<T> getAll(@NotNull Class<T> type);

    <T> void register(@NotNull Class<T> type, @NotNull T value);

    <T> void register(@NotNull Class<T> type, @Nullable String name, @NotNull T value);

    <T> void unregister(@NotNull Class<T> type);

    <T> void unregister(@NotNull Class<T> type, @Nullable String name);

    @NotNull IBeanManager newSubScope();
}
