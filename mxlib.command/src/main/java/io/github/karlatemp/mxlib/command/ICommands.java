/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: ICommands.java@author: karlatemp@vip.qq.com: 2019/12/29 下午3:01@version: 2.0
 */

package io.github.karlatemp.mxlib.command;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Map;

public interface ICommands extends ICommand {
    ICommand getSubCommand(String name);

    boolean register(@NotNull String name, @NotNull ICommand sub);

    @NotNull
    Map<String, ICommand> getSubCommands();

    @Override
    default Map<String, CommandParameter> parameters() {
        return Collections.emptyMap();
    }
}
