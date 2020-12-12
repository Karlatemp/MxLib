/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: DefaultCommands.java@author: karlatemp@vip.qq.com: 2019/12/29 下午2:51@version: 2.0
 */

package io.github.karlatemp.mxlib.command;

import io.github.karlatemp.mxlib.command.internal.Tools;
import com.google.common.collect.ImmutableMap;
import org.jetbrains.annotations.NotNull;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;

public class DefaultCommands implements io.github.karlatemp.mxlib.command.ICommands {
    private final String name;
    private final String permission;
    private final String permissionMessage;
    private final CommandProvider provider;
    private final String description;
    private final TabCompileMode tabCompileMode;

    @Override
    public String description() {
        return description;
    }

    public DefaultCommands(String name, String permission,
                           String permissionMessage,
                           String description,
                           CommandProvider provider) {
        this(name, permission, permissionMessage, description, provider, TabCompileMode.CONTAINS);
    }

    public DefaultCommands(String name, String permission,
                           String permissionMessage,
                           String description,
                           CommandProvider provider,
                           TabCompileMode tabCompileMode) {
        this.name = name;
        this.permission = permission;
        this.permissionMessage = permissionMessage;
        this.description = description;
        this.provider = provider;
        this.tabCompileMode = tabCompileMode;
    }

    protected Map<String, ICommand> commands = new HashMap<>();
    protected String helpInd = "-?";

    @Override
    public void invoke(Object sender, String label, @NotNull List<String> arguments, @NotNull List<String> fillArguments) {
        sender = provider.resolveSender(sender, null);
        if (sender == null) {
            throw new IllegalArgumentException("Cannot resolve sender");
        }
        if (provider.hasPermission(sender, getPermission())) {
            String path = String.join(" ", Tools.getInvokePath(arguments, fillArguments));
            if (arguments.isEmpty()) {
                provider.translate(Level.WARNING, sender, "command.no.argument", label, path);
                // provider.sendMessage(Level.WARNING, sender, "Please type \"" + label + " help\" for help.");
            } else {
                if (arguments.get(0).equals(helpInd)) {
                    if (Tools.processHelp(helpInd, arguments, fillArguments, sender, provider, this, label)) {
                        return;
                    } /* else throw new RuntimeException("??????????????");*/
                }
                String sub = arguments.get(0);
                ICommand cmd = getSubCommand(sub);
                if (cmd == null) {
                    if (Tools.processHelp(helpInd, arguments, fillArguments, sender, provider, this, label)) {
                        return;
                    }
                    provider.translate(Level.WARNING, sender, "command.not.found", label, sub, path);
                } else {
                    arguments.remove(0);
                    cmd.invoke(sender, label, arguments, fillArguments);
                }
            }
        } else {
            provider.noPermission(sender, this);
        }
    }

    @Override
    public ICommand getSubCommand(String name) {
        return commands.get(name.toLowerCase());
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getPermission() {
        return permission;
    }

    @Override
    public String usage() {
        return "";
    }

    @Override
    public String noPermissionMessage() {
        return permissionMessage;
    }

    @Override
    public void tabCompile(Object sender, @NotNull List<String> result, @NotNull List<String> fillArguments, @NotNull List<String> args) {
        sender = provider.resolveSender(sender, null);
        if (sender != null) {
            Object atomicSender = sender;
            if (!args.isEmpty()) {
                if (args.size() == 1) {
                    String test = args.remove(0).toLowerCase();
                    commands.entrySet().stream().map(entry -> {
                        final ICommand value = entry.getValue();
                        final String key = entry.getKey();
                        if (provider.hasPermission(atomicSender, value.getPermission())) {
                            switch (tabCompileMode) {
                                case CONTAINS:
                                    if (!key.contains(test)) return null;
                                    break;
                                case NONE:
                                    return null;
                                case ENDS_WITH:
                                    if (!key.endsWith(test)) return null;
                                    break;
                                case STARTS_WITH:
                                    if (!key.startsWith(test)) return null;
                                    break;
                                default:
                                    throw new UnsupportedOperationException("Unknown Tab Compile Type: " + tabCompileMode);
                            }
                            return key;
                        }
                        return null;
                    }).filter(Objects::nonNull)
                            .sorted((s1, s2) -> {
                                if (tabCompileMode == TabCompileMode.CONTAINS || tabCompileMode == TabCompileMode.ENDS_WITH) {
                                    boolean a = s1.startsWith(test);
                                    boolean b = s2.startsWith(test);
                                    if (a | b) {
                                        if (a) {
                                            if (!b) {
                                                return -1;
                                            }
                                        } else return 1;
                                    }
                                }
                                return s1.compareTo(s2);
                            }).forEach(result::add);
                    return;
                }
                ICommand ic = getSubCommand(args.remove(0));
                if (ic != null) {
                    if (!provider.hasPermission(sender, ic.getPermission())) return;
                    ic.tabCompile(sender, result, fillArguments, args);
                }
            }
        }
    }

    @NotNull
    public Map<String, ICommand> getSubCommands() {
        return ImmutableMap.copyOf(commands);
    }

    public boolean register(@NotNull String name, @NotNull ICommand sub) {
        commands.put(name.toLowerCase(), sub);
        return true;
    }

    public void dump(String prefix, PrintStream stream) {
        for (Map.Entry<String, ICommand> ic : commands.entrySet()) {
            stream.append(prefix).append(ic.getKey()).append(" = ").println(ic.getValue());
            if (ic.getValue() instanceof DefaultCommands) {
                ((DefaultCommands) ic.getValue()).dump(prefix + '\t', stream);
            }
        }
    }

    @Override
    public CommandProvider provider() {
        return provider;
    }
}
