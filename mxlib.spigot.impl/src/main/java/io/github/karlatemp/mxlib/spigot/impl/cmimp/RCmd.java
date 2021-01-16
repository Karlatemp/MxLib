/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-spigot-impl.main/RCmd.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.spigot.impl.cmimp;

import io.github.karlatemp.mxlib.annotations.injector.AfterInject;
import io.github.karlatemp.mxlib.annotations.injector.Inject;
import io.github.karlatemp.mxlib.command.annoations.MCommand;
import io.github.karlatemp.mxlib.command.annoations.MCommandHandle;
import io.github.karlatemp.mxlib.command.annoations.MSender;
import io.github.karlatemp.mxlib.spigot.TitleApi;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

@MCommand
public class RCmd {
    @Inject
    private TitleApi titleApi;

    @MCommandHandle
    public void handle(@MSender Player sender) {
        sender.sendMessage("FAQ");
        titleApi.sendActionBar(sender, TextComponent.fromLegacyText("Test!! §cMore Test!!"));
        titleApi.setTabTitle(sender,
                new TextComponent("Header"),
                new TextComponent("Footer")
        );
    }

    @AfterInject
    private void post() {
        System.out.println("??");
        System.out.println(titleApi);
        System.out.println("??");
    }
}
