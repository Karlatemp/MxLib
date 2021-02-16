/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-api.main/FieldInfoImpl.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.classmodel.impl;

import io.github.karlatemp.mxlib.classmodel.ClassInfo;
import io.github.karlatemp.mxlib.classmodel.FieldInfo;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Modifier;
import java.util.Objects;

@ApiStatus.Internal
public class FieldInfoImpl implements FieldInfo {
    private final ClassInfo type;
    private final ClassInfo declared;
    private final int modifiers;
    private final String name;


    @SuppressWarnings("DuplicatedCode")
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (Modifier.isPublic(modifiers)) {
            builder.append("public ");
        } else if (Modifier.isPrivate(modifiers)) {
            builder.append("private ");
        } else if (Modifier.isProtected(modifiers)) {
            builder.append("protected ");
        } else {
            builder.append("package-private ");
        }
        if (Modifier.isStatic(modifiers)) {
            builder.append("static ");
        }
        if (Modifier.isFinal(modifiers)) {
            builder.append("final ");
        }
        if ((modifiers & 0x1000) != 0) {
            builder.append("synthetic ");
        }
        if ((modifiers & 0x0040) != 0) {
            builder.append("volatile ");
        }
        if ((modifiers & 0x0080) != 0) {
            builder.append("transient ");
        }

        builder.append(type.getName()).append(' ');
        builder.append(name);

        return builder.toString();
    }

    public FieldInfoImpl(
            @NotNull ClassInfo type,
            @NotNull ClassInfo declared,
            int modifiers,
            String name
    ) {
        this.type = type;
        this.declared = declared;
        this.modifiers = modifiers;
        this.name = name;
    }

    @Override
    public @NotNull ClassInfo getType() {
        return type;
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
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FieldInfoImpl fieldInfo = (FieldInfoImpl) o;

        if (modifiers != fieldInfo.modifiers) return false;
        if (!Objects.equals(type, fieldInfo.type)) return false;
        if (!Objects.equals(declared, fieldInfo.declared)) return false;
        return Objects.equals(name, fieldInfo.name);
    }

    @Override
    public int hashCode() {
        int result = type.hashCode();
        result = 31 * result + declared.hashCode();
        result = 31 * result + modifiers;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}
