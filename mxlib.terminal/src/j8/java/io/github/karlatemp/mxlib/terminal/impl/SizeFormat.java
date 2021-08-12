/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-terminal.main/SizeFormat.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.terminal.impl;

class SizeFormat {
    static final long KB = 1024L;
    static final long K0 = 1000L;

    static final long MB = 1024L * 1024L;
    static final long M0 = 1024L * 1000L;

    static final long GB = 1024L * 1024L * 1024L;
    static final long G0 = 1024L * 1024L * 1000L;

    static final long TB = 1024L * 1024L * 1024L * 1024L;
    static final long T0 = 1024L * 1024L * 1024L * 1000L;

    static double sf(double v) {
        return Math.floor(v * 10) / 10;
    }

    static void format(StringBuilder sb, long v) {
        if (v < 0) {
            sb.append(v);
            return;
        }
        if (v < K0) {
            sb.append(v).append('B');
            return;
        }
        if (v < M0) {
            sb.append(sf(v / 1024d))
                    .append('K');
            return;
        }
        if (v < G0) {
            sb.append(sf(((double) (v / KB)) / 1024d))
                    .append('M');
            return;
        }
        if (v < T0) {
            sb.append(sf(((double) (v / MB)) / 1024d))
                    .append('G');
            return;
        }
        sb.append(sf(((double) (v / GB)) / 1024d))
                .append('T');
    }
}
