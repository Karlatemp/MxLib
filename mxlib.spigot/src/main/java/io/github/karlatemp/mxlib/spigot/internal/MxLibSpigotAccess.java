package io.github.karlatemp.mxlib.spigot.internal;

import io.github.karlatemp.mxlib.bean.IBeanManager;
import org.bukkit.plugin.java.JavaPlugin;

public interface MxLibSpigotAccess {
    public IBeanManager getPluginBeanManager(JavaPlugin plugin);
}
