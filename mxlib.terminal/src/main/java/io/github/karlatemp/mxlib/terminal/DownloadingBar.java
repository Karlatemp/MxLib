/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-terminal.main/DownloadingBar.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.terminal;

public interface DownloadingBar {
    int getLeft();

    void setLeft(int value);

    int getRight();

    int getBarWidth();

    void setBarWidth(int value);

    void setRight(int value);

    Object getMsg();

    void setMsg(Object msg);

    void update();

    CharSequence getPrefix();

    void setPrefix(CharSequence value);
}
