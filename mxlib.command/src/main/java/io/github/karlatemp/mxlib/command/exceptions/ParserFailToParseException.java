/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-command.main/ParserFailToParseException.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
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
