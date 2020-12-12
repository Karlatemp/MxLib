/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * Reserved.FileName: Tools.java@author: karlatemp@vip.qq.com: 2020/1/3 下午11:10@version: 2.0
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
