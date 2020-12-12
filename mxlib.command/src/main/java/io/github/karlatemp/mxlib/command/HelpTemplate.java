/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * Reserved.FileName: HelpTemplate.java@author: karlatemp@vip.qq.com: 2020/1/4 下午1:52@version: 2.0
 */

package io.github.karlatemp.mxlib.command;

import io.github.karlatemp.mxlib.format.FormatArguments;
import io.github.karlatemp.mxlib.format.FormatTemplate;
import io.github.karlatemp.mxlib.utils.StringUtils.BkColors;
import io.github.karlatemp.mxlib.utils.Toolkit;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class HelpTemplate {
    // public static final Formatter formatter = new PunctuateFormatter("${", "}");
    public static final String LINE, EMPTY;

    static {
        char[] c = new char[100];
        Arrays.fill(c, '=');
        LINE = new String(c);
        Arrays.fill(c, ' ');
        EMPTY = new String(c);
    }


    public static final FormatTemplate
            HEADER = (StringBuilder builder, @NotNull FormatArguments arguments) -> {
        arguments.append(builder, 0);
        int spl = builder.length();
        int inserts = 50 - spl;
        if (inserts < 0) return;
        int left = inserts / 2;
        int right = inserts - left;
        builder.insert(0, '[')
                .insert(0, LINE, 0, left)
                .insert(0, BkColors._6)
                .append(']')
                .append(LINE, 0, right);
    }, PARAMETERS = (sb, arguments) -> {
        @SuppressWarnings("unchecked")
        Map<String, CommandParameter> params = (Map<String, CommandParameter>) arguments.magicValue();
        CommandProvider provider = (CommandProvider) arguments.magicValue();
        int max = 0;
        for (String p : params.keySet()) {
            max = Math.max(max, p.length());
        }
        for (Map.Entry<String, CommandParameter> parameter : params.entrySet()) {
            String n = parameter.getKey();
            sb.append(BkColors._6 + "-").append(n);
            sb.append(EMPTY, 0, max - n.length());
            sb.append(BkColors._F + ":" + BkColors._C + " ").append(
                    provider.parse_message(parameter.getValue().description())
            );
            if (parameter.getValue().require()) {
                sb.append(BkColors._C + "(require)");
            }
            sb.append('\n');
        }
    }, COMMANDS = (sb, r) -> {
        @SuppressWarnings("unchecked") Map<String, ICommand> cmds = (Map<String, ICommand>) r.magicValue();
        CommandProvider provider = (CommandProvider) r.magicValue();
        int max = 0;
        for (String p : cmds.keySet()) {
            max = Math.max(max, p.length());
        }
        for (Map.Entry<String, ICommand> parameter : cmds.entrySet()) {
            String n = parameter.getKey();
            sb.append("\n" + BkColors._6).append(n);
            sb.append(EMPTY, 0, max - n.length());
            sb.append(BkColors._F + ":" + BkColors._C + " ")
                    .append(provider.parse_message(parameter.getValue().description()));
        }
    };

    public void processHelp(
            Object sender,
            io.github.karlatemp.mxlib.command.CommandProvider provider,
            List<String> invokePath,
            List<String> args,
            String label,
            io.github.karlatemp.mxlib.command.ICommand command) {
        provider.sendMessage(Level.INFO, sender, HEADER.format(new FormatArguments.AbstractArgument() {
            public void append(@NotNull StringBuilder builder, int slot) {
                builder.append(label);
                for (String path : invokePath) {
                    builder.append(' ').append(path);
                }
            }
        }));
        provider.sendMessage(Level.INFO, sender, BkColors._6 + provider.parse_message(command.description()));
        if (command instanceof ICommands) {
            final Map<String, ICommand> subCommands = ((ICommands) command).getSubCommands();
            List<String> cmds = subCommands.keySet().stream().sorted().collect(Collectors.toList());
            if (!cmds.isEmpty()) {
                int start = 0;
                try {
                    start = Integer.parseInt(args.get(0));
                } catch (Throwable ignore) {
                }
                int pageSize = 5;
                try {
                    pageSize = Math.max(1, Integer.parseInt(args.get(1)));
                } catch (Throwable ignore) {
                }
                int pages = cmds.size() / pageSize;
                if (cmds.size() % pageSize > 0) pages++;
                if (start >= 0 && start < pages) {
                    List<String> filt = cmds.subList(start * pageSize, Math.min(cmds.size(), (start + 1) * pageSize));
                    provider.sendMessage(Level.ALL, sender, COMMANDS.format(new FormatArguments.AbstractArgument() {
                        boolean a;

                        @Override
                        public Object magicValue() {
                            if (!(a = !a)) {
                                return provider;
                            }
                            return subCommands.entrySet().stream().filter(a -> filt.contains(a.getKey())).collect(
                                    Toolkit.toMapCollector(HashMap::new)
                            );
                        }
                    }));
                }
                int st = start + 1, pg = pages;
                provider.sendMessage(Level.INFO, sender, HEADER.format(new FormatArguments.AbstractArgument() {
                    @Override
                    public void append(@NotNull StringBuilder builder, int slot) {
                        builder.append(st).append(" / ").append(pg);
                    }
                }));
            }
        } else {
            final Map<String, CommandParameter> parameters = command.parameters();
            provider.sendMessage(Level.ALL, sender, "§c" + command.usage() + "\n");
            if (parameters != null && !parameters.isEmpty()) {
                provider.sendMessage(Level.INFO, sender, PARAMETERS.format(new FormatArguments.AbstractArgument() {
                    boolean a;

                    @Override
                    public Object magicValue() {
                        return (a = !a) ? parameters : provider;
                    }
                }));
            }
        }
    }
}
