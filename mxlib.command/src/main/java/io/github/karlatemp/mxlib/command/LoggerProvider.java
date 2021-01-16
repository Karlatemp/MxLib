/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-command.main/LoggerProvider.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.command;

import io.github.karlatemp.mxlib.translate.Translator;
import io.github.karlatemp.mxlib.utils.StringBuilderFormattable;

import java.util.logging.Level;
import java.util.logging.Logger;

public class LoggerProvider extends BaseCommandProvider {
    public LoggerProvider(CommandProvider parent, Translator translator) {
        super(parent, translator);
    }

    public LoggerProvider(CommandProvider parent, Translator translator, Logger logger) {
        super(parent, translator, logger);
    }

    public LoggerProvider(CommandProvider parent) {
        super(parent);
    }

    public LoggerProvider() {
    }

    @Override
    public void senderNotResolve(Object sender, Class<?> toClass) {
        sendMessage(Level.WARNING, sender, "Cannot case to " + toClass);
    }

    @Override
    public void sendMessage(Level level, Object sender, String message) {
        ((Logger) sender).log(level, message);
    }

    @Override
    public void sendMessage(Level level, Object sender, StringBuilderFormattable message) {
        ((Logger) sender).log(level, message.to_string());
    }
}
