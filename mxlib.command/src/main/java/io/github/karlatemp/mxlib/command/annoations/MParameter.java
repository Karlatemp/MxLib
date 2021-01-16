/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-command.main/MParameter.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.command.annoations;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Define a parameter.
 *
 * @since 2.11
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface MParameter {
    /**
     * Mark this parameter must exists
     *
     * @return Must exists
     */
    boolean require() default false;

    /**
     * This attribute only for field type {@code boolean}
     *
     * @return If this parameter was more parameter
     */
    boolean hasParameter() default false;

    /**
     * @return The name of this option.
     */
    String name();

    String[] alias() default {};

    String description() default "";

}
