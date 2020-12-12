/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * Reserved.FileName: FilesIterator.java@author: karlatemp@vip.qq.com: 2020/1/1 下午11:38@version: 2.0
 */

package io.github.karlatemp.mxlib.utils;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * A Iterator to visit file tree.
 *
 * @since 2.11
 */
public class FilesIterator implements Iterator<File>, Iterable<File> {
    private final FileDepthInfo info;
    private final boolean sd;
    private final boolean sf;

    @NotNull
    @Override
    public Iterator<File> iterator() {
        return this;
    }

    private File next;
    private boolean nexted = false;

    @Override
    public synchronized boolean hasNext() {
        if (nexted)
            return next != null;
        while (true) {
            File n = pool();
            next = n;
            boolean hasNext = n != null;
            if (hasNext) {
                if (sf && n.isFile()) {
                    continue;
                }
                if (sd && n.isDirectory()) {
                    continue;
                }
            }
            nexted = true;
            return hasNext;
        }
    }

    @Override
    public synchronized File next() {
        if (hasNext()) {
            nexted = false;
            return next;
        }
        throw new NoSuchElementException();
    }

    static class FileDepthInfo {
        int allDepth;
        File current;
        ConcurrentLinkedQueue<FileDepthInfo> children;
        FileDepthInfo parent;

        FileDepthInfo(int a, File b) {
            allDepth = a;
            current = b;
        }

        public File next() {
            FileDepthInfo loop = this;
            while (true) {
                if (loop == null) return null;
                File test = loop.current;
                int dep = loop.allDepth;
                ConcurrentLinkedQueue<FileDepthInfo> queue = loop.children;
                if (test != null && test.isFile()) {
                    loop.current = null;
                    return test;
                }
                if (queue != null) {
                    if (queue.isEmpty()) {
                        loop = loop.parent;
                        if (loop != null) loop.children.poll();
                        continue;
                    }
                    FileDepthInfo qf = queue.peek();
                    if (qf.children != null || (qf.current != null && qf.current.isDirectory())) {
                        loop = qf;
                        continue;
                    }
                    queue.poll();
                    if (qf.current == null) {
                        continue;
                    }
                    return qf.current;
                } else {
                    if (test != null) {
                        if (dep != 0 && test.isDirectory()) {
                            File[] files = test.listFiles();
                            if (files != null) {
                                ConcurrentLinkedQueue<FileDepthInfo> que = loop.children = new ConcurrentLinkedQueue<>();
                                for (File f : files) {
                                    // System.out.println("-F " + f);
                                    FileDepthInfo a = new FileDepthInfo(dep - 1, f);
                                    a.parent = loop;
                                    que.offer(a);
                                }
                            }
                        }
                    }
                }
                loop.current = null;
                return test;
            }
        }
    }

    public FilesIterator(@NotNull File dir, int maxDepth, boolean skipFile, boolean skipDir) {
        if (!dir.isDirectory()) throw new AssertionError();
        this.info = new FileDepthInfo(maxDepth, dir);
        this.sf = skipFile;
        this.sd = skipDir;
    }

    public File pool() {
        return info.next();
    }
}
