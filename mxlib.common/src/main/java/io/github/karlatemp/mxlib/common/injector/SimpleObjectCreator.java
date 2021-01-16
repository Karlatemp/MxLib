/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-common.main/SimpleObjectCreator.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.common.injector;

import io.github.karlatemp.mxlib.annotations.injector.Inject;
import io.github.karlatemp.mxlib.bean.ContextBean;
import io.github.karlatemp.mxlib.bean.IBeanManager;
import io.github.karlatemp.mxlib.exception.ObjectCreateException;
import io.github.karlatemp.mxlib.injector.IInjector;
import io.github.karlatemp.mxlib.injector.IObjectCreator;
import io.github.karlatemp.mxlib.injector.MethodCallerWithBeans;
import io.github.karlatemp.unsafeaccessor.Root;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class SimpleObjectCreator implements IObjectCreator, ContextBean {
    private final IBeanManager beanManager;

    public SimpleObjectCreator(@NotNull IBeanManager beanManager) {
        this.beanManager = beanManager;
    }

    @Override
    public @NotNull IBeanManager getBeanManager() {
        return beanManager;
    }

    protected Constructor<?> findConstructor(Constructor<?>[] constructors) {
        int length = constructors.length;
        for (int i = 0; i < length; i++) {
            Constructor<?> constructor = constructors[i];
            if (constructor.isAnnotationPresent(Inject.class)) {
                i++;
                for (; i < length; i++) {
                    if (constructors[i].isAnnotationPresent(Inject.class)) {
                        return null;
                    }
                }
                return constructor;
            }
        }
        if (length == 1) return constructors[0];
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> @NotNull T newInstance(@NotNull Class<T> type) throws ObjectCreateException {
        Constructor<?>[] constructors = type.getDeclaredConstructors();
        Constructor<T> c = (Constructor<T>) findConstructor(constructors);

        if (c == null) {
            throw new ObjectCreateException("Cannot allocate " + type + ", no constructor selected.");
        }
        IInjector injector = beanManager.getBy(IInjector.class).orElseGet(() -> new SimpleInjector(beanManager));
        injector.inject(type);
        MethodCallerWithBeans caller = beanManager.getBy(MethodCallerWithBeans.class).orElseGet(SimpleMethodCallerWithBeans::new);
        Root.openAccess(c);

        try {
            return injector.inject(caller.callConstructor(beanManager, c));
        } catch (InstantiationException | IllegalAccessException e) {
            throw new ObjectCreateException(e);
        } catch (InvocationTargetException e) {
            throw new ObjectCreateException(e.getTargetException());
        }
    }

    @Override
    public @NotNull SimpleObjectCreator copy(@NotNull IBeanManager newBeanManager) {
        return new SimpleObjectCreator(newBeanManager);
    }
}
