/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-selenium.main/FirefoxKit.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.selenium;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.github.karlatemp.mxlib.utils.Toolkit;
import org.codehaus.plexus.archiver.tar.TarGZipUnArchiver;
import org.codehaus.plexus.logging.console.ConsoleLoggerManager;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipFile;

class FirefoxKit {
    static final String API = "https://api.github.com/repos/mozilla/geckodriver/releases/latest";
    static final File firefox_cache = new File(MxSelenium.data, "firefox_driver.json");

    static void fetch() throws IOException {
        if (firefox_cache.isFile() && System.currentTimeMillis() - firefox_cache.lastModified() < 1000L * 60 * 60 * 24 * 7) {
            try (Reader reader = new InputStreamReader(new FileInputStream(firefox_cache), StandardCharsets.UTF_8)) {
                JsonParser.parseReader(reader);
                return;
            } catch (Throwable ignored) {
            }
        }
        try {
            NetKit.download(firefox_cache, Collections.singleton(API), "firefox_driver.json.redownload", (to, from) -> {
                to.delete();
                if (!from.renameTo(to)) {
                    try (FileInputStream fis = new FileInputStream(from);
                         FileOutputStream fos = new FileOutputStream(to)) {
                        Toolkit.IO.writeTo(fis, fos);
                    }
                }
            }, true);
        } catch (Throwable exception) {
            new IOException("Failed fetch latest firefox driver", exception).printStackTrace();
        }
    }

    static File parse() throws IOException {
        JsonObject obj;
        try (Reader reader = new InputStreamReader(new FileInputStream(firefox_cache), StandardCharsets.UTF_8)) {
            obj = JsonParser.parseReader(reader).getAsJsonObject();
        }
        String ver = obj.getAsJsonPrimitive("name").getAsString();
        JsonArray assetsArray = obj.getAsJsonArray("assets");
        Map<String, String> assets = new HashMap<>();
        for (JsonElement asset : assetsArray) {
            JsonObject assetAsJsonObject = asset.getAsJsonObject();
            assets.put(
                    assetAsJsonObject.getAsJsonPrimitive("name").getAsString(),
                    assetAsJsonObject.getAsJsonPrimitive("browser_download_url").getAsString()
            );
        }
        String except = null;
        String os = System.getProperty("os.name");
        if (os.toLowerCase().startsWith("windows")) {
            except = "-win32.zip";
        } else if (os.toLowerCase().startsWith("mac")) {
            except = "macos.tar.gz";
        } else if (os.startsWith("Linux")) {
            if (System.getProperty("os.arch").contains("64")) {
                except = "linux64.tar.gz";
            } else {
                except = "linux32.tar.gz";
            }

        }
        if (except == null) {
            throw new UnsupportedOperationException("Unsupported platform: " + except);
        }
        final String exc = except;
        Map.Entry<String, String> entry = assets.entrySet().stream().filter(it ->
                it.getKey().endsWith(exc)
        ).findFirst().get();
        File file = new File(MxSelenium.data, entry.getKey());
        NetKit.download(file, entry.getValue(), null, null);
        if (except.endsWith(".zip")) { // Windows NT
            File res = new File(MxSelenium.data, "geckodriver-v" + ver + ".exe");
            if (!res.isFile()) {
                try (ZipFile zip = new ZipFile(file)) {
                    try (OutputStream fos = new BufferedOutputStream(new FileOutputStream(res))) {
                        Toolkit.IO.writeTo(zip.getInputStream(zip.getEntry("geckodriver.exe")), fos);
                    }
                }
            }
            return res;
        } else { // Tar.GZ
            File res = new File(MxSelenium.data, "geckodriver-v" + ver);
            if (res.isFile()) {
                return res;
            }
            final TarGZipUnArchiver ua = new TarGZipUnArchiver();
            // Logging - as @Akom noted, logging is mandatory in newer versions, so you can use a code like this to configure it:
            ConsoleLoggerManager manager = new ConsoleLoggerManager();
            manager.initialize();
            ua.enableLogging(manager.getLoggerForComponent("bla"));
            // -- end of logging part
            ua.setSourceFile(file);
            File destDir = new File(MxSelenium.data, "tmp-dest");
            destDir.mkdirs();
            ua.setDestDirectory(destDir);
            ua.extract();
            File f = destDir.listFiles()[0];
            f.renameTo(res);
            res.setExecutable(true);
            return res;
        }
    }
}
