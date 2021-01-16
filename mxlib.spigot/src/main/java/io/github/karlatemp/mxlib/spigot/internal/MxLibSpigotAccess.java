/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-spigot.main/MxLibSpigotAccess.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.spigot.internal;

import io.github.karlatemp.mxlib.bean.IBeanManager;
import org.bukkit.plugin.java.JavaPlugin;

public interface MxLibSpigotAccess {
    public IBeanManager getPluginBeanManager(JavaPlugin plugin);
}
