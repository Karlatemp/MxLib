/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-command.test/Cmd4.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package ex0.cmd.subcmd;

import io.github.karlatemp.mxlib.command.annoations.MCommand;
import io.github.karlatemp.mxlib.command.annoations.MCommandHandle;
import io.github.karlatemp.mxlib.command.annoations.MSender;

import java.io.PrintStream;

/*
This command is store in a sub package with @MCommands

So call this command with `/... subcmd cmd4`
*/
@MCommand
public class Cmd4 {
    @MCommandHandle
    public void handle(
            @MSender PrintStream sender
    ) {
        sender.println("Here is cmd4");
    }
}
