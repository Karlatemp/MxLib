/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-logger-log4j2.main/MLog4jMessageFactory.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.logger.slf4j;

import io.github.karlatemp.mxlib.utils.StringBuilderFormattable;
import org.apache.logging.log4j.message.Message;
import org.jetbrains.annotations.NotNull;


class MLog4jMessageFactory {
    static StringBuilderFormattable of(Message msg) {
        if (msg instanceof WrpMsg) {
            Object obj = ((WrpMsg) msg).obj;
            return StringBuilderFormattable.byLazyToString(obj);
        }
        return new StringBuilderFormattable() {
            @Override
            public void formatTo(@NotNull StringBuilder builder) {
                builder.append(msg.getFormattedMessage());
            }
        };
    }

    static class WrpMsg extends EmpLog {
        final Object obj;

        WrpMsg(Object obj) {
            this.obj = obj;
        }

        @Override
        public String getFormattedMessage() {
            return StringBuilderFormattable.toString(obj);
        }
    }

    static abstract class EmpLog implements Message {
        @Override
        public String getFormat() {
            return "";
        }

        @Override
        public Object[] getParameters() {
            return new Object[0];
        }

        @Override
        public Throwable getThrowable() {
            return null;
        }
    }
}
