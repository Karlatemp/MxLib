/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-api.main/ClassInfo.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.classmodel;

import io.github.karlatemp.mxlib.classmodel.impl.ClassInfoImpl;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public interface ClassInfo
        extends ModifierInfo, Nameable {
    public static ClassInfo ofClass(Class<?> type) {
        return ClassInfoImpl.ofClass(type);
    }

    public static ClassInfo ofClass(Map<Class<?>, ClassInfo> cache, Class<?> type) {
        return ClassInfoImpl.ofClass(cache, type);
    }

    boolean isArray();

    @Contract(pure = true)
    ClassInfo getComponentType();

    @Contract(pure = true)
    @NotNull ClassInfo arrayType();

    @Contract(pure = true)
    @Nullable ClassInfo getSuperclass();

    @Contract(pure = true)
    @NotNull List<ClassInfo> getInterfaces();

    @Contract(pure = true)
    @NotNull List<MethodInfo> getMethods();

    @Contract(pure = true)
    @NotNull List<ConstructorInfo> getConstructors();

    @Contract(pure = true)
    @NotNull List<MethodInfo> getDeclaredMethods();

    @Contract(pure = true)
    @NotNull List<FieldInfo> getFields();

    @Contract(pure = true)
    @NotNull List<FieldInfo> getDeclaredFields();
}
