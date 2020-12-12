package io.github.karlatemp.mxlib.spigot.impl;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.github.karlatemp.mxlib.MxLib;
import io.github.karlatemp.mxlib.bean.IBeanManager;
import io.github.karlatemp.mxlib.bean.SimpleBeanManager;
import io.github.karlatemp.mxlib.command.CommandBuilder;
import io.github.karlatemp.mxlib.common.utils.BeanManagers;
import io.github.karlatemp.mxlib.common.utils.SimpleClassLocator;
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
import io.github.karlatemp.mxlib.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MxLibSpigotPlugin extends JavaPlugin {
    @SuppressWarnings("FieldCanBeLocal")
    private static Handler SYS;
    private static final Map<Path, String> MAPPING = new HashMap<>();
    private static final Map<JavaPlugin, IBeanManager> BEAN_MANAGER_MAP = new HashMap<>();
    private static final Lock BEAN_MANAGER_MAP_LOCK = new ReentrantLock();
    private static AbstractTranslator MXLIB_TRANSLATOR;

    private static class BM extends SimpleBeanManager implements MxLibSpigotAccess {
        @Override
        public IBeanManager getPluginBeanManager(JavaPlugin plugin) {
            return BEAN_MANAGER_MAP.get(plugin);
        }
    }

    public MxLibSpigotPlugin() {
        IBeanManager bm = new BM();
        BeanManagers.registerStandardBeans(bm, getClassLoader());
        MxLib.setBeanManager(bm);
        MxLib.setDataStorage(getDataFolder());
        AnsiMessageFactory mf = new AnsiMessageFactory(new SimpleClassLocator() {
            @Override
            public @Nullable Path findLocate(@NotNull String classname) {
                // String path = classname.replace('.', '/') + ".class";
                Path sup = super.findLocate(classname);
                if (sup != null) return sup;
                try {
                    Class<?> name = Class.forName(classname);
                    return findLocate(name);
                } catch (ClassNotFoundException ignore) {
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
        for (Handler h : handlers) {
            if (h instanceof ConsoleHandler) {
                root.removeHandler(h);
                SYS = h;
                break;
            }
        }
        root.addHandler(new MJdkLoggerHandler(toplevel));
        MAPPING.put(bm.getBeanNonNull(ClassLocator.class).findLocate(Bukkit.class), Bukkit.getVersion());
        bm.register(TitleApi.class, new TitleApiImpl());
        bm.register(SystemTranslator.class, new BkSysTranslator());

        ResourcePackProcessor.invoke();

        try (InputStreamReader reader = new InputStreamReader(getResource("trans.json"), StandardCharsets.UTF_8)) {
            JsonObject object = new JsonParser().parse(reader).getAsJsonObject();
            MXLIB_TRANSLATOR = new TransByObj(object);
        } catch (Exception err) {
            getLogger().log(Level.WARNING, "[MxLib] Exception in allocating new scope", err);
        }
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
            PluginClassLoaderAccess.setLoaders(loader, new PluginLoadNotifyLoader(
                    PluginClassLoaderAccess.getLoaders(loader)
            ));
        }
        Notifications.PluginLoadEvent.add(loader -> {
            JavaPlugin jp = PluginClassLoaderAccess.getPlugin(loader);
            PlLogger.inject(jp);
            MAPPING.put(PluginClassLoaderAccess.GET_FILE.apply(jp).toPath().toAbsolutePath(), jp.getDescription().getVersion());
            BEAN_MANAGER_MAP_LOCK.lock();
            try {
                IBeanManager manager = BEAN_MANAGER_MAP.get(jp);
                if (manager == null) {
                    BEAN_MANAGER_MAP.put(jp, newScope(jp));
                }
            } finally {
                BEAN_MANAGER_MAP_LOCK.unlock();
            }
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
        getLogger().info(MAPPING.toString());
    }

    private static IBeanManager newScope(JavaPlugin jp) {
        IBeanManager subScope = MxLib.getBeanManager().newSubScope();
        List<AbstractTranslator> translators = new ArrayList<>();

        // TODO: File I18n

        try (InputStream resource = jp.getResource("translates/translate.json")) {
            if (resource != null) {
                try (InputStreamReader reader = new InputStreamReader(resource, StandardCharsets.UTF_8)) {
                    JsonObject object = new JsonParser().parse(reader).getAsJsonObject();
                    translators.add(new TransByObj(object));
                }
            }
        } catch (Exception err) {
            jp.getLogger().log(Level.WARNING, "[MxLib] Exception in allocating new scope", err);
        }
        translators.add(MXLIB_TRANSLATOR);

        subScope.register(Translator.class, new AbstractTranslator.Link(translators, subScope.getBeanNonNull(SystemTranslator.class)));
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
}
