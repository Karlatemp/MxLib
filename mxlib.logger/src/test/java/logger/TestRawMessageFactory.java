/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-logger.test/TestRawMessageFactory.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package logger;

import io.github.karlatemp.mxlib.common.utils.SimpleClassLocator;
import io.github.karlatemp.mxlib.logger.AnsiMessageFactory;
import io.github.karlatemp.mxlib.logger.SimpleLogger;
import io.github.karlatemp.mxlib.logger.renders.PrefixedRender;
import io.github.karlatemp.mxlib.logger.renders.PrefixedRender.PrefixSupplier;
import io.github.karlatemp.mxlib.logger.renders.SimpleRender;
import io.github.karlatemp.mxlib.utils.StringUtils.BkColors;
import org.junit.jupiter.api.Test;

import java.lang.management.ManagementFactory;
import java.time.format.DateTimeFormatter;

public class TestRawMessageFactory {
    public static PrefixedRender newRender() {
        var mf = new AnsiMessageFactory(new SimpleClassLocator());
        return new PrefixedRender(
                new SimpleRender(mf),
                PrefixSupplier.constant(BkColors._B)
                        .plus(PrefixSupplier.dated(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                        .plus(" " + BkColors._5)
                        .plus(PrefixSupplier.dated(DateTimeFormatter.ofPattern("HH:mm:ss")))
                        .plus(BkColors.RESET + " [" + BkColors._6)
                        .plus(PrefixSupplier.loggerName().aligned(PrefixedRender.AlignedSupplier.AlignType.LEFT))
                        .plus(BkColors.RESET + "] [" + BkColors._B)
                        .plus(PrefixSupplier.loggerLevel().aligned(PrefixedRender.AlignedSupplier.AlignType.CENTER))
                        .plus(BkColors.RESET + "] ")
        );
    }

    public static class Lg extends SimpleLogger {
        public static final PrefixedRender renderX = newRender();

        public Lg(String n) {
            super(n, renderX);
        }

        @Override
        protected void render(StringBuilder message) {
            System.out.println(message);
        }
    }

    @Test
    void runRawMessageFactory() {
        var mf = new AnsiMessageFactory(new SimpleClassLocator());
        System.out.println(mf.dump(new Exception(new Exception("Sub cause")), true).to_string());
        System.out.println();
        System.out.println("====================================");
        System.out.println();
        var r = ManagementFactory.getThreadMXBean();
        var inf = r.getThreadInfo(new long[]{
                Thread.currentThread().getId()
        }, true, true)[0];
        System.out.println(mf.dump(inf, -1).to_string());
        System.out.println(inf);
    }

    @Test
    void runAnsiMessageFactory() {
        new Lg("HX").info(new Exception("Hello!"));
        new Lg("HX").info("WXC", new Exception("Hello!"));
        new Lg("OWXV").warn(new Exception("Hi!"));
        new Lg("HX").info(new Exception("HOW ARE YOU!", new Exception("sx", new Exception("RT"))));

    }

}
