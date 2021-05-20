/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-terminal.main/DownloadingBarImpl.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.terminal;

import org.fusesource.jansi.Ansi;
import org.jline.terminal.Cursor;

import java.util.Arrays;

class DownloadingBarImpl extends AbstractDownloadBar implements MxTerminal.DBI {

    static final char[] EMP = new char[1024];

    static {
//        Arrays.fill(EMP, '█');
        Arrays.fill(EMP, ' ');
    }

    public void update() {
        synchronized (MxTerminal.WRT_LOCK) {
            update0();
        }
    }

    private void update0() {
        Cursor position = terminal.getCursorPosition(null);
        StringBuilder nm = new StringBuilder()
                .append(" (" )
                .append(left).append('/').append(right)
                .append(')');
        int bn = Math.max(0,
                Math.min(barLen, terminal.getWidth())
                        - nm.length()
                        - prefix.length()
        );
        int olf;
        if (right == 0) {
            olf = 0;
        } else {
            olf = bn * left / right;
        }
        writer.print(
                Ansi.ansi()
                        .a("\n\n\n" )
                        .cursorUp(2)
                        .eraseLine()
                        .fgBrightYellow()
                        .a(prefix)
                        .bgCyan()
                        .a(EMP, 0, olf)
                        .bgBrightMagenta()
                        .a(EMP, 0, bn - olf)
                        .reset()
                        .a(nm)
                        .a('\n')
                        .a(msg)
                        .cursorUp(2)
                        .cursorToColumn(position.getX())
                        .reset()
        );
        writer.flush();
    }

    @Override
    public void clearDownloadBar() {
        terminal.writer().print(
                Ansi.ansi().eraseLine()
                        .a('\n').eraseLine()
                        .a('\n').eraseLine()
                        .cursorUp(2)
        );
        terminal.writer().flush();
    }
}
