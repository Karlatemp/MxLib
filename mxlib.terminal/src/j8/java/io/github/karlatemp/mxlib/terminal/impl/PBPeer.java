/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-terminal.j8/PBPeer.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.terminal.impl;

import io.github.karlatemp.mxlib.terminal.ProgressBar;

interface PBPeer {
    default void setProgressState(ProgressBar.State state) {
    }

    default void setProgressValue(int value) {
    }

    default void requestWindowUserAttention() {
    }
}
