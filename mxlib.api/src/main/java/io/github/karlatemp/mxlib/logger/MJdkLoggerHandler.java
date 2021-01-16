/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-api.main/MJdkLoggerHandler.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.logger;

import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class MJdkLoggerHandler extends Handler {
    private final MLogger logger;

    public MJdkLoggerHandler(MLogger logger) {
        this.logger = logger;
    }

    @Override
    public void publish(LogRecord record) {
        logger.log(record);
    }

    @Override
    public void flush() {
    }

    @Override
    public void close() throws SecurityException {
    }
}
