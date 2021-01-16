/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-spigot.main/TitleApi.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.spigot;

import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface TitleApi {
    public void sendActionBar(
            @NotNull Player player,
            @NotNull BaseComponent... components
    );

    public void setTabTitle(
            @NotNull Player player,
            @NotNull BaseComponent header,
            @NotNull BaseComponent footer
    );
}
