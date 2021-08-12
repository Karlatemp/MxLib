/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-terminal.main/ProgressBar.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.terminal;

import io.github.karlatemp.mxlib.terminal.impl.PBDirect;
import io.github.karlatemp.mxlib.terminal.impl.PBTerminal;
import org.jline.terminal.Terminal;

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * All the methods are thread-unsafely
 */
@SuppressWarnings("Since15")
public interface ProgressBar {
    /**
     * Kinds of available window progress states.
     *
     * @see java.awt.Taskbar
     */
    public static enum State {
        /**
         * Stops displaying the progress.
         */
        OFF,
        /**
         * The progress indicator displays with normal color and determinate
         * mode.
         */
        NORMAL,
        /**
         * Shows progress as paused, progress can be resumed by the user.
         * Switches to the determinate display.
         */
        PAUSED,
        /**
         * The progress indicator displays activity without specifying what
         * proportion of the progress is complete.
         */
        INDETERMINATE,
        /**
         * Shows that an error has occurred. Switches to the determinate
         * display.
         */
        ERROR
    }

    /**
     * Available on Windows, JRE 9+, cmd.exe
     */
    void setProgressState(State state);

    /**
     * Available on JRE 9+
     */
    void requestWindowUserAttention();

    long getValue();

    long getMaxValue();

    void setValue(long value);

    void setMaxValue(long value);

    /**
     * Available if jline not working
     */
    void setBarWidth(int value);

    void setMsg(Object msg);

    /**
     * Call after printed a new message
     */
    void repaint();

    void complete();

    boolean isAutoFlush();

    void setAutoFlush(boolean value);

    static ProgressBar of(Terminal terminal) {
        if (terminal.getCursorPosition(null) == null) {
            return new PBDirect(terminal.writer());
        } else {
            return new PBTerminal(terminal);
        }
    }

    static ProgressBar of(PrintStream ps) {
        return new PBDirect(ps);
    }

    static ProgressBar of(PrintWriter ps) {
        return new PBDirect(ps);
    }

    static ProgressBar ofTerminal(Terminal terminal) {
        return new PBTerminal(terminal);
    }
}
