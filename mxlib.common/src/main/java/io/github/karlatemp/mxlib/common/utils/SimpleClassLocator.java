/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-common.main/SimpleClassLocator.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.common.utils;

import io.github.karlatemp.caller.CallerFinder;
import io.github.karlatemp.mxlib.reflect.Reflections;
import io.github.karlatemp.mxlib.utils.ClassLocator;
import io.github.karlatemp.mxlib.utils.Lazy;
import io.github.karlatemp.mxlib.utils.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.ClassReader;

import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SimpleClassLocator implements ClassLocator {
    private static final Lazy<Path> JRT_MODULES = Lazy.lazy(
            () -> FileSystems.getFileSystem(URI.create("jrt:/")).getPath("/modules")
    );

    private final ClassLoader loader;

    public SimpleClassLocator(ClassLoader loader) {
        this.loader = loader;
    }

    public SimpleClassLocator() {
        this(Reflections.getClassLoader(CallerFinder.getCaller()));
    }


    @Override
    public @Nullable ClassLoader context() {
        return loader;
    }

    @Override
    public @Nullable Path findLocate(@NotNull String classname) {
        ClassLoader loader = this.loader;
        if (loader == null) loader = ClassLoader.getSystemClassLoader();

        return findLocate(
                loader.getResource(classname.replace('.', '/') + ".class"),
                classname
        );
    }

    @Override
    public @Nullable Path findLocate(@NotNull URL url) {
        String cname = null;
        if (url.getProtocol().equals("file")) {
            try (InputStream is = url.openStream()) {
                ClassReader reader = new ClassReader(is);
                cname = reader.getClassName();
            } catch (Exception ignored) {
                return null;
            }
        }
        return findLocate(url, cname);
    }

    private @Nullable Path findLocate(URL url, String classname) {
        if (url == null) return null;

        switch (url.getProtocol()) {
            case "file": {
                Path path = Paths.get(URI.create(url.toString())).getParent();
                int count = classname.chars().map(it -> (it == '.' || it == '/') ? 1 : 0).sum();
                while (count-- > 0) path = path.getParent();
                return path;
            }
            case "jar": {
                String path = url.getFile();
                String file = StringUtils.substringBefore(path, "!/", null);
                return Paths.get(URI.create(file));
            }
            case "jrt": {
                String path = url.getPath();
                if (path == null || path.isEmpty()) return JRT_MODULES.get();
                return JRT_MODULES.get().resolve(
                        StringUtils.substringBefore(path.substring(1), "/", "")
                );
            }
        }
        return null;

    }

    @Override
    public @Nullable Path findLocate(@NotNull Class<?> type) {
        return findLocate(type.getResource(type.getSimpleName() + ".class"), type.getName());
    }
}
