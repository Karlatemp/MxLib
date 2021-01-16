/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-common.main/BeanManagers.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.common.utils;

import io.github.karlatemp.caller.CallerFinder;
import io.github.karlatemp.caller.StackFrame;
import io.github.karlatemp.mxlib.MxLib;
import io.github.karlatemp.mxlib.annotations.injector.Inject;
import io.github.karlatemp.mxlib.bean.IBeanManager;
import io.github.karlatemp.mxlib.bean.SimpleBeanManager;
import io.github.karlatemp.mxlib.common.injector.SimpleInjector;
import io.github.karlatemp.mxlib.common.injector.SimpleMethodCallerWithBeans;
import io.github.karlatemp.mxlib.common.injector.SimpleObjectCreator;
import io.github.karlatemp.mxlib.exception.ScanException;
import io.github.karlatemp.mxlib.injector.IInjector;
import io.github.karlatemp.mxlib.injector.IObjectCreator;
import io.github.karlatemp.mxlib.injector.MethodCallerWithBeans;
import io.github.karlatemp.mxlib.reflect.Reflections;
import io.github.karlatemp.mxlib.utils.ClassLocator;
import io.github.karlatemp.mxlib.utils.IJarScanner;
import io.github.karlatemp.mxlib.utils.IteratorSupplier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@SuppressWarnings({"unchecked", "rawtypes"})
public class BeanManagers {
    private static final Set<Path> registeredSets = new HashSet<>();
    private static final Lock lock = new ReentrantLock();

    public static IBeanManager newStandardManager() {
        IBeanManager beanManager = new SimpleBeanManager();
        registerStandardBeans(beanManager, Reflections.getClassLoader(CallerFinder.getCaller()));
        return beanManager;
    }

    public static void registerStandardBeans(
            @NotNull IBeanManager beanManager,
            @Nullable ClassLoader loader
    ) {
        if (loader == null) {
            loader = Reflections.getClassLoader(CallerFinder.getCaller());
        }
        SimpleClassLocator locator = new SimpleClassLocator(loader);
        beanManager.register(IInjector.class, new SimpleInjector(beanManager));
        beanManager.register(IObjectCreator.class, new SimpleObjectCreator(beanManager));
        beanManager.register(MethodCallerWithBeans.class, new SimpleMethodCallerWithBeans());
        beanManager.register(ClassLocator.class, locator);
        beanManager.register(IJarScanner.class, new SimpleJarScanner(beanManager));
    }

    public static void registerAll(@NotNull Path path) {
        ClassLoader loader;
        {
            StackFrame frame = CallerFinder.getCaller();
            if (frame != null) {
                loader = Objects.requireNonNull(frame.getClassInstance(), "CallerFinder cannot find caller")
                        .getClassLoader();
            } else {
                throw new RuntimeException("No caller frame found");
            }
        }
        registerAll(path, loader, null);
    }

    public static void registerAll(@NotNull Path path, ClassLoader loader, IBeanManager beanManager) {
        if (beanManager == null) {
            beanManager = MxLib.getBeanManager();
        }
        lock.lock();
        try {
            if (registeredSets.contains(path)) return;
            registeredSets.add(path);

            IJarScanner scanner = beanManager.getBeanNonNull(IJarScanner.class);
            IObjectCreator objectCreator = beanManager.getBeanNonNull(IObjectCreator.class);
            MethodCallerWithBeans caller = beanManager.getBeanNonNull(MethodCallerWithBeans.class);

            List<String> classes;
            try {
                classes = scanner.scan(path, new ArrayList<>());
            } catch (ScanException e) {
                MxLib.getLogger().warn("Exception in scanning path " + path, e);
                return;
            }
            for (String klass : classes) {
                InputStream i0x;
                try {
                    i0x = Files.newInputStream(path.resolve(klass.replace('.', '/') + ".class"));
                } catch (IOException ignore) {
                    continue;
                }
                ClassNode node;
                try (InputStream ixw = i0x) {
                    node = new ClassNode();
                    new ClassReader(ixw).accept(node, 0);
                } catch (Exception err) {
                    MxLib.getLogger().warn("Exception in reading class file " + path, err);
                    continue;
                }
                List<AnnotationNode> annotations = node.visibleAnnotations;
                boolean process = false;
                if (annotations != null) {
                    for (AnnotationNode an : annotations) {
                        if (an.desc.equals("Lio/github/karlatemp/mxlib/annotations/injector/Configuration;")) {
                            process = true;
                            break;
                        }
                    }
                }
                if (process) {
                    try {
                        Class<?> klassX = Class.forName(node.name.replace('/', '.'), false, loader);
                        Object instance = objectCreator.newInstance(klassX);
                        for (Method method : new IteratorSupplier<>(Reflections.allMethods(klassX)
                                .filter(Reflections.withAnnotated(Inject.class))
                                .filter(Reflections.ModifierFilter.NON_STATIC)
                                .peek(Reflections.openAccess())
                                .iterator())) {
                            Inject inject = method.getDeclaredAnnotation(Inject.class);
                            String name = inject.name();
                            if (name.equals(Inject.NAME_UNSET)) name = null;
                            Class type = inject.value();
                            if (type == Void.class) type = method.getReturnType();
                            beanManager.register(type, name, caller.callMethod(beanManager, method, instance));
                        }

                    } catch (Throwable throwable) {
                        MxLib.getLogger().warn("Exception in processing " + path, throwable);
                    }
                }
            }
        } finally {
            lock.unlock();
        }
    }
}
