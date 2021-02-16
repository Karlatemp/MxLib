/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-api.main/FieldInfoBuilderImpl.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.classmodel.builder;

import io.github.karlatemp.mxlib.classmodel.ClassInfo;
import io.github.karlatemp.mxlib.classmodel.FieldInfo;
import io.github.karlatemp.mxlib.classmodel.impl.FieldInfoImpl;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

@ApiStatus.Internal
class FieldInfoBuilderImpl implements FieldInfoBuilder {
    private String name;
    private ClassInfo d, t;
    private int modifiers;

    @Override
    public @NotNull FieldInfo result() {
        return new FieldInfoImpl(
                Objects.requireNonNull(t, "`type` not setup"),
                Objects.requireNonNull(d, "`declaredOn` not setup"),
                modifiers, name
        );
    }

    @Override
    public @NotNull FieldInfoBuilder name(@NotNull String name) {
        this.name = name;
        return this;
    }

    @Override
    public @NotNull FieldInfoBuilder type(@NotNull ClassInfo type) {
        this.t = type;
        return this;
    }

    @Override
    public @NotNull FieldInfoBuilder declaredOn(@NotNull ClassInfo type) {
        this.d = type;
        return this;
    }

    @Override
    public @NotNull FieldInfoBuilder modifiers(int modifiers) {
        this.modifiers = modifiers;
        return this;
    }
}
