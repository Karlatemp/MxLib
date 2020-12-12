/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: ParserFailToParseException.java@author: karlatemp@vip.qq.com: 2019/12/31 下午4:28@version: 2.0
 */

package io.github.karlatemp.mxlib.command.exceptions;

import io.github.karlatemp.mxlib.command.CommandProvider;

import java.util.logging.Level;

public class ParserFailToParseException extends Exception {
    private static final Object[] EMP = new Object[0];
    private final Object[] params;
    private final String trans;

    public ParserFailToParseException(String message, String translate, Object[] params, Throwable cause) {
        super(message, cause, true, false);
        this.trans = translate;
        this.params = params == null ? EMP : params;
    }

    public void sendToSender(CommandProvider provider, Object sender) {
        if (trans == null) {
            provider.sendMessage(Level.SEVERE, sender, toString());
        } else {
            provider.translate(Level.SEVERE, sender, trans, params);
        }
    }
}
