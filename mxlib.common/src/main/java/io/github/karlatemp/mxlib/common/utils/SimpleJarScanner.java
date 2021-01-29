/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-common.main/SimpleJarScanner.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.common.utils;

import io.github.karlatemp.mxlib.annotations.injector.Inject;
import io.github.karlatemp.mxlib.bean.ContextBean;
import io.github.karlatemp.mxlib.bean.IBeanManager;
import io.github.karlatemp.mxlib.exception.ScanException;
import io.github.karlatemp.mxlib.utils.ClassLocator;
import io.github.karlatemp.mxlib.utils.IJarScanner;
import io.github.karlatemp.mxlib.utils.IteratorSupplier;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

public class SimpleJarScanner implements IJarScanner, ContextBean {
    @Inject
    protected IBeanManager beanManager;

    protected ClassLocator locator() {
        return beanManager.getBeanNonNull(ClassLocator.class);
    }

    @Override
    public @NotNull SimpleJarScanner copy(@NotNull IBeanManager newBeanManager) {
        return new SimpleJarScanner(newBeanManager);
    }

    public SimpleJarScanner() {
    }

    @Inject
    public SimpleJarScanner(@Inject @NotNull IBeanManager beanManager) {
        this.beanManager = beanManager;
    }

    @Override
    public @NotNull List<String> scan(@NotNull File file, @NotNull List<String> list) throws ScanException {
        scan(file.toPath(), list);
        return list;
    }

    @Override
    public @NotNull List<String> scan(@NotNull Path path, @NotNull List<String> list) throws ScanException {
        scan0(path, list);
        return list;
    }

    protected void scan0(Path path, @NotNull List<String> list) throws ScanException {
        if (path == null) throw new ScanException("Cannot found jar location.");
        try {
            FileSystem fsToClose = null;
            try {
                Iterator<Path> iterator;
                Path root;
                Stream<Path> csStr;
                if (Files.isDirectory(path)) {
                    root = path;
                    iterator = (csStr = Files.walk(path)).iterator();
                } else {
                    fsToClose = FileSystems.newFileSystem(URI.create("jar:" + path.toUri().toString()), Collections.emptyMap());
                    iterator = (csStr = Files.walk(root = fsToClose.getPath("/"))).iterator();
                }
                for (Path p : new IteratorSupplier<>(iterator)) {

                    Path relativize = root.relativize(p);
                    String px = relativize.toString();
                    if (!Files.isDirectory(p) && px.endsWith(".class")) {
                        String pw = px.replace('\\', '.').replace('/', '.');
                        list.add(pw.substring(0, pw.length() - 6));
                    }
                }
                csStr.close();
            } finally {
                if (fsToClose != null) fsToClose.close();
            }
        } catch (IOException ioe) {
            throw new ScanException(ioe);
        }
    }

    @Override
    public @NotNull List<String> scan(@NotNull Class<?> c, @NotNull List<String> list) throws ScanException {
        scan(locator().findLocate(c), list);
        return list;
    }

    @Override
    public @NotNull List<String> scan(@NotNull URL url, @NotNull List<String> list) throws ScanException {
        scan(locator().findLocate(url), list);
        return list;
    }
}
