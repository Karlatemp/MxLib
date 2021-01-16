/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-spigot-impl.main/Notifications.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.spigot.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Notifications {
    // PluginClassLoader
    public static final List<Consumer<Object>> PluginLoadEvent = new ArrayList<>();
    public static final List<Consumer<Object>> PluginUnLoadEvent = new ArrayList<>();
    public static final List<Runnable> MxLibUnloadHooks = new ArrayList<>();
}
