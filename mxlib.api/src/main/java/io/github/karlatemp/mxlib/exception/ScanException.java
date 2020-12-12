/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: ScanException.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

package io.github.karlatemp.mxlib.exception;

public class ScanException extends Exception{
    public ScanException() {
    }

    public ScanException(String message) {
        super(message);
    }

    public ScanException(String message, Throwable cause) {
        super(message, cause);
    }

    public ScanException(Throwable cause) {
        super(cause);
    }
}
