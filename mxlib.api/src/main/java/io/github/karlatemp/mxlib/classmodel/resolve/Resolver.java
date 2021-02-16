/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-api.main/Resolver.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.classmodel.resolve;

import io.github.karlatemp.mxlib.classmodel.*;
import io.github.karlatemp.mxlib.classmodel.impl.ClassInfoImpl;
import org.jetbrains.annotations.NotNull;

import java.lang.invoke.MethodType;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public interface Resolver {
    public static final Resolver DEFAULT = new AbstractResolver.Awesome() {
        @Override
        protected Class<?> tryResolve(ClassInfo classInfo) {
            if (classInfo instanceof ClassInfoImpl) {
                return ((ClassInfoImpl) classInfo).vmClass;
            }
            return null;
        }
    };

    @NotNull
    Class<?> resolve(ClassInfo classInfo) throws ClassNotFoundException;

    @NotNull
    Method resolve(MethodInfo methodInfo) throws ClassNotFoundException, NoSuchMethodException;

    @NotNull
    Field resolve(FieldInfo fieldInfo) throws ClassNotFoundException, NoSuchFieldException;

    @NotNull
    Constructor<?> resolve(ConstructorInfo constructorInfo) throws ClassNotFoundException, NoSuchMethodException;

    @NotNull
    MethodType resolveMethodType(ExecutableInfo executableInfo) throws ClassNotFoundException;

    @NotNull Resolver withLoader(ClassLoader loader);
}
