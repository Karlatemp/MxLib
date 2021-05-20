/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-terminal.test/Tr.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package t;

import io.github.karlatemp.mxlib.terminal.DownloadingBar;
import io.github.karlatemp.mxlib.terminal.LinedOutputStream;
import io.github.karlatemp.mxlib.terminal.MxTerminal;
import org.jline.reader.LineReader;
import org.jline.terminal.Terminal;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

@SuppressWarnings("CommentedOutCode")
public class Tr {
    public static void main(String[] args) throws Exception {
        MxTerminal.doInstall(null);
        Terminal terminal = MxTerminal.getTerminal();
        LineReader reader = MxTerminal.getLineReader();
        PrintWriter writer = terminal.writer();
        {
            PrintStream ps = new PrintStream(
                    new LinedOutputStream(reader, 1024 * 1024, StandardCharsets.UTF_8)
            );
            System.setOut(ps);
            System.setErr(ps);
        }
        DownloadingBar bar = MxTerminal.getDownloadingBar();
        bar.setRight(5000);
        bar.setBarWidth(Math.max(100, terminal.getWidth()));
        bar.setPrefix("Test ");
        for (int i = 0; i < 5000; i++) {
            if (i % 100 == 10) {
                MxTerminal.clearDownloadBar();
                System.out.append("Testo { ").print(i);
                System.out.println(" }");
                // reader.printAbove("Testo { " + i + " }");
                // writer.print("Testo { " + i + " }");
                // writer.flush();
            }
            bar.setMsg("Downloading test {" + i + "}");
            bar.setLeft(i);
            bar.update();
            Thread.sleep(5);
        }
        MxTerminal.clearDownloadBar();
    }
}
