/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-command.main/MCommand.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.command.annoations;

import io.github.karlatemp.mxlib.command.CommandProvider;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface MCommand {
    /**
     * The name of this command.
     * <p>
     * If it is empty. then will use Class/Method name.
     *
     * @return The name of this command.
     */
    String name() default "";

    /**
     * The description of this command.
     *
     * @return The description of this command.
     */
    String description() default "";

    /**
     * The Usage of current commands
     *
     * @return
     */
    String usage() default "";

    /**
     * The permission of this command.
     *
     * @return The permission need check.
     */
    String permission() default "";

    /**
     * If this command non have permission. Them will send this message to sender.
     *
     * @return The message will send back if sender don't have permission.
     */
    String noPermissionMessage() default "";

    /**
     * The provider using.
     *
     * @return The provider of this command.
     */
    Class<? extends CommandProvider> provider() default CommandProvider.class;
}
