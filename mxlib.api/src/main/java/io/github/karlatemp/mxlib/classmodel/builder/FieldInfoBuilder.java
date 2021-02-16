/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-api.main/FieldInfoBuilder.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.classmodel.builder;

import io.github.karlatemp.mxlib.classmodel.ClassInfo;
import io.github.karlatemp.mxlib.classmodel.FieldInfo;
import org.jetbrains.annotations.NotNull;

public interface FieldInfoBuilder {
    static FieldInfoBuilder newBuilder() {
        return new FieldInfoBuilderImpl();
    }

    @NotNull FieldInfo result();

    @NotNull FieldInfoBuilder name(@NotNull String name);

    @NotNull FieldInfoBuilder type(@NotNull ClassInfo type);

    @NotNull FieldInfoBuilder declaredOn(@NotNull ClassInfo type);

    @NotNull FieldInfoBuilder modifiers(int modifiers);
}
