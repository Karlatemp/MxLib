package io.github.karlatemp.mxlib.common.injector;

import io.github.karlatemp.mxlib.annotations.injector.AfterInject;
import io.github.karlatemp.mxlib.annotations.injector.Inject;
import io.github.karlatemp.mxlib.bean.ContextBean;
import io.github.karlatemp.mxlib.bean.IBeanManager;
import io.github.karlatemp.mxlib.exception.InjectException;
import io.github.karlatemp.mxlib.injector.IInjector;
import io.github.karlatemp.mxlib.injector.Injected;
import io.github.karlatemp.mxlib.injector.MethodCallerWithBeans;
import io.github.karlatemp.mxlib.reflect.Reflections;
import io.github.karlatemp.mxlib.utils.IteratorSupplier;
import io.github.karlatemp.unsafeaccessor.Root;
import io.github.karlatemp.unsafeaccessor.Unsafe;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Optional;
import java.util.stream.Stream;

@SuppressWarnings({"unchecked", "rawtypes", "DuplicatedCode"})
public class SimpleInjector implements IInjector, ContextBean {
    private final IBeanManager beanManager;
    private static final Unsafe UNSAFE = Unsafe.getUnsafe();

    public SimpleInjector(@NotNull IBeanManager beanManager) {
        this.beanManager = beanManager;
    }

    @Override
    public @NotNull IBeanManager getBeanManager() {
        return beanManager;
    }

    private boolean inject(Field field, Class<?> type, boolean nullable, String name, Injected<?> injected, Object instance) {
        try {
            if (injected == null) {
                Root.openAccess(field);
                if (field.get(instance) != null) return false;
            } else if (injected.getValueDirect() != null) {
                return false;
            }

            Optional<?> optional = beanManager.getBy(type, name);
            if (nullable || optional.isPresent()) {
                if (injected == null) {
                    field.set(instance, optional.orElse(null));
                } else {
                    ((Injected) injected).initialize(optional.orElse(null));
                }
            } else {
                throw new InjectException("Bean of " + field + "[" + type + ", named" + name + "] not found.");
            }
            return true;
        } catch (IllegalAccessException e) {
            throw new InjectException("Exception in inject " + field + "[" + type + ", named" + name + "]", e);
        }
    }

    @Override
    public void inject(@NotNull Class<?> klass) throws InjectException {
        boolean injectedFlag = false;
        for (Field field : klass.getDeclaredFields()) {
            if (!Modifier.isStatic(field.getModifiers())) continue;
            if (field.getType() == Injected.class) {
                UNSAFE.ensureClassInitialized(klass);
                Injected<?> injected = (Injected<?>) UNSAFE.getReference(
                        UNSAFE.staticFieldBase(field),
                        UNSAFE.staticFieldOffset(field)
                );
                if (injected == null) continue;
                inject(field, injected.getType(), injected.isNullable(), injected.getName(), injected, null);
            } else {
                Inject inject = field.getDeclaredAnnotation(Inject.class);
                if (inject == null) continue;
                String name = inject.name();
                if (name.equals(Inject.NAME_UNSET)) name = null;
                UNSAFE.ensureClassInitialized(klass);
                Class<?> value = inject.value();
                if (value == Void.class) {
                    value = field.getType();
                }
                if (!field.getType().isAssignableFrom(value)) {
                    throw new ClassCastException("Error in injecting static " + klass + ", " + value + " cannot cast to " + field.getType());
                }
                injectedFlag |= inject(field, value, inject.nullable(), name, null, null);
            }
        }
        if (injectedFlag) {
            callPost(null,
                    Stream.of(klass.getDeclaredMethods())
                            .filter(Reflections.ModifierFilter.STATIC)
            );
        }
    }

    private void callPost(Object instance, Stream<Method> methods) throws InjectException {
        MethodCallerWithBeans caller = beanManager.getBy(MethodCallerWithBeans.class)
                .orElseGet(SimpleMethodCallerWithBeans::new);
        for (Method method : IteratorSupplier.by(methods
                .filter(Reflections.withAnnotated(AfterInject.class))
                .peek(Reflections.openAccess())
                .iterator())) {
            try {
                caller.callMethod(beanManager, method, instance);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new InjectException(e);
            }
        }
    }

    @Override
    public <T> @NotNull T inject(@NotNull T obj) throws InjectException {
        boolean injectedFlag = false;
        for (Field field : IteratorSupplier.by(Reflections.allFields(obj.getClass())
                .filter(Reflections.ModifierFilter.NON_STATIC)
                .peek(Reflections.openAccess())
                .iterator())) {
            if (field.getType() == Injected.class) {
                Injected<?> injected = (Injected<?>) UNSAFE.getReference(
                        obj,
                        UNSAFE.objectFieldOffset(field)
                );
                if (injected == null) continue;
                injectedFlag |= inject(field, injected.getType(), injected.isNullable(), injected.getName(), injected, obj);
            } else {
                Inject inject = field.getDeclaredAnnotation(Inject.class);
                if (inject == null) continue;
                String name = inject.name();
                if (name.equals(Inject.NAME_UNSET)) name = null;
                Class<?> value = inject.value();
                if (value == Void.class) {
                    value = field.getType();
                }
                if (!field.getType().isAssignableFrom(value)) {
                    throw new ClassCastException("Error in injecting " + obj + ", " + value + " cannot cast to " + field.getType());
                }
                injectedFlag |= inject(field, value, inject.nullable(), name, null, obj);
            }
        }
        if (injectedFlag) {
            callPost(obj,
                    Reflections.allMethods(obj.getClass())
                            .filter(Reflections.ModifierFilter.NON_STATIC)
                            .filter(Reflections.ModifierFilter.PRIVATE_OR_PACKAGE)
            );
        }
        return obj;
    }

    @Override
    public @NotNull SimpleInjector copy(@NotNull IBeanManager newBeanManager) {
        return new SimpleInjector(newBeanManager);
    }
}
