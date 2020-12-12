/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * Reserved.FileName: PBootstrap.java@author: karlatemp@vip.qq.com: 2020/1/5 下午2:00@version: 2.0
 */

package io.github.karlatemp.mxlib.command.annoations;

import io.github.karlatemp.mxlib.command.CommandProvider;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @since 2.12
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface PBootstrap {
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE_USE)
    @interface PCommand {
        String name();

        Class<? extends CommandProvider> provider();

        Class<?> source();
    }

    PCommand[] value();
}
