/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-api.main/ConstructorInfoImpl.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.classmodel.impl;

import io.github.karlatemp.mxlib.classmodel.ClassInfo;
import io.github.karlatemp.mxlib.classmodel.ConstructorInfo;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@ApiStatus.Internal
public class ConstructorInfoImpl implements ConstructorInfo {
    private final List<ClassInfo> exceptionTypes;
    private final List<ClassInfo> arguments;
    private final ClassInfo declared;
    private final int modifiers;

    public ConstructorInfoImpl(
            @NotNull List<ClassInfo> arguments,
            @NotNull List<ClassInfo> exceptionTypes,
            @NotNull ClassInfo declared,
            int modifiers
    ) {
        this.arguments = arguments;
        this.exceptionTypes = exceptionTypes;
        this.declared = declared;
        this.modifiers = modifiers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ConstructorInfoImpl that = (ConstructorInfoImpl) o;

        if (modifiers != that.modifiers) return false;
        if (!exceptionTypes.equals(that.exceptionTypes)) return false;
        if (!arguments.equals(that.arguments)) return false;
        return declared.equals(that.declared);
    }

    @Override
    public int hashCode() {
        int result = exceptionTypes.hashCode();
        result = 31 * result + arguments.hashCode();
        result = 31 * result + declared.hashCode();
        result = 31 * result + modifiers;
        return result;
    }

    @Override
    public @NotNull ClassInfo getReturnType() {
        return ClassInfoImpl.ConstantPools.VOID;
    }

    @Override
    public @NotNull List<ClassInfo> getArgumentTypes() {
        return arguments;
    }

    @Override
    public @NotNull List<ClassInfo> getThrows() {
        return exceptionTypes;
    }

    @Override
    public @NotNull ClassInfo getDeclaredClass() {
        return declared;
    }

    @Override
    public int getModifiers() {
        return modifiers;
    }

    @Override
    public @NotNull String getName() {
        return "<init>";
    }

    @Override
    public String toString() {
        return MethodInfoImpl.toString0("<init>", this);
    }
}
