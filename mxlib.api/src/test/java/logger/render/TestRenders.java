/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-api.test/TestRenders.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package logger.render;

import io.github.karlatemp.mxlib.logger.MMarket;
import io.github.karlatemp.mxlib.logger.MessageRender;
import io.github.karlatemp.mxlib.logger.renders.PrefixedRender;
import io.github.karlatemp.mxlib.utils.StringBuilderFormattable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Test;

import java.util.logging.Level;
import java.util.logging.LogRecord;

public class TestRenders {
    @Test
    void invoke() {
        var render = new MessageRender() {
            @Override
            public @NotNull StringBuilder render(@Nullable String loggerName, @Nullable MMarket market, @Nullable StringBuilderFormattable message, boolean isError, @Nullable Level level, @Nullable LogRecord record) {
                StringBuilder builder = new StringBuilder();
                if (message != null)
                    message.formatTo(builder);
                return builder;
            }
        };
        var prefix = new PrefixedRender.AlignedSupplier() {
            @Override
            public @NotNull CharSequence rendPrefix(
                    @Nullable String loggerName, @Nullable MMarket market,
                    @NotNull StringBuilder content,
                    boolean isError,
                    @Nullable Level level,
                    @Nullable LogRecord record,
                    int[] len) {
                String name = market == null ? "<none>" : market.getName();
                return name + "> ";
            }

            {
                alignType = AlignType.RIGHT;
            }
        };
        var renderX = new PrefixedRender(render, prefix);
        var message = StringBuilderFormattable.by("Line sb yellow");
        System.out.println(renderX.render(
                null, null,
                message, false, null, null
        ).toString());
        System.out.println(renderX.render(
                null, () -> "Sb Yellow",
                message, false, null, null
        ).toString());
        System.out.println(renderX.render(
                null, null,
                message, false, null, null
        ).toString());
        System.out.println(renderX.render(
                null, () -> "Sb Sb SB Yellowwww!",
                message, false, null, null
        ).toString());
        System.out.println(renderX.render(
                null, null,
                message, false, null, null
        ).toString());
        System.out.println(renderX.render(
                null, null,
                message, false, null, null
        ).toString());
    }

}
