/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-api.main/ClassInfoBuilderImpl.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.classmodel.builder;

import io.github.karlatemp.mxlib.classmodel.ClassInfo;
import io.github.karlatemp.mxlib.classmodel.ConstructorInfo;
import io.github.karlatemp.mxlib.classmodel.FieldInfo;
import io.github.karlatemp.mxlib.classmodel.MethodInfo;
import io.github.karlatemp.mxlib.classmodel.impl.ClassInfoImpl;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Consumer;

@ApiStatus.Internal
class ClassInfoBuilderImpl implements ClassInfoBuilder {
    private ClassInfo result;
    private String name;
    private ClassInfo superType;
    private List<ClassInfo> interfaces;
    private int modifiers;
    private final List<MethodInfo> methods = new ArrayList<>();
    private final List<FieldInfo> fields = new ArrayList<>();
    private final List<ConstructorInfo> constructors = new ArrayList<>();

    private void requireNotInitialized(String type) {
        if (result != null) {
            throw new UnsupportedOperationException("Cannot change " + type + " after class info initialized");
        }
    }

    @Override
    public @NotNull ClassInfo result() {
        if (result == null) {
            if (interfaces == null) interfaces = new ArrayList<>();
            result = new ClassInfoImpl(
                    Objects.requireNonNull(name, "Class name not setup."),
                    superType,
                    Collections.unmodifiableList(methods),
                    Collections.unmodifiableList(fields),
                    Collections.unmodifiableList(constructors),
                    Collections.unmodifiableList(interfaces),
                    modifiers,
                    null
            );
        }
        return result;
    }

    @Override
    public @NotNull ClassInfoBuilder name(@NotNull String name) {
        requireNotInitialized("name");
        this.name = name;
        return this;
    }

    @Override
    public @NotNull ClassInfoBuilder modifiers(int modifiers) {
        requireNotInitialized("modifiers");
        this.modifiers = modifiers;
        return this;
    }

    @Override
    public @NotNull ClassInfoBuilder superType(ClassInfo superType) {
        requireNotInitialized("superType");
        this.superType = superType;
        return this;
    }

    @Override
    public @NotNull ClassInfoBuilder interfaces(ClassInfo... interfaces) {
        return interfaces(Arrays.asList(interfaces));
    }

    @Override
    public @NotNull ClassInfoBuilder interfaces(List<ClassInfo> interfaces) {
        if (this.interfaces == null) {
            this.interfaces = interfaces;
        } else {
            if (result != null) {
                this.interfaces.clear();
                this.interfaces.addAll(interfaces);
            } else {
                this.interfaces = interfaces;
            }
        }
        return this;
    }

    @Override
    public @NotNull ClassInfoBuilder method(@NotNull Consumer<MethodInfoBuilder> action) {
        MethodInfoBuilder builder = MethodInfoBuilder.newBuilder().declaredOn(result());
        action.accept(builder);
        methods.add(builder.resultMethod());
        return this;
    }

    @Override
    public @NotNull ClassInfoBuilder field(@NotNull Consumer<FieldInfoBuilder> action) {
        FieldInfoBuilder builder = FieldInfoBuilder.newBuilder().declaredOn(result());
        action.accept(builder);
        fields.add(builder.result());
        return this;
    }

    @Override
    public @NotNull ClassInfoBuilder constructor(@NotNull Consumer<MethodInfoBuilder> action) {
        MethodInfoBuilder builder = MethodInfoBuilder.newBuilder().declaredOn(result());
        action.accept(builder);
        constructors.add(builder.resultConstructor());
        return this;
    }
}
