/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: Commands.java@author: karlatemp@vip.qq.com: 2019/12/29 下午1:25@version: 2.0
 */

package io.github.karlatemp.mxlib.command.annoations;

import io.github.karlatemp.mxlib.command.CommandProvider;
import io.github.karlatemp.mxlib.command.TabCompileMode;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.PACKAGE, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface MCommands {
    /**
     * The name of this command set.
     *
     * @return The name of this command set.
     */
    String value();

    /**
     * The permission need to invoke this command.
     *
     * @return The permission need.
     */
    String permission() default "";

    /**
     * If this command non have permission. Them will send this message to sender.
     *
     * @return The message will send back if sender don't have permission.
     */
    String noPermissionMessage() default "";

    String description() default "";

    /**
     * The providers of this commands.
     *
     * @return The provider using.
     */
    Class<? extends CommandProvider> provider() default CommandProvider.class;

    TabCompileMode tabCompileMode() default TabCompileMode.CONTAINS;
}
