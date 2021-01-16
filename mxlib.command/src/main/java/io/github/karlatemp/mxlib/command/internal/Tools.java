/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-command.main/Tools.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.command.internal;

import io.github.karlatemp.mxlib.command.CommandProvider;
import io.github.karlatemp.mxlib.command.ICommand;
import io.github.karlatemp.mxlib.common.utils.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

public class Tools {
    public static void keyword(List<String> result, String word, String... opts) {
        if (word == null) {
            result.addAll(Arrays.asList(opts));
            return;
        }
        String wl = word.toLowerCase();
        for (String o : opts) {
            if (o.toLowerCase().contains(wl)) {
                result.add(o);
            }
        }
    }

    public static List<String> getInvokePath(List<String> args, List<String> full) {
        return full.subList(0, full.size() - args.size());
    }

    public static boolean processHelp(String pi, List<String> args, List<String> full,
                                      Object sender, CommandProvider provider,
                                      ICommand thiz,
                                      String label) {
        if (args.contains(pi)) {
            int index = args.indexOf(pi);
            List<String> harg = args.subList(index + 1, args.size());
            provider.getHelp().processHelp(
                    sender, provider, getInvokePath(args, full),
                    harg, label, thiz
            );
            return true;
        }
        return false;
    }

    public static byte[] readClass(Class<?> klass) {
        try (InputStream is = klass.getResourceAsStream(klass.getSimpleName() + ".class")) {
            return IOUtils.readAllBytes(is);
        } catch (IOException ignored) {
            return null;
        }
    }
}
