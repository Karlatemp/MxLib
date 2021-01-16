/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-api.main/ContextBean.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.bean;

import org.jetbrains.annotations.NotNull;

public interface ContextBean {
    /**
     * @return return the class of return should be same with this
     */
    @NotNull ContextBean copy(@NotNull IBeanManager newBeanManager);
}
