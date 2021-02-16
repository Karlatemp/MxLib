/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-api.main/MemberInfo.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.classmodel;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public interface MemberInfo extends ModifierInfo, Nameable {
    @Contract(pure = true)
    @NotNull ClassInfo getDeclaredClass();
}
