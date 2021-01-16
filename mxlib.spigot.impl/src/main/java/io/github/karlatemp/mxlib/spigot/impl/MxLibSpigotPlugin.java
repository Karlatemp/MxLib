/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-spigot-impl.main/MxLibSpigotPlugin.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.spigot.impl;

import io.github.karlatemp.mxlib.MxLib;
import io.github.karlatemp.mxlib.annotations.MPlugin;
import io.github.karlatemp.mxlib.annotations.injector.Configuration;
import io.github.karlatemp.mxlib.bean.IBeanManager;
import io.github.karlatemp.mxlib.bean.SimpleBeanManager;
import io.github.karlatemp.mxlib.command.CommandBuilder;
import io.github.karlatemp.mxlib.common.utils.BeanManagers;
import io.github.karlatemp.mxlib.common.utils.SimpleClassLocator;
import io.github.karlatemp.mxlib.common.utils.ToolkitCommon;
import io.github.karlatemp.mxlib.logger.*;
import io.github.karlatemp.mxlib.logger.renders.PrefixedRender;
import io.github.karlatemp.mxlib.logger.renders.SimpleRender;
import io.github.karlatemp.mxlib.logger.utils.JdkLoggerUtils;
import io.github.karlatemp.mxlib.spigot.PluginClassLoaderAccess;
import io.github.karlatemp.mxlib.spigot.TitleApi;
import io.github.karlatemp.mxlib.spigot.command.BukkitCommandExecutor;
import io.github.karlatemp.mxlib.spigot.command.BukkitCommandProvider;
import io.github.karlatemp.mxlib.spigot.impl.cmimp.RCmd;
import io.github.karlatemp.mxlib.spigot.internal.MxLibSpigotAccess;
import io.github.karlatemp.mxlib.translate.AbstractTranslator;
import io.github.karlatemp.mxlib.translate.SystemTranslator;
import io.github.karlatemp.mxlib.translate.Translator;
import io.github.karlatemp.mxlib.utils.ClassLocator;
import io.github.karlatemp.mxlib.utils.IteratorSupplier;
import io.github.karlatemp.mxlib.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;

import java.io.InputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class MxLibSpigotPlugin extends JavaPlugin {
    private static final Map<Path, String> MAPPING = new HashMap<>();
    private static final Map<JavaPlugin, IBeanManager> BEAN_MANAGER_MAP = new HashMap<>();
    private static final Lock BEAN_MANAGER_MAP_LOCK = new ReentrantLock();
    static AbstractTranslator MXLIB_TRANSLATOR;

    private static class BM extends SimpleBeanManager implements MxLibSpigotAccess {
        @Override
        public IBeanManager getPluginBeanManager(JavaPlugin plugin) {
            return BEAN_MANAGER_MAP.get(plugin);
        }
    }

    private static void setup(JavaPlugin jp) {
        if (jp.getClass().getDeclaredAnnotation(MPlugin.class) == null) return;
        final IBeanManager scope;
        try {
            IBeanManager manager = BEAN_MANAGER_MAP.get(jp);
            if (manager == null) {
                BEAN_MANAGER_MAP.put(jp, scope = newScope(jp));
            } else {
                scope = manager;
            }
        } finally {
            BEAN_MANAGER_MAP_LOCK.unlock();
        }
        System.out.println(scope);
        ClassLoader classLoader = jp.getClass().getClassLoader();
        // Load configurations
        try (ZipFile zip = new ZipFile(PluginClassLoaderAccess.GET_FILE.apply(jp))) {
            List<ClassNode> classTree = new ArrayList<>();
            for (ZipEntry entry : new IteratorSupplier<>(ToolkitCommon.asIterator(zip.entries()))) {
                if (entry.getName().endsWith(".class")) {
                    ClassNode node = new ClassNode();
                    try (InputStream is = zip.getInputStream(entry)) {
                        new ClassReader(is).accept(node, 0);
                        classTree.add(node);
                    } catch (Throwable throwable) {
                        jp.getLogger().log(Level.WARNING, "Exception in reading " + entry.getName());
                    }
                }
            }
            Function<String, Predicate<ClassNode>> withAnno = key -> {
                String kx = "L" + key.replace('.', '/') + ";";
                return node -> node.visibleAnnotations.stream().anyMatch(anno -> anno.desc.equals(kx));
            };
            for (ClassNode node : new IteratorSupplier<>(classTree.stream()
                    .filter(withAnno.apply(Configuration.class.getName()))
                    .iterator())) {
                Class<?> klass = Class.forName(node.name.replace('/', '.'), true, classLoader);

            }

        } catch (Exception exception) {
            jp.getLogger().log(Level.SEVERE, null, exception);
        }
    }

    public MxLibSpigotPlugin() {
        IBeanManager bm = new BM();
        BeanManagers.registerStandardBeans(bm, getClassLoader());
        MxLib.setBeanManager(bm);
        MxLib.setDataStorage(getDataFolder());
        AnsiMessageFactory mf = new AnsiMessageFactory(new SimpleClassLocator() {
            private void record(Throwable throwable) {
                Bukkit.getLogger().log(Level.SEVERE, "[MxLib] Error in locating", throwable);
            }

            @Override
            public @Nullable Path findLocate(@NotNull String classname) {
                // String path = classname.replace('.', '/') + ".class";
                Path sup = super.findLocate(classname);
                if (sup != null) return sup;
                try {
                    Class<?> name = Class.forName(classname);
                    return findLocate(name);
                } catch (ClassNotFoundException z) {
                    if (z.getCause() != null) {
                        record(z);
                    }
                } catch (Throwable throwable) {
                    record(throwable);
                }
                return null;
            }
        }) {
            @Override
            protected String findVersion(Path path) {
                String ver = MAPPING.get(path);
                if (ver != null) {
                    return ver;
                }
//                return path.toString();
                return super.findVersion(path);
            }
        };
        PrefixedRender renderX = new PrefixedRender(
                new SimpleRender(mf),
                PrefixedRender.PrefixSupplier.constant(StringUtils.BkColors._B)
                        .plus(StringUtils.BkColors.RESET + "[" + StringUtils.BkColors._6)
                        .plus(PlLoggerName.I.aligned(PrefixedRender.AlignedSupplier.AlignType.LEFT))
                        .plus(StringUtils.BkColors.RESET + "] [" + StringUtils.BkColors._B)
                        .plus(PrefixedRender.PrefixSupplier.loggerLevel().aligned(PrefixedRender.AlignedSupplier.AlignType.CENTER))
                        .plus(StringUtils.BkColors.RESET + "] ")
        );
        Consumer<StringBuilder> printer = System.out::println;
        MLogger toplevel = new AwesomeLogger.Awesome("Root", printer, renderX);
        MxLib.setLogger(toplevel);
        MxLib.setJdkLogger(new MJdkLogger(toplevel));
        MxLib.setLoggerFactory(name -> new AwesomeLogger.Awesome(name, printer, renderX));
        Logger root = JdkLoggerUtils.ROOT.get();
        Handler[] handlers = root.getHandlers();
        MJdkLoggerHandler mJdkLoggerHandler = new MJdkLoggerHandler(toplevel);
        Notifications.MxLibUnloadHooks.add(() -> root.removeHandler(mJdkLoggerHandler));
        for (Handler h : handlers) {
            if (h instanceof ConsoleHandler) {
                root.removeHandler(h);
                Notifications.MxLibUnloadHooks.add(() -> root.addHandler(h));
                break;
            }
        }
        root.addHandler(mJdkLoggerHandler);
        MAPPING.put(bm.getBeanNonNull(ClassLocator.class).findLocate(Bukkit.class), Bukkit.getVersion());
        bm.register(TitleApi.class, new TitleApiImpl());
        bm.register(SystemTranslator.class, new BkSysTranslator());

        ResourcePackProcessor.invoke();
        MXLIB_TRANSLATOR = TransLoader.loadTranslate(this);
    }

    @Override
    public void onLoad() {
        PlLogger.inject(this);
        for (Plugin p : Bukkit.getPluginManager().getPlugins()) {
            if (p instanceof JavaPlugin) {
                JavaPlugin jp = (JavaPlugin) p;
                PlLogger.inject(jp);
                BEAN_MANAGER_MAP.put(jp, newScope(jp));
                MAPPING.put(PluginClassLoaderAccess.GET_FILE.apply(jp).toPath().toAbsolutePath(), jp.getDescription().getVersion());
            }
        }
        BeanManagers.registerAll(getFile().toPath(), getClassLoader(), MxLib.getBeanManager());
        {
            JavaPluginLoader loader = (JavaPluginLoader) getPluginLoader();
            List<?> loaders = PluginClassLoaderAccess.getLoaders(loader);
            PluginClassLoaderAccess.setLoaders(loader, new PluginLoadNotifyLoader(
                    loaders
            ));
            Notifications.MxLibUnloadHooks.add(() -> PluginClassLoaderAccess.setLoaders(loader, loaders));
        }
        Notifications.PluginLoadEvent.add(loader -> {
            JavaPlugin jp = PluginClassLoaderAccess.getPlugin(loader);
            PlLogger.inject(jp);
            MAPPING.put(PluginClassLoaderAccess.GET_FILE.apply(jp).toPath().toAbsolutePath(), jp.getDescription().getVersion());
            BEAN_MANAGER_MAP_LOCK.lock();
            setup(jp);
        });
        Notifications.PluginUnLoadEvent.add(loader -> {
            JavaPlugin jp = PluginClassLoaderAccess.getPlugin(loader);
            MAPPING.remove(PluginClassLoaderAccess.GET_FILE.apply(jp).toPath().toAbsolutePath());
            BEAN_MANAGER_MAP_LOCK.lock();
            try {
                IBeanManager manager = BEAN_MANAGER_MAP.get(jp);
                if (manager != null) {
                    BEAN_MANAGER_MAP.remove(jp, manager);
                }
            } finally {
                BEAN_MANAGER_MAP_LOCK.unlock();
            }
        });
    }

    private static IBeanManager newScope(JavaPlugin jp) {
        IBeanManager subScope = MxLib.getBeanManager().newSubScope();
        subScope.register(Translator.class, TransLoader.loadTranslate(jp));
        return subScope;
    }

    @Override
    public void onEnable() {
        getCommand("mxlib").setExecutor(new CommandBuilder()
                .provider(new BukkitCommandProvider())
                .ofFile(getFile())
                .ofClass(RCmd.class)
                .end(BukkitCommandExecutor::new));
    }

    @Override
    public void onDisable() {
        Notifications.MxLibUnloadHooks.removeIf(it -> {
            it.run();
            return true;
        });
    }
}
