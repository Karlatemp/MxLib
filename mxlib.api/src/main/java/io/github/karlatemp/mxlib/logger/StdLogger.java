/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-api.main/StdLogger.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.logger;

import io.github.karlatemp.mxlib.logger.renders.PrefixedRender;
import io.github.karlatemp.mxlib.logger.renders.PrefixedRender.PrefixSupplier;
import io.github.karlatemp.mxlib.logger.renders.SimpleRender;

import java.io.PrintStream;
import java.time.format.DateTimeFormatter;

public class StdLogger extends AwesomeLogger {

    protected static final PrefixedRender DEFAULT_RENDERER = new PrefixedRender(
            new SimpleRender(new BasicMessageFactory()),
            PrefixSupplier.constant("[")
                    .plus(PrefixSupplier.dated(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                    .plus(" ")
                    .plus(PrefixSupplier.loggerAndRecordName())
                    .plus("] ")
    );

    protected final PrintStream out;
    protected String name;

    public StdLogger(String name, PrintStream out) {
        this(name, out, DEFAULT_RENDERER);
    }

    public StdLogger(String name, PrintStream out, MessageRender render) {
        this.out = out;
        this.name = name;
        this.render = render;
    }

    @Override
    protected void render(StringBuilder message) {
        out.println(message);
    }

    @Override
    public String getName() {
        return name;
    }
}
