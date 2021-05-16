/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-selenium.main/ChromeKit.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.selenium;

import io.github.karlatemp.mxlib.logger.MLogger;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class ChromeKit {
    static final String address;
    static final String GOOGLE_API = "https://chromedriver.storage.googleapis.com/";
    static final String TAOBAO_MIRROR = "http://npm.taobao.org/mirrors/chromedriver/";


    static {
        MLogger logger = MxSelenium.getLogger();
        if (System.getProperty("mxlib.selenium.chrome.no-mirror") != null) {
            address = GOOGLE_API;
            logger.info("Google Chrome Driver download mirror was disabled by `mxlib.selenium.chrome.no-mirror`");
            logger.info("Using default location: " + GOOGLE_API);
        } else {
            String mirror = System.getProperty("mxlib.selenium.chrome.mirror");
            if (mirror != null) {
                if (mirror.endsWith("/")) {
                    address = mirror;
                } else {
                    address = mirror + '/';
                }
                logger.info("Google Chrome Driver download mirror set by `mxlib.selenium.chrome.mirror`");
                logger.info("Using " + mirror);
            } else {
                if (Locale.getDefault().equals(Locale.SIMPLIFIED_CHINESE)) {
                    address = TAOBAO_MIRROR;
                    logger.info("Running on 中国-大陆");
                    logger.info("Using taoboo mirror default");
                } else {
                    logger.info("Using default location");
                    address = GOOGLE_API;
                }
                logger.info("MxLib will download chrome driver from " + address);
                logger.info("Change it with `-Dmxlib.selenium.chrome.mirror=.....` vm option");
                logger.info("Or disable it with `-Dmxlib.selenium.chrome.no-mirror`");
            }
        }
    }

    static String getDriverVersion(String chromever) throws IOException {
        String cver = chromever;
        File verfile = new File(MxSelenium.data, "chromedriver-" + chromever + ".mapping");
        if (verfile.isFile()) {
            try (BufferedReader bis = new BufferedReader(new FileReader(verfile))) {
                return bis.readLine();
            }
        }
        MLogger logger = MxSelenium.getLogger();
        List<Throwable> throwables = new ArrayList<>(5);
        while (true) {
            try {
                String uri;
                if (chromever.isEmpty()) {
                    uri = address + "LATEST_RELEASE";
                } else {
                    uri = address + "LATEST_RELEASE_" + chromever;
                }
                logger.info("Fetching chrome driver version from " + uri);
                HttpResponse response = MxSelenium.client.execute(new HttpGet(uri));
                if (response.getStatusLine().getStatusCode() != 200) {
                    response.getEntity().getContent().close();
                    throw new IOException("Response code not 200, " + response.getStatusLine().getStatusCode());
                }
                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(response.getEntity().getContent()));
                     Writer writer = new FileWriter(verfile)
                ) {
                    String l = reader.readLine();
                    logger.info("Driver version is " + l);
                    writer.write(l);
                    return l;
                }
            } catch (IOException ioException) {
                throwables.add(ioException);
            }
            if (chromever.isEmpty()) {
                IOException ioException = new IOException("Cannot get driver version of " + cver);
                for (Throwable t : throwables) ioException.addSuppressed(t);
                throw ioException;
            } else {
                int ln = chromever.lastIndexOf('.');
                if (ln == -1) chromever = "";
                else chromever = chromever.substring(0, ln);
            }
        }
    }

    static List<String[]> windowsVerCommands = Stream.concat(
            Stream.of(
                    //:: Look for machine-wide Chrome installs (stable, Beta, and Dev).
                    //:: Get the name, running version (if an update is pending relaunch), and
                    //:: installed version of each.
                    "{8A69D345-D564-463c-AFF1-A69D9E530F96}",
                    "{8237E44A-0054-442C-B6B6-EA0509993955}",
                    "{401C381F-E0DE-4B85-8BD8-3F3F14FBDA57}"
            ).flatMap(entry -> {
                //  reg query "HKLM\Software\Google\Update\Clients\%%A" /v name "/reg:32" 2>NUL
                //  reg query "HKLM\Software\Google\Update\Clients\%%A" /v opv "/reg:32" 2>NUL
                //  reg query "HKLM\Software\Google\Update\Clients\%%A" /v pv "/reg:32" 2>NUL
                return Stream.of(
                        new String[]{"reg", "query", "HKLM\\Software\\Google\\Update\\Clients\\" + entry, "/v", "name", "\"/reg:32\""},
                        new String[]{"reg", "query", "HKLM\\Software\\Google\\Update\\Clients\\" + entry, "/v", "opv", "\"/reg:32\""},
                        new String[]{"reg", "query", "HKLM\\Software\\Google\\Update\\Clients\\" + entry, "/v", "pv", "\"/reg:32\""}
                );
            }),
            Stream.of(
                    //:: Look for Chrome installs in the current user's %LOCALAPPDATA% directory
                    //:: (stable, Beta, Dev, and canary).
                    //:: Get the name, running version (if an update is pending relaunch), and
                    //:: installed version of each.
                    "{8A69D345-D564-463c-AFF1-A69D9E530F96}",
                    "{8237E44A-0054-442C-B6B6-EA0509993955}",
                    "{401C381F-E0DE-4B85-8BD8-3F3F14FBDA57}",
                    "{4ea16ac7-fd5a-47c3-875b-dbf4a2008c20}"
            ).flatMap(entry -> {
                // reg query "HKCU\Software\Google\Update\Clients\%%A" /v name "/reg:32" 2>NUL
                // reg query "HKCU\Software\Google\Update\Clients\%%A" /v opv "/reg:32" 2>NUL
                // reg query "HKCU\Software\Google\Update\Clients\%%A" /v pv "/reg:32" 2>NUL
                return Stream.of(
                        new String[]{"reg", "query", "HKCU\\Software\\Google\\Update\\Clients\\" + entry, "/v", "name", "\"/reg:32\""},
                        new String[]{"reg", "query", "HKCU\\Software\\Google\\Update\\Clients\\" + entry, "/v", "opv", "\"/reg:32\""},
                        new String[]{"reg", "query", "HKCU\\Software\\Google\\Update\\Clients\\" + entry, "/v", "pv", "\"/reg:32\""}
                );
            })
    ).collect(Collectors.toList());
}
