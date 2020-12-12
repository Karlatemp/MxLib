/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * Reserved.FileName: MLabel.java@author: karlatemp@vip.qq.com: 2020/1/4 下午4:27@version: 2.0
 */

package io.github.karlatemp.mxlib.command.annoations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.CLASS)
@Target(ElementType.PARAMETER)
public @interface MLabel {
}
