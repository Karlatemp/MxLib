/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-command.main/CommandParameter.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.command;

import io.github.karlatemp.mxlib.command.annoations.MParameter;

/**
 * The parameter of command.
 *
 * @see io.github.karlatemp.mxlib.command.annoations.MParameter
 */
public interface CommandParameter {
    /**
     * Get the option name of this parameter.
     *
     * @return The option name.
     */
    String name();

    /**
     * The mark type.
     *
     * @return The type.
     */
    Class<?> type();

    /**
     * The value of {@link MParameter#description()}
     *
     * @return Parameter description.
     */
    String description();

    /**
     * Does this parameter must exists?
     *
     * @return Need exists.
     */
    boolean require();
}
