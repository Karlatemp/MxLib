package io.github.karlatemp.mxlib.injector;

import io.github.karlatemp.mxlib.bean.IBeanManager;
import io.github.karlatemp.mxlib.exception.InjectException;
import org.jetbrains.annotations.NotNull;

public interface IInjector {
    @NotNull IBeanManager getBeanManager();

    void inject(@NotNull Class<?> klass) throws InjectException;

    <T> @NotNull T inject(@NotNull T obj) throws InjectException;
}
