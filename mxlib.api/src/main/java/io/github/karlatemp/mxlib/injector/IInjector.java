/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-api.main/IInjector.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.injector;

import io.github.karlatemp.mxlib.bean.IBeanManager;
import io.github.karlatemp.mxlib.exception.InjectException;
import org.jetbrains.annotations.NotNull;

public interface IInjector {
    @NotNull IBeanManager getBeanManager();

    void inject(@NotNull Class<?> klass) throws InjectException;

    <T> @NotNull T inject(@NotNull T obj) throws InjectException;
}
