/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-command.main/ICommands.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
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
