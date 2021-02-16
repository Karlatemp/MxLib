/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-api.main/MethodInfoBuilderImpl.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.classmodel.builder;

import io.github.karlatemp.mxlib.classmodel.ClassInfo;
import io.github.karlatemp.mxlib.classmodel.ConstructorInfo;
import io.github.karlatemp.mxlib.classmodel.MethodInfo;
import io.github.karlatemp.mxlib.classmodel.impl.ConstructorInfoImpl;
import io.github.karlatemp.mxlib.classmodel.impl.MethodInfoImpl;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@ApiStatus.Internal
class MethodInfoBuilderImpl implements MethodInfoBuilder {
    private String name;
    private int modifier;
    private ClassInfo returnType, declaredOn;
    private List<ClassInfo> arguments, exceptions;

    private static List<ClassInfo> unmodify(List<ClassInfo> list) {
        if (list == null || list.isEmpty()) return Collections.emptyList();
        return Collections.unmodifiableList(list);
    }

    @Override
    public @NotNull MethodInfo resultMethod() {
        return new MethodInfoImpl(
                Objects.requireNonNull(name, "`name` not setup"),
                modifier,
                Objects.requireNonNull(declaredOn, "`declaredOn` not setup"),
                Objects.requireNonNull(returnType, "`returnType` not setup"),
                unmodify(arguments),
                unmodify(exceptions)
        );
    }

    @Override
    public @NotNull ConstructorInfo resultConstructor() {
        return new ConstructorInfoImpl(unmodify(arguments), unmodify(exceptions),
                Objects.requireNonNull(declaredOn, "`declaredOn` not setup"),
                modifier
        );
    }

    @Override
    public @NotNull MethodInfoBuilder name(@NotNull String name) {
        this.name = name;
        return this;
    }

    @Override
    public @NotNull MethodInfoBuilder declaredOn(@NotNull ClassInfo type) {
        this.declaredOn = type;
        return this;
    }

    @Override
    public @NotNull MethodInfoBuilder returnType(@NotNull ClassInfo type) {
        this.returnType = type;
        return this;
    }

    @Override
    public @NotNull MethodInfoBuilder arguments(ClassInfo... type) {
        return arguments(Arrays.asList(type));
    }

    @Override
    public @NotNull MethodInfoBuilder arguments(@NotNull List<ClassInfo> type) {
        this.arguments = type;
        return this;
    }

    @Override
    public @NotNull MethodInfoBuilder exceptions(ClassInfo... type) {
        return exceptions(Arrays.asList(type));
    }

    @Override
    public @NotNull MethodInfoBuilder exceptions(@NotNull List<ClassInfo> type) {
        this.exceptions = type;
        return this;
    }

    @Override
    public @NotNull MethodInfoBuilder modifiers(int modifiers) {
        this.modifier = modifiers;
        return this;
    }
}
