/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-api.main/ClassInfoBuilder.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.classmodel.builder;

import io.github.karlatemp.mxlib.classmodel.ClassInfo;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;

public interface ClassInfoBuilder {
    static ClassInfoBuilder newBuilder() {
        return new ClassInfoBuilderImpl();
    }

    @NotNull ClassInfo result();

    @NotNull ClassInfoBuilder name(@NotNull String name);

    @NotNull ClassInfoBuilder modifiers(int modifiers);

    @NotNull ClassInfoBuilder superType(ClassInfo superType);

    @NotNull ClassInfoBuilder interfaces(ClassInfo... interfaces);

    @NotNull ClassInfoBuilder interfaces(List<ClassInfo> interfaces);

    @NotNull ClassInfoBuilder method(@NotNull Consumer<MethodInfoBuilder> action);

    @NotNull ClassInfoBuilder field(@NotNull Consumer<FieldInfoBuilder> action);

    @NotNull ClassInfoBuilder constructor(@NotNull Consumer<MethodInfoBuilder> action);
}
