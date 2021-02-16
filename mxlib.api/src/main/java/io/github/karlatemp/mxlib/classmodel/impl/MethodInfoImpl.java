/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-api.main/MethodInfoImpl.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.classmodel.impl;

import io.github.karlatemp.mxlib.classmodel.ClassInfo;
import io.github.karlatemp.mxlib.classmodel.ExecutableInfo;
import io.github.karlatemp.mxlib.classmodel.MethodInfo;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

@ApiStatus.Internal
@SuppressWarnings("DuplicatedCode")
public class MethodInfoImpl implements MethodInfo {
    private final String name;
    private final ClassInfo declared;
    private final ClassInfo returnType;
    private final List<ClassInfo> arguments;
    private final List<ClassInfo> throwsTypes;
    private final int modifiers;

    public MethodInfoImpl(
            @NotNull String name,
            int modifiers,
            @NotNull ClassInfo declared,
            @NotNull ClassInfo returnType,
            @NotNull List<ClassInfo> arguments,
            @NotNull List<ClassInfo> throwsTypes
    ) {
        this.name = name;
        this.declared = declared;
        this.returnType = returnType;
        this.arguments = arguments;
        this.throwsTypes = throwsTypes;
        this.modifiers = modifiers;
    }

    static String toString0(String func, ExecutableInfo this0) {
        int modifiers = this0.getModifiers();

        StringBuilder builder = new StringBuilder();
        if (Modifier.isPublic(modifiers)) {
            builder.append("public ");
        } else if (Modifier.isProtected(modifiers)) {
            builder.append("protected ");
        } else if (Modifier.isPrivate(modifiers)) {
            builder.append("private ");
        } else {
            builder.append("package-private ");
        }
        if (Modifier.isAbstract(modifiers)) {
            builder.append("abstract ");
        } else {
            if (Modifier.isStatic(modifiers)) {
                builder.append("static ");
            }
            if (Modifier.isFinal(modifiers)) {
                builder.append("final ");
            }
            if (Modifier.isNative(modifiers)) {
                builder.append("native ");
            }
        }
        if ((modifiers & 0x1000) != 0) {
            builder.append("synthetic ");
        }
        if (Modifier.isSynchronized(modifiers)) {
            builder.append("synchronized ");
        }

        builder.append(this0.getReturnType().getName()).append(' ');
        builder.append(func);
        builder.append('(');
        {
            Iterator<ClassInfo> iterator = this0.getArgumentTypes().iterator();
            if (iterator.hasNext()) {
                builder.append(iterator.next().getName());
            }
            while (iterator.hasNext()) {
                builder.append(", ").append(iterator.next().getName());
            }
        }
        builder.append(')');
        {
            Iterator<ClassInfo> iterator = this0.getThrows().iterator();
            if (iterator.hasNext()) {
                builder.append(" throws ").append(iterator.next().getName());
                while (iterator.hasNext()) {
                    builder.append(", ").append(iterator.next().getName());
                }
            }
        }

        return builder.toString();
    }

    @Override
    public String toString() {
        return toString0(name, this);
    }

    @Override
    public @NotNull List<MethodInfo> getOverrides() {
        if (Modifier.isStatic(modifiers)) return Collections.emptyList();

        List<MethodInfo> result = new ArrayList<>();
        for (ClassInfo interfaceInfo : declared.getInterfaces()) {
            for (MethodInfo met : interfaceInfo.getDeclaredMethods()) {
                if (Modifier.isStatic(met.getModifiers())) continue;
                if (met.getName().equals(getName())) {
                    if (met.getReturnType().equals(returnType)) {
                        if (met.getArgumentTypes().equals(arguments)) {
                            result.add(met);
                        }
                    }
                }
            }
        }

        ClassInfo info0 = declared.getSuperclass();
        while (info0 != null) {
            for (MethodInfo met : info0.getDeclaredMethods()) {
                if (Modifier.isStatic(met.getModifiers())) continue;
                if (met.getName().equals(getName())) {
                    if (met.getReturnType().equals(returnType)) {
                        if (met.getArgumentTypes().equals(arguments)) {
                            result.add(met);
                        }
                    }
                }
            }
            info0 = info0.getSuperclass();
        }
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MethodInfoImpl that = (MethodInfoImpl) o;

        if (modifiers != that.modifiers) return false;
        if (!name.equals(that.name)) return false;
        if (!declared.equals(that.declared)) return false;
        if (!returnType.equals(that.returnType)) return false;
        if (!arguments.equals(that.arguments)) return false;
        return throwsTypes.equals(that.throwsTypes);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + declared.hashCode();
        result = 31 * result + returnType.hashCode();
        result = 31 * result + arguments.hashCode();
        result = 31 * result + throwsTypes.hashCode();
        result = 31 * result + modifiers;
        return result;
    }

    @Override
    public @NotNull ClassInfo getDeclaredClass() {
        return declared;
    }

    @Override
    public @NotNull ClassInfo getReturnType() {
        return returnType;
    }

    @Override
    public @NotNull List<ClassInfo> getArgumentTypes() {
        return arguments;
    }

    @Override
    public @NotNull List<ClassInfo> getThrows() {
        return throwsTypes;
    }

    @Override
    public int getModifiers() {
        return modifiers;
    }

    @Override
    public @NotNull String getName() {
        return name;
    }
}
