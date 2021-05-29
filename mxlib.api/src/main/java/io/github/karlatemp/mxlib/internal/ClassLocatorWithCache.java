/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-api.main/ClassLocatorWithCache.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.internal;

import io.github.karlatemp.mxlib.utils.ClassLocator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.URL;
import java.nio.file.Path;
import java.util.WeakHashMap;

public class ClassLocatorWithCache implements ClassLocator {
    private final ClassLocator cl;
    private final WeakHashMap<Class<?>, Path> byClass = new WeakHashMap<>();
    private final WeakHashMap<String, Path> byClassname = new WeakHashMap<>();
    private final WeakHashMap<URL, Path> byUrl = new WeakHashMap<>();

    @Override
    public @Nullable Path findLocate(@NotNull String classname) {
        return byClassname.computeIfAbsent(classname, cl::findLocate);
    }

    @Override
    public @Nullable Path findLocate(@NotNull URL url) {
        return byUrl.computeIfAbsent(url, cl::findLocate);
    }

    @Override
    public @Nullable Path findLocate(@NotNull Class<?> type) {
        return byClass.computeIfAbsent(type, cl::findLocate);
    }


    public ClassLocatorWithCache(ClassLocator cl) {
        this.cl = cl;
    }

    public static ClassLocator c(ClassLocator cl) {
        if (cl == null || cl instanceof ClassLocatorWithCache) return cl;
        return new ClassLocatorWithCache(cl);
    }

    @Override
    public @Nullable ClassLoader context() {
        return cl.context();
    }

}
