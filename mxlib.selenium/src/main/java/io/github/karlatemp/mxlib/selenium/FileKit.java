/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-selenium.main/FileKit.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.selenium;

import io.github.karlatemp.mxlib.utils.Toolkit;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

class FileKit {
    static NetKit.Invoke unzip(String entry) {
        return unzip(entry, false);
    }

    static NetKit.Invoke unzip(String entry, boolean exec) {
        return (tar, zipFile) -> {
            try (ZipFile zip = new ZipFile(zipFile)) {
                ZipEntry entry0 = zip.getEntry(entry);
                if (entry0 == null) {
                    entry0 = zip.getEntry('/' + entry);
                }
                if (entry0 == null) throw new IllegalStateException("No entry " + entry);
                try (InputStream inp = zip.getInputStream(entry0);
                     OutputStream out = new BufferedOutputStream(new FileOutputStream(tar))) {
                    Toolkit.IO.writeTo(inp, out);
                }
            }
            if (exec) tar.setExecutable(true);
        };
    }
}
