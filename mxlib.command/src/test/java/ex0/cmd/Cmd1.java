/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-command.test/Cmd1.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package ex0.cmd;

import io.github.karlatemp.mxlib.command.CommandProvider;
import io.github.karlatemp.mxlib.command.annoations.*;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;

@MCommand // Telling MxLib this class is a command class
public class Cmd1 {
    @MCommandHandle
    public void handle(
            @MSender Object sender, // The command sender object.
            @MProvider CommandProvider provider, // The command provider, If you are writing a common command, It is useful
            @NotNull @MParameter(name = "arg0") String arg0 // The command parameter, MxLib using Unix Command Line Style
    ) {
        provider.sendMessage(Level.INFO, sender, "Hi! arg0=" + arg0);
    }
}
