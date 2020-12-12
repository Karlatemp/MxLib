/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: MParameter.java@author: karlatemp@vip.qq.com: 2019/12/29 下午1:39@version: 2.0
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
