package io.github.karlatemp.mxlib.spigot;

import io.github.karlatemp.mxlib.reflect.Reflections;
import io.github.karlatemp.unsafeaccessor.Root;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;

import java.io.File;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class PluginClassLoaderAccess {
    public static final Class<?> PluginClassLoader;
    private static final Field $plugin;
    private static final Field $classes;
    private static final Field $loaders;

    @SuppressWarnings({"OptionalGetWithoutIsPresent", "unchecked"})
    public static final Function<JavaPlugin, File> GET_FILE = Reflections.findMethod(
            JavaPlugin.class,
            "getFile",
            false,
            File.class
    ).map(method -> Reflections.bindToNoErr(
            Root.getTrusted().in(JavaPlugin.class),
            Reflections.mapToHandle(method),
            Function.class,
            MethodType.methodType(Object.class, Object.class),
            "apply"
    )).get();

    static {
        try {
            PluginClassLoader = Class.forName("org.bukkit.plugin.java.PluginClassLoader");
            Root.openAccess($plugin = PluginClassLoader.getDeclaredField("plugin"));
            Root.openAccess($classes = PluginClassLoader.getDeclaredField("classes"));
            Root.openAccess($loaders = JavaPluginLoader.class.getDeclaredField("loaders"));
        } catch (Exception e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public static List<?> getLoaders(JavaPluginLoader loader) {
        try {
            return (List<?>) $loaders.get(loader);
        } catch (IllegalAccessException exception) {
            throw new RuntimeException(exception);
        }
    }

    public static void setLoaders(JavaPluginLoader loader, List<?> loaders) {
        try {
            $loaders.set(loader, loaders);
        } catch (IllegalAccessException exception) {
            throw new RuntimeException(exception);
        }
    }

    public static JavaPlugin getPlugin(Object classloader) {
        try {
            return (JavaPlugin) $plugin.get(classloader);
        } catch (IllegalAccessException exception) {
            throw new RuntimeException(exception);
        }
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Class<?>> getClasses(Object classloader) {
        try {
            return (Map<String, Class<?>>) $classes.get(classloader);
        } catch (IllegalAccessException exception) {
            throw new RuntimeException();
        }
    }

}
