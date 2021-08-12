/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-terminal.j9/PBPeerImpl.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.terminal.impl;

import io.github.karlatemp.mxlib.terminal.ProgressBar;

import java.awt.*;
import java.util.EnumMap;
import java.util.Map;

class PBPeerImpl implements PBPeer {
    final Taskbar taskbar;
    final Window consoleWindow;
    final Map<ProgressBar.State, Taskbar.State> mapping = new EnumMap<>(ProgressBar.State.class);

    {
        if (!Taskbar.isTaskbarSupported()) {
            throw new UnsupportedOperationException();
        }
        consoleWindow = FakeWindowFactory.factory.getConsole();
        if (consoleWindow == null) {
            throw new UnsupportedOperationException();
        }
        taskbar = Taskbar.getTaskbar();
        for (ProgressBar.State s : ProgressBar.State.values()) {
            mapping.put(s, Taskbar.State.valueOf(s.name()));
        }
    }

    @Override
    public void setProgressState(ProgressBar.State state) {
        if (taskbar.isSupported(Taskbar.Feature.PROGRESS_STATE_WINDOW)) {
            taskbar.setWindowProgressState(consoleWindow, mapping.get(state));
        }
    }

    @Override
    public void requestWindowUserAttention() {
        if (taskbar.isSupported(Taskbar.Feature.USER_ATTENTION_WINDOW)) {
            taskbar.requestWindowUserAttention(consoleWindow);
        }
    }
}
