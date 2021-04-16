/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-spigot-impl.main/GameProfileTest.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.spigot.impl.cmimp;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import io.github.karlatemp.mxlib.command.annoations.MCommand;
import io.github.karlatemp.mxlib.command.annoations.MCommandHandle;
import io.github.karlatemp.mxlib.command.annoations.MSender;
import io.github.karlatemp.mxlib.spigot.PlayerHelper;
import org.bukkit.entity.Player;

import java.util.Map;

@MCommand(name = "gp")
public class GameProfileTest {
    @MCommandHandle
    public void handle(@MSender Player sender) {
        GameProfile profile = (GameProfile) PlayerHelper.getProfile(sender);
        for (Map.Entry<String, Property> entry : profile.getProperties().entries()) {
            System.out.println(entry.getKey());
            Property property = entry.getValue();
            System.out.println("> Value: " + property.getValue());
            System.out.println("> Sign: " + property.getSignature());
        }
    }
}
