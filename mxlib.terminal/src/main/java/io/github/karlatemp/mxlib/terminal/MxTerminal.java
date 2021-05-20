/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-terminal.main/MxTerminal.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.terminal;

import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Cursor;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.IOException;
import java.util.function.BiConsumer;

public class MxTerminal {
    private static Terminal TERMINAL;
    private static LineReader LINE_READER;
    public static final Object WRT_LOCK = new Object();
    static DBI dbi;

    public static DownloadingBar getDownloadingBar() {
        return dbi;
    }

    interface DBI extends DownloadingBar {
        void clearDownloadBar();
    }

    public static void clearDownloadBar() {
        synchronized (MxTerminal.WRT_LOCK) {
            dbi.clearDownloadBar();
        }
    }


    public static Terminal getTerminal() {
        return TERMINAL;
    }

    public static LineReader getLineReader() {
        return LINE_READER;
    }

    public static void doInstall(BiConsumer<TerminalBuilder, LineReaderBuilder> setup) throws IOException {
        TerminalBuilder terminalBuilder = TerminalBuilder.builder();
        LineReaderBuilder lineReaderBuilder = LineReaderBuilder.builder();
        if (setup != null) {
            setup.accept(terminalBuilder, lineReaderBuilder);
        }
        Terminal terminal = terminalBuilder.build();
        TERMINAL = terminal;
        LINE_READER = lineReaderBuilder.terminal(terminal).build();
        Cursor position = TERMINAL.getCursorPosition(null);
        if (position == null) {
            dbi = new RawDownloadBar();
        } else {
            dbi = new DownloadingBarImpl();
        }

        AbstractDownloadBar dbii = (AbstractDownloadBar) dbi;
        dbii.terminal = TERMINAL;
        dbii.writer = TERMINAL.writer();

    }
}
