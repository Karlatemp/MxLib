/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-api.main/IObjectCreator.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.injector;

import io.github.karlatemp.mxlib.bean.IBeanManager;
import io.github.karlatemp.mxlib.exception.ObjectCreateException;
import org.jetbrains.annotations.NotNull;

public interface IObjectCreator {
    @NotNull IBeanManager getBeanManager();

    <T> @NotNull T newInstance(@NotNull Class<T> type) throws ObjectCreateException;
}
