/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-api.main/ObjectCreateException.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.exception;

public class ObjectCreateException extends RuntimeException {
    public ObjectCreateException() {
    }

    public ObjectCreateException(String message) {
        super(message);
    }

    public ObjectCreateException(String message, Throwable cause) {
        super(message, cause);
    }

    public ObjectCreateException(Throwable cause) {
        super(cause);
    }

    protected ObjectCreateException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
