/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-logger.main/JdkLoggerUtils.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.logger.utils;

import io.github.karlatemp.mxlib.utils.Lazy;

import java.util.logging.Logger;

public class JdkLoggerUtils {
    public static final Lazy<Logger> ROOT = Lazy.publication(() -> {
        Logger global = Logger.getGlobal();
        while (true) {
            Logger c = global;
            if ((global = c.getParent()) == null) {
                return c;
            }
        }
    });
}
