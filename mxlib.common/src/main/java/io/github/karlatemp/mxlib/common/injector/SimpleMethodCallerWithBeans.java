package io.github.karlatemp.mxlib.common.injector;

import io.github.karlatemp.mxlib.annotations.injector.Inject;
import io.github.karlatemp.mxlib.bean.IBeanManager;
import io.github.karlatemp.mxlib.exception.InjectException;
import io.github.karlatemp.mxlib.injector.MethodCallerWithBeans;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;

public class SimpleMethodCallerWithBeans implements MethodCallerWithBeans {
    private static final Annotation[] EMPTY_ANNOTATION_ARRAY = new Annotation[0];

    @Override
    public Object call(
            @NotNull IBeanManager beanManager,
            @NotNull Executable method,
            @Nullable Object instance
    ) throws IllegalAccessException, InvocationTargetException, InjectException, InstantiationException {

        Class<?>[] types = method.getParameterTypes();
        Annotation[][] annotations = method.getParameterAnnotations();

        if (annotations.length + 1 == types.length) {
            Annotation[][] tmp = new Annotation[types.length][];
            System.arraycopy(annotations, 0, tmp, 1, annotations.length);
            tmp[0] = EMPTY_ANNOTATION_ARRAY;
            annotations = tmp;
        }

        Inject[] injectFlags = new Inject[annotations.length];
        for (int i = 0; i < annotations.length; i++) {
            for (Annotation ano : annotations[i]) {
                if (ano instanceof Inject) {
                    injectFlags[i] = (Inject) ano;
                    break;
                }
            }
        }
        Object[] arguments = new Object[annotations.length];
        for (int i = 0; i < arguments.length; i++) {
            Inject inject = injectFlags[i];
            if (inject != null) {
                Class<?> type = inject.value();
                if (type == Void.class) {
                    type = types[i];
                }
                if (!types[i].isAssignableFrom(type)) {
                    throw new InjectException("Exception in inject parameter " + i + " of method " + method + ", " + type + " cannot cast to " + types[i]);
                }
                String name = inject.name();
                if (name.equals(Inject.NAME_UNSET)) {
                    name = null;
                }
                Optional<?> optional = beanManager.getBy(type, name);
                if (optional.isPresent()) {
                    arguments[i] = optional.get();
                } else if (!inject.nullable()) {
                    throw new InjectException("Failed inject parameter " + i + " of method " + method + ", Bean[" + type + ", named " + name + "] not found.");
                }
            } else {
                arguments[i] = safelyValue(types[i]);
            }
        }
        if (method instanceof Method) {
            return ((Method) method).invoke(instance, arguments);
        } else if (method instanceof Constructor<?>) {
            return ((Constructor<?>) method).newInstance(arguments);
        } else {
            throw new IllegalArgumentException("Unknown how to execute " + method);
        }
    }

    private static final Integer ZERO_INT = 0;
    private static final Long ZERO_LONG = 0L;
    private static final Double ZERO_DOUBLE = 0d;
    private static final Float ZERO_FLOAT = 0f;
    private static final Byte ZERO_BYTE = (byte) 0;
    private static final Short ZERO_SHORT = (short) 0;
    private static final Character ZERO_CHAR = '\u0000';

    private static Object safelyValue(Class<?> type) {
        if (type == int.class) return ZERO_INT;
        if (type == long.class) return ZERO_LONG;
        if (type == double.class) return ZERO_DOUBLE;
        if (type == float.class) return ZERO_FLOAT;
        if (type == byte.class) return ZERO_BYTE;
        if (type == short.class) return ZERO_SHORT;
        if (type == boolean.class) return Boolean.FALSE;
        if (type == char.class) return ZERO_CHAR;
        return null;
    }

}
