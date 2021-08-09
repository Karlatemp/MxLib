/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-logger.test/LoggerSetup.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package logger;

import io.github.karlatemp.mxlib.MxLib;
import io.github.karlatemp.mxlib.common.utils.SimpleClassLocator;
import io.github.karlatemp.mxlib.logger.AnsiMessageFactory;
import io.github.karlatemp.mxlib.logger.AwesomeLogger;
import io.github.karlatemp.mxlib.logger.MessageRender;
import io.github.karlatemp.mxlib.logger.renders.PrefixedRender;
import io.github.karlatemp.mxlib.logger.renders.SimpleRender;
import io.github.karlatemp.mxlib.utils.StringUtils;

import java.time.format.DateTimeFormatter;
import java.util.function.Consumer;
import java.util.logging.Level;

public class LoggerSetup {
    private static class OurLogger extends AwesomeLogger.Awesome {
        public OurLogger(String name, Consumer<StringBuilder> printer, MessageRender render) {
            super(name, printer, render);
        }

        @Override
        public boolean isEnabled(Level level) {
            // Custom logging rules
            return level.intValue() >= Level.INFO.intValue();
        }
    }

    public static void setupLogger() {
        AnsiMessageFactory messageFactory = new AnsiMessageFactory(new SimpleClassLocator());
        PrefixedRender renderX = new PrefixedRender(
                new SimpleRender(messageFactory),
                PrefixedRender.PrefixSupplier.constant(StringUtils.BkColors._B)
                        .plus(PrefixedRender.PrefixSupplier.dated(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                        .plus(" " + StringUtils.BkColors._5)
                        .plus(PrefixedRender.PrefixSupplier.dated(DateTimeFormatter.ofPattern("HH:mm:ss")))
                        .plus(StringUtils.BkColors.RESET + " [" + StringUtils.BkColors._7)
                        .plus(PrefixedRender.PrefixSupplier.threadName().aligned())
                        .plus(StringUtils.BkColors.RESET + "] [" + StringUtils.BkColors._6)
                        .plus(PrefixedRender.PrefixSupplier.loggerName().aligned(PrefixedRender.AlignedSupplier.AlignType.LEFT))
                        .plus(StringUtils.BkColors.RESET + "] [" + StringUtils.BkColors._B)
                        .plus(PrefixedRender.PrefixSupplier.loggerLevel().aligned(PrefixedRender.AlignedSupplier.AlignType.CENTER))
                        .plus(StringUtils.BkColors.RESET + "] ")
        );
        Consumer<StringBuilder> printer = System.out::println;
        MxLib.setLogger(new OurLogger("Toplevel", printer, renderX));
        MxLib.setLoggerFactory(name -> new OurLogger(name, printer, renderX));
    }

    public static void main(String[] args) {
        setupLogger();
        MxLib.getLogger().info("Hello!");
        MxLib.getLoggerFactory().getLogger("Named Logger").info("Hi!");
        MxLib.getLoggerFactory().getLogger("Named Logger").verbose("Verbose messages");
        MxLib.getLogger().error("Error message", new Exception("Thread stacktrace"));
    }
}
