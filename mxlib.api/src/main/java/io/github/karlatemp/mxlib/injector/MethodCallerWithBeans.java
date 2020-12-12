package io.github.karlatemp.mxlib.injector;

import io.github.karlatemp.mxlib.bean.IBeanManager;
import io.github.karlatemp.mxlib.exception.InjectException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public interface MethodCallerWithBeans {
    Object call(
            @NotNull IBeanManager beanManager,
            @NotNull Executable method,
            @Nullable Object instance
    ) throws InstantiationException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException,
            InjectException;

    @SuppressWarnings("unchecked")
    default <T> T callConstructor(
            @NotNull IBeanManager beanManager,
            @NotNull Constructor<T> constructor
    ) throws InstantiationException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException,
            InjectException {
        return (T) call(beanManager, constructor, null);
    }

    default Object callMethod(
            @NotNull IBeanManager beanManager,
            @NotNull Method method,
            @Nullable Object instance
    ) throws IllegalAccessException,
            InvocationTargetException,
            InjectException {
        try {
            return call(beanManager, method, instance);
        } catch (InstantiationException e) {
            throw (RuntimeException) new IllegalAccessException().initCause(e);
        }
    }
}
