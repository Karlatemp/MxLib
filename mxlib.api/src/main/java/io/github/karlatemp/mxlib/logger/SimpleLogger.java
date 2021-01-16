/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-api.main/SimpleLogger.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.logger;

public abstract class SimpleLogger extends AwesomeLogger {
    private final String name;

    public SimpleLogger(String name, MessageRender render) {
        super(render);
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
