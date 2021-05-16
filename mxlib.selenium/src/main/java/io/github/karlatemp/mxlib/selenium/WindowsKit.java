/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-selenium.main/WindowsKit.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.selenium;

import com.google.common.base.Splitter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Pattern;

import static io.github.karlatemp.mxlib.selenium.MxSelenium.DEBUG;
import static io.github.karlatemp.mxlib.selenium.MxSelenium.commandProcessResult;

class WindowsKit {
    static Splitter splitter = Splitter.on(Pattern.compile("\\s+")).limit(4);

    static Map<String, Map<String, String>> parseRegResult(String result) {
        if (DEBUG) {
            System.out.println("Try parsing: " + result);
            System.out.println();
            System.out.println();
        }
        try (Scanner scanner = new Scanner(result)) {
            Map<String, Map<String, String>> res = new HashMap<>();

            while (scanner.hasNextLine()) {
                String nextLine = scanner.nextLine();
                if (nextLine.isEmpty()) {
                    continue;
                }
                if (DEBUG) {
                    System.out.println();
                    System.out.println("===== Next Chunk: " + nextLine);
                }
                Map<String, String> rfx = res.computeIfAbsent(nextLine, $ -> new HashMap<>());
                while (scanner.hasNextLine()) {
                    String next = scanner.nextLine();
                    if (next.isEmpty()) break;
                    List<String> list = splitter.splitToList(next);
                    if (DEBUG) {
                        System.out.println("Line: " + next + ", b64:" + Base64.getEncoder().encodeToString(next.getBytes(StandardCharsets.UTF_8)));
                        System.out.println("List: " + list);
                    }
                    rfx.put(list.get(1), list.get(3));
                }
            }
            if (DEBUG) {
                System.out.println();
                System.out.println();
                System.out.println();
            }

            return res;
        }
    }

    static String queryBrowserUsing() throws IOException {
        String override = System.getProperty("mxlib.selenium.browser"); // DEBUG ONLY
        if (override != null) return override;
        Iterator<Map<String, String>> iterator = WindowsKit.parseRegResult(commandProcessResult(
                "reg", "query", "HKEY_CURRENT_USER\\SOFTWARE\\Microsoft\\Windows\\Shell\\Associations\\URLAssociations\\https\\UserChoice", "/v", "ProgId"
        )).values().iterator();
        if (!iterator.hasNext()) {
            throw new IllegalStateException("Couldn't read default browser from `HKEY_CURRENT_USER\\SOFTWARE\\Microsoft\\Windows\\Shell\\Associations\\URLAssociations\\https\\UserChoice`, "
                    + "Please open WindowsSettings and check your default browser.");
        }
        return iterator.next().get("ProgId");
    }

    static Map<String, String> queryApplicationInfo(String progId) throws IOException {
        Iterator<Map<String, String>> iterator = WindowsKit.parseRegResult(commandProcessResult(
                "reg", "query", "HKEY_CLASSES_ROOT\\" + progId + "\\Application"
        )).values().iterator();
        if (!iterator.hasNext()) {
            throw new IllegalStateException("Couldn't found Application info from `" + "HKEY_CLASSES_ROOT\\" + progId + "\\Application" + "`");
        }
        return iterator.next();
    }

    static String queryProcOpenCommand(String progId) throws IOException {
        return WindowsKit.parseRegResult(commandProcessResult(
                "reg", "query", "HKEY_CLASSES_ROOT\\" + progId + "\\shell\\open\\command", "/ve"
        )).values().iterator().next().values().iterator().next();
    }
}
