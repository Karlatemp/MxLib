/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-api.main/ClassLocator.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.utils;

import io.github.karlatemp.mxlib.internal.ClassLocatorWithCache;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.URL;
import java.nio.file.Path;

public interface ClassLocator {
    @Nullable Path findLocate(@NotNull Class<?> type);

    @Nullable ClassLoader context();

    @Nullable Path findLocate(@NotNull String classname);

    @Nullable Path findLocate(@NotNull URL url);

    /**
     * Return the locator with caching
     *
     * @since 3.0-dev-18
     */
    static ClassLocator withCache(ClassLocator delegate) {
        return ClassLocatorWithCache.c(delegate);
    }
}
