/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-terminal.main/AbstractDownloadBar.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.terminal;

import org.jline.terminal.Terminal;

import java.io.PrintWriter;

abstract class AbstractDownloadBar implements DownloadingBar, MxTerminal.DBI {
    public int left, right;
    public PrintWriter writer;
    public Terminal terminal;
    public Object msg;
    public int barLen = 20;
    public CharSequence prefix = "";

    @Override
    public int getLeft() {
        return left;
    }

    @Override
    public void setLeft(int value) {
        this.left = value;
    }

    @Override
    public int getRight() {
        return right;
    }

    @Override
    public void setRight(int value) {
        this.right = value;
    }

    @Override
    public Object getMsg() {
        return msg;
    }

    @Override
    public void setMsg(Object msg) {
        this.msg = msg;
    }

    @Override
    public int getBarWidth() {
        return barLen;
    }

    @Override
    public void setBarWidth(int value) {
        barLen = value;
    }

    @Override
    public CharSequence getPrefix() {
        return prefix;
    }

    @Override
    public void setPrefix(CharSequence prefix) {
        if (prefix == null) prefix = "";
        this.prefix = prefix;
    }

}
