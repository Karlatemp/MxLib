package io.github.karlatemp.mxlib.spigot.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Notifications {
    // PluginClassLoader
    public static final List<Consumer<Object>> PluginLoadEvent = new ArrayList<>();
    public static final List<Consumer<Object>> PluginUnLoadEvent = new ArrayList<>();
}
