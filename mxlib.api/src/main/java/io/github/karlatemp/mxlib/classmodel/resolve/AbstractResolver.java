/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-api.main/AbstractResolver.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.classmodel.resolve;

import io.github.karlatemp.mxlib.classmodel.*;
import org.jetbrains.annotations.NotNull;

import java.lang.invoke.MethodType;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

public abstract class AbstractResolver implements Resolver {
    protected abstract static class Awesome extends AbstractResolver {
        protected abstract Class<?> tryResolve(ClassInfo classInfo) throws ClassNotFoundException;

        protected Class<?> onResolveFailure(ClassInfo classInfo, ClassNotFoundException exception) throws ClassNotFoundException {
            if (exception != null) throw exception;
            throw new ClassNotFoundException(classInfo.getName());
        }

        @Override
        public @NotNull Class<?> resolve(ClassInfo classInfo) throws ClassNotFoundException {
            try {
                Class<?> resp = tryResolve(classInfo);
                if (resp != null) {
                    return resp;
                }
            } catch (ClassNotFoundException cnfe) {
                return onResolveFailure(classInfo, cnfe);
            }
            return onResolveFailure(classInfo, null);
        }

        @Override
        public @NotNull Resolver withLoader(ClassLoader loader) {
            Awesome this0 = this;
            return new Awesome() {
                @Override
                protected Class<?> tryResolve(ClassInfo classInfo) throws ClassNotFoundException {
                    return this0.tryResolve(classInfo);
                }

                @Override
                protected Class<?> onResolveFailure(ClassInfo classInfo, ClassNotFoundException exception) throws ClassNotFoundException {
                    try {
                        return Class.forName(classInfo.getName(), false, loader);
                    } catch (Throwable throwable) {
                        if (exception != null) throwable.addSuppressed(throwable);
                        throw throwable;
                    }
                }
            };
        }
    }

    protected Class<?>[] resolve(List<ClassInfo> classes) throws ClassNotFoundException {
        Class<?>[] response = new Class[classes.size()];
        for (int i = 0; i < response.length; i++) {
            response[i] = resolve(classes.get(i));
        }
        return response;
    }

    @Override
    public @NotNull Method resolve(MethodInfo methodInfo) throws ClassNotFoundException, NoSuchMethodException {
        return resolve(methodInfo.getDeclaredClass())
                .getDeclaredMethod(
                        methodInfo.getName(),
                        resolve(methodInfo.getArgumentTypes())
                );
    }

    @Override
    public @NotNull Field resolve(FieldInfo fieldInfo) throws ClassNotFoundException, NoSuchFieldException {
        return resolve(fieldInfo.getType()).getDeclaredField(fieldInfo.getName());
    }

    @Override
    public @NotNull Constructor<?> resolve(ConstructorInfo constructorInfo) throws ClassNotFoundException, NoSuchMethodException {
        return resolve(constructorInfo.getDeclaredClass())
                .getDeclaredConstructor(resolve(constructorInfo.getArgumentTypes()));
    }

    @Override
    public @NotNull MethodType resolveMethodType(ExecutableInfo executableInfo) throws ClassNotFoundException {
        return MethodType.methodType(resolve(executableInfo.getReturnType()), resolve(executableInfo.getArgumentTypes()));
    }
}
