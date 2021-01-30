/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-command.test/Cmd2.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package ex0.cmd;

import io.github.karlatemp.mxlib.command.annoations.*;

import java.io.PrintStream;
import java.util.List;

@MCommand
public class Cmd2 {
    @MCommandHandle
    public void handle(
            @MSender PrintStream sender, // The command sender with known type
            @MLabel String commandLabel,
            @MParameter(name = "arg") String arg,
            @MArguments List<String> argumentLine, // List of remaining parameters
            @MArguments(full = true) List<String> commandLine, // Command parameters for executing sub command
            @MArguments(full = true, allLine = true) List<String> fullCommandLine // Full command line for executing command
    ) {
        sender.println("Hi! commandLabel    = " + commandLabel);
        sender.println("arg                 = " + arg);
        sender.println("Argument line       = " + argumentLine);
        sender.println("Command  line       = " + commandLine);
        sender.println("Command  line(full) = " + fullCommandLine);
    }
}
