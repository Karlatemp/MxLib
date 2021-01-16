/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-command.main/MCommands.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
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
