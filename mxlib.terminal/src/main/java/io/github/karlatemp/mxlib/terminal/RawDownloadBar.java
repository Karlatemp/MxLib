/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-terminal.main/RawDownloadBar.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.terminal;

import java.util.Arrays;

class RawDownloadBar extends AbstractDownloadBar {

    static final char[] BWN = new char[1024];

    static {
        Arrays.fill(BWN, '=');
    }

    @Override
    public void update() {
        StringBuilder sb = new StringBuilder();
        sb.append('\r');
        sb.append(prefix);
        sb.append(msg);
        sb.append(' ').append('[');
        int zInsert = sb.length();
        sb.append(']').append(' ').append('(');
        sb.append(left).append('/').append(right).append(')');
        int blen = barLen - sb.length() + 1;
        int olf;
        if (right == 0) {
            olf = 0;
        } else {
            olf = blen * left / right;
        }
        sb.insert(zInsert, DownloadingBarImpl.EMP, 0, blen - olf);
        sb.insert(zInsert, BWN, 0, olf);
        writer.print(sb);
        writer.flush();
    }

    @Override
    public void clearDownloadBar() {
        writer.print('\r');
        writer.write(DownloadingBarImpl.EMP, 0, barLen);
        writer.print('\r');
        writer.flush();
    }
}
