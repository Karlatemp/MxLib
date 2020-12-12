package io.github.karlatemp.mxlib.injector;

import io.github.karlatemp.mxlib.bean.IBeanManager;
import io.github.karlatemp.mxlib.exception.ObjectCreateException;
import org.jetbrains.annotations.NotNull;

public interface IObjectCreator {
    @NotNull IBeanManager getBeanManager();

    <T> @NotNull T newInstance(@NotNull Class<T> type) throws ObjectCreateException;
}
