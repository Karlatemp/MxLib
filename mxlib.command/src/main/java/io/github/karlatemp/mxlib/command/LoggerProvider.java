/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * Reserved.FileName: LoggerProvider.java@author: karlatemp@vip.qq.com: 2020/1/5 上午1:37@version: 2.0
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
