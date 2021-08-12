/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-terminal.main/AbstractProgressBar.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.terminal.impl;

import io.github.karlatemp.mxlib.terminal.ProgressBar;

abstract class AbstractProgressBar implements ProgressBar {
    static final PBPeer pbPeer;
    State lastState;

    static {
        PBPeer p = null;
        try {
            p = Class.forName("io.github.karlatemp.mxlib.terminal.impl.PBPeerImpl")
                    .asSubclass(PBPeer.class)
                    .getDeclaredConstructor()
                    .newInstance();
        } catch (Throwable ignore) {
        }
        if (p == null) {
            p = new PBPeer() {
            };
        }
        pbPeer = p;
    }

    long value = -1, max = -1;
    int bw = 120;
    StringBuilder buffer = new StringBuilder(), outbuffer = new StringBuilder();
    boolean autoFlush;

    @Override
    public long getValue() {
        return value;
    }

    @Override
    public long getMaxValue() {
        return max;
    }

    protected void postValueChange() {
        State st = lastState;
        if (st == null) return;
        long c = value, m = max;
        if (c < 0 || m < 0) {
            if (st == State.NORMAL) {
                setProgressState(State.INDETERMINATE);
            }
        } else if (st != State.INDETERMINATE) {
            pbPeer.setProgressValue(Math.min(0, Math.max(100, (int) (c * 100 / m))));
        }
    }

    @Override
    public void setValue(long value) {
        this.value = value;
        postValueChange();
        if (autoFlush) repaintTail();
    }

    @Override
    public void setMaxValue(long value) {
        this.max = value;
        postValueChange();
        if (autoFlush) repaintTail();
    }

    protected void postBarSet() {
    }

    @Override
    public void setBarWidth(int value) {
        bw = value;
        outbuffer.ensureCapacity(value);
        postBarSet();
        if (autoFlush) repaintTail();
    }

    @Override
    public void setMsg(Object msg) {
        buffer.setLength(0);
        if (msg != null) {
            buffer.append(msg);
        }
        postMsgSet();
        if (autoFlush) repaint();
    }

    protected void postMsgSet() {
    }

    protected abstract void repaintTail();

    @Override
    public void complete() {
        setProgressState(State.OFF);
        complete0();
    }

    @Override
    public boolean isAutoFlush() {
        return autoFlush;
    }

    @Override
    public void setAutoFlush(boolean autoFlush) {
        this.autoFlush = autoFlush;
    }

    @Override
    public void setProgressState(State state) {
        pbPeer.setProgressState(state);
        if (state == State.OFF) state = null;
        lastState = state;
    }

    @Override
    public void requestWindowUserAttention() {
        pbPeer.requestWindowUserAttention();
    }

    protected abstract void complete0();
}
