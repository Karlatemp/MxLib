/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-terminal.main/PBDirect.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.terminal.impl;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Arrays;

public class PBDirect extends AbstractProgressBar {
    char[] output;
    int offset;
    PsPw ps;

    interface PsPw {
        void print(char[] value);
    }

    public PBDirect(PrintStream ps) {
        this.ps = ps::print;
        postBarSet();
    }

    public PBDirect(PrintWriter pw) {
        this.ps = pw::print;
        postBarSet();
    }

    @Override
    protected void postBarSet() {
        output = new char[bw - 1];
        Arrays.fill(output, '*');
        output[0] = '\r';
        postMsgSet();
    }

    @Override
    protected void postMsgSet() {
        char[] msg = buffer.toString().toCharArray();
        int len = Math.min(bw - 2, msg.length);
        System.arraycopy(msg, 0, output, 1, len);
        output[len + 1] = ' ';
        output[len + 2] = '[';
        offset = len + 3;
    }

    private static void putRv(char[] src, char[] target, int offset) {
        int dst = Math.max(0, target.length - offset - src.length);
        System.arraycopy(src, 0, target, dst, Math.min(src.length, target.length - 1 - dst));
    }

    @Override
    public void repaint() {
        long current = value, max = this.max;
        StringBuilder tmp = this.outbuffer;
        char[] output = this.output;
        int outputLen = output.length;

        int edOffset = 1;
        output[outputLen - 1] = ')';
        if (max > 1) {
            tmp.setLength(0);
            SizeFormat.format(tmp, max);
            char[] v = tmp.toString().toCharArray();
            putRv(v, output, 1);
            edOffset += v.length;
        } else {
            output[outputLen - 2] = '?';
            edOffset++;
        }
        edOffset++;
        output[outputLen - edOffset] = '/';
        edOffset++;
        if (current > 1) {
            tmp.setLength(0);
            SizeFormat.format(tmp, current);
            char[] v = tmp.toString().toCharArray();
            putRv(v, output, edOffset - 1);
            edOffset += v.length;
        } else {
            output[outputLen - edOffset] = '?';
            edOffset++;
        }
        output[outputLen - edOffset] = '(';
        int fillEnd;
        output[fillEnd = outputLen - edOffset - 1] = ' ';
        fillEnd--;
        output[fillEnd] = ']';
        if (current < 0 || max < 0) {
            for (int i = offset; i < fillEnd; i++) {
                output[i] = '.';
            }
        } else {
            long sped = offset + (current * (fillEnd - offset) / max);
            for (int i = offset; i < fillEnd; i++) {
                output[i] = i < sped ? '#' : ' ';
            }
        }

        ps.print(output);
        // [msg] [=================/  ] (10086/2048)
    }

    @Override
    protected void complete0() {
        Arrays.fill(output, ' ');
        output[0] = '\r';
        ps.print(output);
    }

    @Override
    protected void repaintTail() {
        repaint();
    }

}
