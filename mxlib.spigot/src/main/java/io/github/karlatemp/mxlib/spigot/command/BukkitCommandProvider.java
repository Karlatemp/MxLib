/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * Reserved.FileName: BukkitCommandProvider.java@author: karlatemp@vip.qq.com: 2020/1/3 下午4:41@version: 2.0
 */

package io.github.karlatemp.mxlib.spigot.command;

import io.github.karlatemp.caller.CallerFinder;
import io.github.karlatemp.caller.StackFrame;
import io.github.karlatemp.mxlib.MxLib;
import io.github.karlatemp.mxlib.command.*;
import io.github.karlatemp.mxlib.spigot.internal.MxLibSpigotAccess;
import io.github.karlatemp.mxlib.translate.Translator;
import io.github.karlatemp.mxlib.utils.StringBuilderFormattable;
import io.github.karlatemp.mxlib.utils.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;
import java.util.logging.Logger;

public class BukkitCommandProvider
        extends BaseCommandProvider
        implements
        FileRequiredProvider,
        InitWithCallerProvider {
    public BukkitCommandProvider(CommandProvider parent) {
        this(parent, null);
    }

    public BukkitCommandProvider() {
        this(null);
    }

    public BukkitCommandProvider(CommandProvider provider, Translator translator, Logger logger) {
        super(provider, translator, logger);
    }

    @Override
    public void setup(StackFrame frame) {
        if (parent == null && translator == null) {
            try {
                translator = ((MxLibSpigotAccess) MxLib.getBeanManager())
                        .getPluginBeanManager(JavaPlugin.getProvidingPlugin(frame.getClassInstance()))
                        .getBeanNonNull(Translator.class);
            } catch (Throwable ignore) {
            }
        }
    }

    private /*synthetic*/ static Logger a(StackFrame p) {
        if (p != null) {
            Class<?> classInstance = p.getClassInstance();
            if (classInstance != null) {
                try {
                    return JavaPlugin.getProvidingPlugin(classInstance).getLogger();
                } catch (Exception ignored) {
                }
            }
        }
        return MxLib.getJdkLogger();
    }

    public BukkitCommandProvider(CommandProvider provider, Translator translate) {
        this(provider, translate, a(CallerFinder.getCaller()));
    }

    @Override
    public Object resolveSender(Object sender, Class<?> toClass) {
        if (!(sender instanceof CommandSender)) return null;
        if (toClass == null) {
            return sender;
        }
        if (toClass.isInstance(sender)) return sender;
        return null;
    }

    @Override
    public boolean hasPermission(Object sender, String permission) {
        if (permission == null || permission.isEmpty()) return true;
        return ((CommandSender) sender).hasPermission(permission);
    }

    @Override
    public void noPermission(Object sender, ICommand command) {
        ((CommandSender) sender).sendMessage(command.noPermissionMessage());
    }

    @Override
    public void sendMessage(Level level, Object sender, StringBuilderFormattable message) {
        sendMessage(level, sender, message.to_string());
    }

    @Override
    public void senderNotResolve(Object sender, Class<?> toClass) {
        ((CommandSender) sender).sendMessage("§cSorry, but you can't use this command.");
    }

    @Override
    public void sendMessage(Level level, Object sender, String message) {
        message = StringUtils.BkColors.replaceToBkMessage(message);
        if (level == Level.SEVERE) {
            message = "§4" + message;
        } else if (level == Level.WARNING) {
            message = "§c" + message;
        }
        if (sender instanceof Player) {
            int off = 0;
            CommandSender cs = (CommandSender) sender;
            while (true) {
                int next = message.indexOf('\n', off);
                if (next == -1) {
                    cs.sendMessage(message.substring(off));
                    break;
                } else {
                    cs.sendMessage(message.substring(off, next));
                    off = next + 1;
                }
            }
            return;
        }
        ((CommandSender) sender).sendMessage(message);
    }
}
