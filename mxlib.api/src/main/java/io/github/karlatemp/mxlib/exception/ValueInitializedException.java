/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-api.main/ValueInitializedException.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.exception;

public class ValueInitializedException extends RuntimeException {
    public ValueInitializedException(String msg) {
        super(msg);
    }

    public ValueInitializedException(String message, Throwable cause) {
        super(message, cause);
    }

    public ValueInitializedException(Throwable cause) {
        super(cause);
    }

    public ValueInitializedException() {
    }
}
