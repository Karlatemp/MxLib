/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-api.main/MessageRender.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.logger;

import io.github.karlatemp.mxlib.utils.StringBuilderFormattable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 * Message Render
 *
 * @see io.github.karlatemp.mxlib.logger.renders.PrefixedRender
 * @see io.github.karlatemp.mxlib.logger.renders.SimpleRender
 */
public interface MessageRender {
    /**
     * Render a message
     */
    @NotNull StringBuilder render(
            @Nullable String loggerName,
            @Nullable MMarket market,
            @Nullable StringBuilderFormattable message,
            boolean isError,
            @Nullable Level level,
            @Nullable LogRecord record
    );
}
