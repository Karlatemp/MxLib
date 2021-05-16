/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-selenium.main/NetKit.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.selenium;

import io.github.karlatemp.mxlib.logger.MLogger;
import org.apache.http.client.methods.HttpGet;

import java.io.*;
import java.util.ArrayList;

import static io.github.karlatemp.mxlib.selenium.MxSelenium.client;

class NetKit {
    interface Invoke {
        void invoke(File file, File zip) throws IOException;
    }

    static int retryCounts = 5;

    static File download(
            File file,
            String url,
            String zipname,
            Invoke unzipper
    ) throws IOException {
        return download(file, url, zipname, unzipper, false);
    }

    static File download(
            File file,
            String url,
            String zipname,
            Invoke unzipper,
            boolean override
    ) throws IOException {
        MLogger logger = MxSelenium.getLogger();
        if (!file.isFile() || override) {
            File parent = file.getParentFile();
            if (parent != null) parent = new File(".");
            parent.mkdirs();
            File zip;
            if (zipname == null) zip = file;
            else zip = new File(parent, zipname);

            if (logger.isInfoEnabled()) {
                logger.info("Downloading " + file.getName() + " from " + url);
            }
            ArrayList<Throwable> errors = new ArrayList<>(retryCounts);
            for (int i = 0; i < retryCounts; i++) {
                if (i != 0 && logger.isInfoEnabled()) {
                    logger.info("Download fail. retrying....");
                }
                try (OutputStream out = new BufferedOutputStream(new FileOutputStream(zip))) {
                    client.execute(new HttpGet(url)).getEntity().writeTo(out);
                } catch (Throwable throwable) {
                    logger.error(throwable.toString());
                    errors.add(throwable);
                    continue;
                }
                if (unzipper != null)
                    unzipper.invoke(file, zip);
                return file;
            }
            IOException downloadFail = new IOException(
                    "Failed download " + file.getName() + " from " + url
            );
            for (Throwable e : errors)
                downloadFail.addSuppressed(e);
            throw downloadFail;
        }
        return file;
    }
}
