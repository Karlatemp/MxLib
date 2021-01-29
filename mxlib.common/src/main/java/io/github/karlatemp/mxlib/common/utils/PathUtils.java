/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-common.main/PathUtils.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.common.utils;

import org.jetbrains.annotations.Contract;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.spi.FileSystemProvider;
import java.util.function.Predicate;

public class PathUtils {
    private static final Predicate<Path> CHECK_IS_JRE = initCheckIsJre();
    private static final FileSystemProvider fileFileSystemProvider = FileSystems.getDefault().provider();

    private static Predicate<Path> initCheckIsJre() {
        Path path = new SimpleClassLocator(null).findLocate(String.class);
        assert path != null;
        FileSystemProvider provider = path.getFileSystem().provider();
        if (path.getFileSystem().provider().getScheme().equals("jrt")) {
            return p -> p.getFileSystem().provider() == provider;
        }
        return p -> p.equals(path);
    }

    @Contract(value = "null -> false", pure = true)
    public static boolean isJre(Path path) {
        if (path == null) return false;
        return CHECK_IS_JRE.test(path);
    }

    public static boolean isOsFileSystem(Path path) {
        if (path == null) return false;
        return path.getFileSystem().provider() == fileFileSystemProvider;
    }
}
