/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * Reserved.FileName: BukkitCommandExecutor.java@author: karlatemp@vip.qq.com: 2020/1/3 下午11:18@version: 2.0
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
