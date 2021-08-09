/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-api.main/StringBuilderFormattableInternal.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.internal;

import io.github.karlatemp.mxlib.utils.StringBuilderFormattable;
import org.jetbrains.annotations.NotNull;

import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings({"rawtypes", "unchecked"})
public class StringBuilderFormattableInternal {
    private interface Conversation<T> {
        void apply(T formattable, StringBuilder sb);
    }

    private static final Map<Class<?>, Conversation<?>> CONVERSATIONS = new HashMap<>();

    private static <T> void reg(Class<T> t, Conversation<T> cov) {
        CONVERSATIONS.put(t, cov);
    }

    static {
        reg(StringBuilderFormattable.class, StringBuilderFormattable::formatTo);

        try {

            log4j:
            {
                Class<?> log4jFormattable;
                try {
                    log4jFormattable = Class.forName("org.apache.logging.log4j.util.StringBuilderFormattable");
                } catch (ClassNotFoundException ignore) {
                    break log4j;
                }
                MethodHandles.Lookup lk = MethodHandles.lookup();
                Object conversation = LambdaMetafactory.metafactory(
                        lk,
                        "apply",
                        MethodType.methodType(Conversation.class),
                        MethodType.methodType(void.class, Object.class, StringBuilder.class),
                        lk.findVirtual(log4jFormattable, "formatTo", MethodType.methodType(void.class, StringBuilder.class)),
                        MethodType.methodType(void.class, log4jFormattable, StringBuilder.class)
                ).getTarget().invoke();
                CONVERSATIONS.put(log4jFormattable, (Conversation) conversation);
            }

        } catch (Throwable throwable) {
            throw new ExceptionInInitializerError(throwable);
        }

    }

    private static Conversation getConversation(Object object) {
        if (object == null) return null;
        for (Map.Entry<Class<?>, Conversation<?>> entry : CONVERSATIONS.entrySet()) {
            if (entry.getKey().isInstance(object)) return entry.getValue();
        }
        return null;
    }

    public static String toString(Object object) {
        if (object == null) return "null";
        Conversation cov = getConversation(object);
        if (cov != null) {
            StringBuilder builder = new StringBuilder();
            cov.apply(object, builder);
            return builder.toString();
        }
        return String.valueOf(object);
    }

    public static void append(@NotNull StringBuilder builder, Object value) {
        if (value instanceof CharSequence) {
            builder.append((CharSequence) value);
        } else if (value == null) {
            builder.append("null");
        } else {
            Conversation cov = getConversation(value);
            if (cov != null) {
                cov.apply(value, builder);
            } else {
                builder.append(value);
            }
        }
    }

    public static StringBuilderFormattable tryConv(Object obj) {
        if (obj == null || obj instanceof StringBuilderFormattable)
            return (StringBuilderFormattable) obj;
        Conversation conv = getConversation(obj);

        if (conv != null)
            return sb -> conv.apply(obj, sb);

        return null;
    }
}
