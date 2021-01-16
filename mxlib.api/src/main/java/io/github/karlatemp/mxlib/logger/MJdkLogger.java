/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-api.main/MJdkLogger.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.logger;

import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class MJdkLogger extends Logger {
    private final MLogger delegate;

    public MJdkLogger(MLogger logger) {
        super(logger.getName(), null);
        this.delegate = logger;
    }

    @Override
    public Logger getParent() {
        return null;
    }

    @Override
    public void setParent(Logger parent) {
    }

    @Override
    public void log(LogRecord record) {
        delegate.log(record);
        Handler[] handlers = getHandlers();
        if (handlers != null) for (Handler h : handlers) {
            if (h != null) h.publish(record);
        }
    }
}

