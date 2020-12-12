/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: ICommand.java@author: karlatemp@vip.qq.com: 2019/12/29 下午1:58@version: 2.0
 */

package io.github.karlatemp.mxlib.command;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public interface ICommand {
    void invoke(Object sender, String label, @NotNull List<String> arguments, @NotNull List<String> fillArguments);

    String getName();

    String getPermission();

    String noPermissionMessage();

    void tabCompile(Object sender, @NotNull List<String> result, @NotNull List<String> fillArguments, @NotNull List<String> args);

    String description();

    Map<String, CommandParameter> parameters();

    String usage();

    default boolean isCommandSet() {
        return this instanceof io.github.karlatemp.mxlib.command.ICommands;
    }

    default boolean hasCommandParameters() {
        if (isCommandSet()) return false;
        return !parameters().isEmpty();
    }

    CommandProvider provider();
}
