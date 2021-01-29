/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-command.main/ICommand.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.command;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.LinkedList;
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

    default void invoke(Object sender, String label, List<String> arguments) {
        invoke(sender, label, new ArrayList<>(arguments), arguments);
    }

    default List<String> tabCompile(Object sender, @NotNull List<String> args) {
        List<String> result = new LinkedList<>();
        tabCompile(sender, result, args, new ArrayList<>(args));
        return result;
    }

}
