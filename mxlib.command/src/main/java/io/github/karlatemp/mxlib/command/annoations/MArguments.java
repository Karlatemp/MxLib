/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: MArguments.java@author: karlatemp@vip.qq.com: 2019/12/29 下午1:37@version: 2.0
 */

package io.github.karlatemp.mxlib.command.annoations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The arguments command invoke.
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface MArguments {
    /**
     * Need put full command arguments.
     *
     * @return Put full arguments
     */
    boolean full() default false;

    /**
     * True then input command line source.
     *
     * @return Real Full arguments
     */
    boolean allLine() default false;
}
