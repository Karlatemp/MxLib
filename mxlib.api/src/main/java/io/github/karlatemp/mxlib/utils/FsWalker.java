/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-api.main/FsWalker.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.utils;

import org.jetbrains.annotations.NotNull;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.stream.Stream;

/**
 * File System walker
 */
public abstract class FsWalker implements Closeable {
    protected FileSystem fs;
    protected boolean closeFs;

    public FsWalker(FileSystem fs, boolean closeFs) {
        this.fs = fs;
        this.closeFs = closeFs;
    }

    protected void ensureFsOpen() throws IOException {
        if (fs == null) throw new IOException("Fs Walker closed");
    }

    public FileSystem getFs() {
        return fs;
    }

    public abstract @NotNull Stream<Path> stream() throws IOException;

    public abstract @NotNull Path relativize(@NotNull Path path);

    @Override
    public void close() throws IOException {
        if (fs == null) return;
        if (closeFs) {
            fs.close();
        }
        fs = null;
    }

    public static @NotNull FsWalker newWalker(Path path) {
        if (path.getFileSystem() == FileSystems.getDefault()) {
            File file = path.toFile();
            if (file.isDirectory()) {
                return new WalkerImpl(path);
            }
            try {
                return new WalkerImpl(
                        FileSystems.newFileSystem(URI.create("zip:" + file.toURI()), Collections.emptyMap())
                                .getPath("/")
                );
            } catch (Exception ignore) {
            }
            try {
                return new WalkerImpl(
                        FileSystems.newFileSystem(URI.create("jar:" + file.toURI()), Collections.emptyMap())
                                .getPath("/")
                );
            } catch (Exception ignore) {
            }
        }
        return new WalkerImpl(path, false);
    }

    private static class WalkerImpl extends FsWalker {
        private Path start;

        public WalkerImpl(Path start) {
            super(start.getFileSystem(), start.getFileSystem() != FileSystems.getDefault());
            this.start = start;
        }

        public WalkerImpl(Path start, boolean clsoeFs) {
            super(start.getFileSystem(), clsoeFs);
            this.start = start;
        }

        @Override
        public void close() throws IOException {
            super.close();
            start = null;
        }

        @Override
        public @NotNull Stream<Path> stream() throws IOException {
            ensureFsOpen();
            return Files.walk(start);
        }

        @Override
        public @NotNull Path relativize(@NotNull Path path) {
            return start.relativize(path);
        }
    }
}
