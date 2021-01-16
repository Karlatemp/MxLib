/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-api.main/IJarScanner.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.utils;

import io.github.karlatemp.mxlib.exception.ScanException;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.util.List;

/**
 * 类搜索器，在BeanManager获取
 */
public interface IJarScanner {
    @Contract
    @NotNull
    List<String> scan(@NotNull File file, @NotNull List<String> list) throws ScanException;

    @Contract
    @NotNull
    List<String> scan(@NotNull Class<?> c, @NotNull List<String> list) throws ScanException;

    @Contract
    @NotNull
    List<String> scan(@NotNull Path path, @NotNull List<String> list) throws ScanException;

    @Contract
    @NotNull
    List<String> scan(@NotNull URL url, @NotNull List<String> list) throws ScanException;
}
