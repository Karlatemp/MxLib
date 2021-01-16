/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-nbt.main/TranslatableException.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.nbt.util;

public class TranslatableException extends RuntimeException {
    private final TranslatableMessage message;

    public TranslatableException(String message, Object... params) {
        super(message);
        this.message = new TranslatableMessage(message, params);
    }

    public TranslatableMessage getTranslatableMessage() {
        return message;
    }

    public TranslatableException(String message, Throwable cause, Object... params) {
        super(message, cause);
        this.message = new TranslatableMessage(message);
    }

    public String getLocalizedMessage() {
        return message.getMessage();
    }
}
