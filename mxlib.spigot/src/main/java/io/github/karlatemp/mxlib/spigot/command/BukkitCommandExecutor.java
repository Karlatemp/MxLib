/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-spigot.main/BukkitCommandExecutor.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.spigot.command;

import io.github.karlatemp.mxlib.command.ICommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A Bukkit Executor. Make MXLib Command to BukkitCommand
 *
 * @see CommandExecutor
 * @see TabCompleter
 * @see org.bukkit.command.PluginCommand#setExecutor(CommandExecutor)
 */
public class BukkitCommandExecutor implements CommandExecutor, TabCompleter {
    private final ICommand cmd;
    public static final String PLAYER_TAB_COMPLETE = "$({PLAYER_LIST})";

    public BukkitCommandExecutor(@NotNull ICommand cmd) {
        this.cmd = cmd;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        cmd.invoke(commandSender, s, new ArrayList<>(Arrays.asList(strings)), Arrays.asList(strings));
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        List<String> result = new ArrayList<>();
        cmd.tabCompile(commandSender, result, Arrays.asList(strings), new ArrayList<>(Arrays.asList(strings)));
        if (result.size() == 1) {
            if (result.get(0).equals(PLAYER_TAB_COMPLETE)) {
                return null;
            }
        }
        return result;
    }
}
