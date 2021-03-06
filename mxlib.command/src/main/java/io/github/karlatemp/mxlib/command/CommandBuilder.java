/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-command.main/CommandBuilder.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.command;

import io.github.karlatemp.caller.CallerFinder;
import io.github.karlatemp.caller.StackFrame;
import io.github.karlatemp.mxlib.MxLib;
import io.github.karlatemp.mxlib.bean.IBeanManager;
import io.github.karlatemp.mxlib.exception.ScanException;
import io.github.karlatemp.mxlib.reflect.Reflections;
import io.github.karlatemp.mxlib.utils.IJarScanner;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.*;
import java.util.function.Function;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Fast Create a command.
 *
 * <pre>{@code ICommand implement = new CommandBuilder()
 * .provider(new PrintStreamProvider(null))
 * .logger(Logger.getLogger("MyCommand"))
 * .ofClass(AnyCommand.class)
 * .buildCommands()}</pre>
 *
 * <pre>{@code
 * this.getCommand("my-command").setExecutor(
 *      new CommandBuilder()
 *          .provider(new BukkitCommandProvider(null))
 *          .logger(this.getLogger())
 *          .ofClass(AnyCommand.class)
 *          .ofFile(this.getFile())
 *          .end(BukkitCommandExecutor::new)
 * );
 * }</pre>
 */
public class CommandBuilder {
    private final List<Class<?>> classes = new ArrayList<>();
    private CommandProvider provider;
    private Package pck;
    private Class<?> source;
    private Logger logger;
    private File file;
    private IBeanManager beanManager;

    public CommandBuilder provider(CommandProvider provider) {
        this.provider = provider;
        return this;
    }

    public CommandBuilder logger(Logger logger) {
        this.logger = logger;
        return this;
    }

    public CommandBuilder addClasses(Collection<Class<?>> classes) {
        this.classes.addAll(classes);
        return this;
    }

    public CommandBuilder inPackage(Package package_) {
        this.pck = package_;
        return this;
    }

    public CommandBuilder ofClass(Class<?> source) {
        this.source = source;
        return this;
    }

    public CommandBuilder ofFile(File file) {
        this.file = file;
        return this;
    }

    public <T> T end(@NotNull Function<ICommand, T> ending) {
        if (provider instanceof InitWithCallerProvider)
            frame = CallerFinder.getCaller();
        return ending.apply(buildCommands());
    }

    public CommandBuilder withBeanManager(IBeanManager beanManager) {
        this.beanManager = beanManager;
        return this;
    }

    private StackFrame frame;

    public synchronized ICommand buildCommands() {
        if (pck == null) {
            pck = source.getPackage();
        }
        if (logger == null) {
            logger = Logger.getGlobal();
        }
        if (provider == null) {
            throw new NullPointerException("Must give a provider.");
        }
        if (provider instanceof FileRequiredProvider && file == null) {
            throw new IllegalArgumentException("Must give your plugin jar file location.");// When JarUpdate. Using URL Scan will lock the server.
        }
        if (provider instanceof InitWithCallerProvider) {
            if (frame == null) frame = CallerFinder.getCaller();
            ((InitWithCallerProvider) provider).setup(frame);
        }
        if (beanManager == null) {
            beanManager = provider.getBeanManager();
        }
        if (beanManager == null) {
            beanManager = MxLib.getBeanManager();
        }
        List<Class<?>> target;
        if (classes.isEmpty()) {
            List<ClassLoader> loaders = Arrays.asList(
                    Reflections.getClassLoader(CallerFinder.getCaller()),
                    Thread.currentThread().getContextClassLoader(),
                    Reflections.getClassLoader(getClass())
            );
            final IJarScanner scanner = beanManager.getBeanNonNull(IJarScanner.class);
            try {
                List<String> list;
                String pck = this.pck.getName() + ".";
                if (file == null) {
                    if (source == null) {
                        throw new IllegalArgumentException("No ClassSource. No FileSource.");
                    }
                    list = scanner.scan(source, new ArrayList<>());
                } else {
                    list = scanner.scan(file, new ArrayList<>());
                }
                target = list.stream()
                        .filter(a -> a.startsWith(pck))
                        .filter(x -> !x.endsWith("package-info"))
                        .map(cn -> {
                            for (ClassLoader loader : loaders) {
                                try {
                                    return loader.loadClass(cn);
                                } catch (NoClassDefFoundError | ClassNotFoundException ignore) {
                                }
                            }
                            logger.warning("ClassNotFound: " + cn);
                            return (Class<?>) null;
                        })
                        .filter(Objects::nonNull).collect(Collectors.toList());
            } catch (ScanException e) {
                throw new RuntimeException(e);
            }
        } else {
            target = classes;
        }
        return provider.buildCommands(pck, target);
    }
}
