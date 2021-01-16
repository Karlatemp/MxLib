/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-command.main/PrintStreamProvider.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.command;

import io.github.karlatemp.mxlib.utils.StringBuilderFormattable;

import java.io.PrintStream;
import java.util.logging.Level;

public class PrintStreamProvider extends io.github.karlatemp.mxlib.command.BaseCommandProvider {
    public PrintStreamProvider(CommandProvider parent) {
        super(parent);
    }

    @Override
    public Object resolveSender(Object sender, Class<?> toClass) {
        if (toClass == null || toClass.isInstance(sender)) {
            if (sender instanceof PrintStream)
                return sender;
        }
        return null;
    }

    @Override
    public void senderNotResolve(Object sender, Class<?> toClass) {
        ((PrintStream) sender).println("Cannot resolve to " + toClass);
    }

    @Override
    public void sendMessage(Level level, Object sender, String message) {
        ((PrintStream) sender).println(message);
    }

    @Override
    public void sendMessage(Level level, Object sender, StringBuilderFormattable message) {
        ((PrintStream) sender).println(message.to_string());
    }
}
