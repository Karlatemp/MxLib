/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-terminal.main/PBTerminal.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.terminal.impl;

import org.fusesource.jansi.Ansi;
import org.jline.terminal.Terminal;

import java.io.PrintWriter;
import java.util.Arrays;

public class PBTerminal extends AbstractProgressBar {
    Terminal terminal;
    StringBuilder ansib = new StringBuilder();
    Ansi ansix = new Ansi(ansib);

    public PBTerminal(Terminal terminal) {
        this.terminal = terminal;
    }

    Ansi asi() {
        ansib.setLength(0);
        return ansix;
    }

    @Override
    public void repaint() {
        PrintWriter writer = terminal.writer();
        writer.print(Ansi.ansi().eraseLine().a(buffer).a(' ').a('['));
        writer.flush();
        repaintTail();
    }

    @Override
    protected void repaintTail() {
        PrintWriter writer = terminal.writer();
        StringBuilder smp = this.outbuffer;//, tmp2 = s2;
        smp.setLength(0);
        smp.append(']').append(' ').append('(');
        long current = this.value, max = this.max;
        if (current < 0) {
            smp.append('?');
        } else {
            SizeFormat.format(smp, current);
        }
        smp.append('/');
        if (max < 0) {
            smp.append('?');
        } else {
            SizeFormat.format(smp, max);
        }
        smp.append(')');
        int wdx = terminal.getWidth();

        writer.print(
                asi().cursorToColumn(wdx - 1 - smp.length()).a(smp)
        );
        wdx -= smp.length();

        int fvm = wdx - buffer.length() - 4;
        char[] c = new char[fvm];
        if (current < 0 || max < 0) {
            Arrays.fill(c, '.');
        } else {
            long sped = (current * fvm / max);
            for (int i = 0; i < sped; i++) {
                c[i] = i < sped ? '#' : ' ';
            }
        }

        writer.print(
                asi().cursorToColumn(buffer.length() + 3).a(c).cursorToColumn(0)
        );

        writer.flush();
    }

    @Override
    protected void complete0() {
        PrintWriter writer = terminal.writer();
        writer.print(asi().eraseLine());
        writer.flush();
    }

}
