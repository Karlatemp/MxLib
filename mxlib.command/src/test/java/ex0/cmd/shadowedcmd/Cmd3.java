/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-command.test/Cmd3.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package ex0.cmd.shadowedcmd;

import io.github.karlatemp.mxlib.command.annoations.MCommand;
import io.github.karlatemp.mxlib.command.annoations.MCommandHandle;
import io.github.karlatemp.mxlib.command.annoations.MSender;

import java.io.PrintStream;

/*
This sub command don't have a package with @MCommands

So we think this command is a sub command for ex0.cmd
 */
@MCommand
public class Cmd3 {
    @MCommandHandle
    public void handle(
            @MSender PrintStream sender
    ) {
        sender.println("Here is cmd3");
    }
}
