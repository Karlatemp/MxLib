/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-api.main/MethodInfoBuilder.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.classmodel.builder;

import io.github.karlatemp.mxlib.classmodel.ClassInfo;
import io.github.karlatemp.mxlib.classmodel.ConstructorInfo;
import io.github.karlatemp.mxlib.classmodel.MethodInfo;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface MethodInfoBuilder {
    static MethodInfoBuilder newBuilder() {
        return new MethodInfoBuilderImpl();
    }

    @NotNull MethodInfo resultMethod();

    @NotNull ConstructorInfo resultConstructor();

    @NotNull MethodInfoBuilder name(@NotNull String name);

    @NotNull MethodInfoBuilder declaredOn(@NotNull ClassInfo type);

    @NotNull MethodInfoBuilder returnType(@NotNull ClassInfo type);

    @NotNull MethodInfoBuilder arguments(ClassInfo... type);

    @NotNull MethodInfoBuilder arguments(@NotNull List<ClassInfo> type);

    @NotNull MethodInfoBuilder exceptions(ClassInfo... type);

    @NotNull MethodInfoBuilder exceptions(@NotNull List<ClassInfo> type);

    @NotNull MethodInfoBuilder modifiers(int modifiers);
}
