/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-selenium.main/MSProviderCollect.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.selenium;

import io.github.karlatemp.mxlib.utils.Toolkit;
import org.apache.commons.exec.CommandLine;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeDriverService;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.GeckoDriverService;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.Consumer;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static io.github.karlatemp.mxlib.selenium.MxSelenium.*;

class MSProviderCollect {
    static SeleniumProvider<?, ?> firefoxProvider(File provider, boolean sysDef, String exec, String application) {
        return new SimpleSeleniumProvider<>(
                application,
                FirefoxDriver.class,
                FirefoxOptions.class,
                sysDef,
                true,
                (agent, c) -> {
                    FirefoxOptions options = new FirefoxOptions();
                    if (exec != null) {
                        options.setBinary(exec);
                    }
                    if (agent != null) options.addPreference("general.useragent.override", agent);
                    if (c != null) c.accept(options);
                    return new FirefoxDriver(
                            new GeckoDriverService.Builder()
                                    .usingFirefoxBinary(options.getBinary())
                                    .usingDriverExecutable(provider)
                                    .build(),
                            options
                    );
                }
        );
    }

    static SeleniumProvider<?, ?> chromeProvider(File provider, boolean sysDef, String exec, String application) {
        return new SimpleSeleniumProvider<>(
                application,
                ChromeDriver.class,
                ChromeOptions.class,
                sysDef, true,
                (agent, c) -> {
                    ChromeOptions options = new ChromeOptions();
                    if (agent != null) options.addArguments("user-agent=" + agent);
                    if (c != null) c.accept(options);
                    return new ChromeDriver(
                            new ChromeDriverService.Builder()
                                    .withLogLevel(options.getLogLevel())
                                    .usingDriverExecutable(provider)
                                    .build(),
                            options
                    );
                }
        );
    }

    static String exec(String cmd) {
        return CommandLine.parse(cmd).getExecutable();
    }

    void collect(Consumer<Callable<SeleniumProvider<?, ?>>> emit) throws Exception {
    }

    String errorMsg(String os) {
        return null;
    }

    static Collection<SeleniumProvider<?, ?>> collectAll() {
        Collection<SeleniumProvider<?, ?>> rsp = new ArrayList<>();
        Collection<Callable<SeleniumProvider<?, ?>>> tasks = new ConcurrentLinkedDeque<>();


        String os = System.getProperty("os.name");

        ExecutorService service = Executors.newFixedThreadPool(5);
        MSProviderCollect[] providers = new MSProviderCollect[]{
                new WinNtMSP(),
                new MacOsMSP(),
                new LinuxMSP(),
        };
        try {
            MSProviderCollect provider = null;
            String errorMsg = null;
            {
                Consumer<Callable<SeleniumProvider<?, ?>>> emit = tasks::add;

                for (MSProviderCollect p : providers) {
                    errorMsg = p.errorMsg(os);
                    if (errorMsg != null) {
                        provider = p;
                        break;
                    }
                }
                if (provider == null) {
                    rsp.add(new ExceptionProvider("Cannot detect drivers for system " + os));
                    return rsp;
                }
                try {
                    provider.collect(emit);
                } catch (Exception e) {
                    rsp.add(new ExceptionProvider(e));
                }
            }

            List<Future<SeleniumProvider<?, ?>>> list = service.invokeAll(tasks);
            for (Future<SeleniumProvider<?, ?>> ft : list) {
                try {
                    rsp.add(ft.get());
                } catch (ExecutionException e) {
                    rsp.add(new ExceptionProvider(e));
                }
            }

            rsp.add(new ExceptionProvider(errorMsg + ", os: " + os + ", arch: " + System.getProperty("os.arch") + ", " + System.getProperty("os.version")));
        } catch (InterruptedException e) {
            rsp.add(new ExceptionProvider(e));
        } finally {
            service.shutdown();
        }
        return rsp;
    }
}

class WinNtMSP extends MSProviderCollect {
    String errorMsg(String os) {
        if (!os.toLowerCase().startsWith("windows ")) return null;
        return "No supported browser detected, please install one of < Google Chrome / Mozilla Firefox / Microsoft Edge >";
    }

    @Override
    void collect(Consumer<Callable<SeleniumProvider<?, ?>>> emit) throws Exception {

        String sysBrowser = WindowsKit.queryBrowserUsing();

        Map<String, Map<String, String>> HKEY_CLASSES_ROOT = WindowsKit.parseRegResult(
                commandProcessResult("reg", "query", "HKEY_CLASSES_ROOT")
        );
        Set<String> collected = new HashSet<>();
        boolean hasChrome = false;
        for (String HKEY_CLASSES_ROOT$APP : HKEY_CLASSES_ROOT.keySet()) {
            String application = HKEY_CLASSES_ROOT$APP.replace("HKEY_CLASSES_ROOT\\", "");
            boolean isSysDefault = sysBrowser.equalsIgnoreCase(application);
            if (application.startsWith("Chrome")) {
                if (hasChrome) continue;
                hasChrome = true;

                File bat = new File(data, "chromever.bat");
                if (!bat.isFile()) {
                    data.mkdirs();
                    try (OutputStream out = new BufferedOutputStream(new FileOutputStream(bat));
                         InputStream res = MxSelenium.class.getResourceAsStream("resources/chromever.bat")) {
                        assert res != null;
                        Toolkit.IO.writeTo(res, out);
                    }
                }

                String result = commandProcessResult("cmd", "/c", bat.getPath());
                if (result.trim().isEmpty()) {
                    StringBuilder builder = new StringBuilder();
                    for (String[] cmd : ChromeKit.windowsVerCommands) {
                        builder.append(commandProcessResult(cmd));
                        builder.append('\n').append('\n').append('\n');
                    }
                    result = builder.toString();
                }
                String chromeverx = System.getProperty("mxlib.selenium.chrome.version");
                if (chromeverx == null) {
                    Map<String, Map<String, String>> chromever = WindowsKit.parseRegResult(result);
                    Iterator<Map<String, String>> iterator = chromever.values().iterator();
                    if (!iterator.hasNext()) {
                        throw new UnsupportedOperationException("Unable to confirm the version of Chrome. " +
                                "Please set chrome version with `-Dmxlib.selenium.chrome.version=VERSION`. " +
                                "And report to https://github.com/Karlatemp/MxLib");
                    }
                    Map<String, String> next = iterator.next();
                    String ver = next.getOrDefault("opv", next.get("pv"));
                    if (ver == null) {
                        throw new UnsupportedOperationException("Unable to confirm the version of Chrome. " +
                                "Please set chrome version with `-Dmxlib.selenium.chrome.version=VERSION`. " +
                                "And report to https://github.com/Karlatemp/MxLib\n" +
                                result);
                    }
                    chromeverx = ver;
                }
                String chromeverx_f = chromeverx;

                emit.accept(() -> {
                    String driverVer = ChromeKit.getDriverVersion(chromeverx_f);
                    File chromedriverExecutable = NetKit.download(
                            new File(data, "chromedriver-" + driverVer + ".exe"),
                            setOf(ChromeKit.address + driverVer + "/chromedriver_win32.zip", ChromeKit.GOOGLE_API + driverVer + "/chromedriver_win32.zip"),
                            "chromedriver-" + driverVer + ".zip",
                            FileKit.unzip("chromedriver.exe")
                    );
                    return chromeProvider(chromedriverExecutable, isSysDefault, null, application);
                });
            }

            if (application.toLowerCase().startsWith("firefox")) {
                String exec = exec(WindowsKit.queryProcOpenCommand(application));
                if (!collected.add(exec)) continue;


                emit.accept(() -> {
                    FirefoxKit.fetch();
                    File provider = FirefoxKit.parse();

                    return firefoxProvider(provider, isSysDefault, exec, application);
                });
            }

            if (application.toLowerCase().startsWith("msedge")) {

                String exec = exec(WindowsKit.queryProcOpenCommand(application));
                if (!collected.add(exec)) continue;
                emit.accept(() -> {
                    File dir = new File(exec).getParentFile();
                    Pattern pt = Pattern.compile("[0-9]+(?:\\.[0-9]+)*");
                    String ver = Stream.of(
                                    Objects.requireNonNull(dir.list(), "No files in " + dir)
                            )
                            .filter(it -> pt.matcher(it).matches())
                            .findFirst()
                            .orElse(null);
                    if (ver == null)
                        throw new UnsupportedOperationException("Version of " + application + " not found in " + dir);

                    File msedgedriverExecutable = NetKit.download(
                            new File(data, "msedgedriver-" + ver + ".exe"),
                            "https://msedgedriver.azureedge.net/" + ver + "/edgedriver_win32.zip",
                            "msedgedriver-" + ver + "-win32.zip",
                            FileKit.unzip("msedgedriver.exe")
                    );
                    return new SimpleSeleniumProvider<>(
                            application,
                            EdgeDriver.class,
                            EdgeOptions.class,
                            isSysDefault, true,
                            (agent, c) -> {
                                EdgeOptions options = new EdgeOptions();
                                if (agent != null) options.addArguments("user-agent=" + agent);
                                options.setBinary(exec);
                                if (c != null) c.accept(options);
                                return new EdgeDriver(
                                        new EdgeDriverService.Builder()
                                                .usingDriverExecutable(msedgedriverExecutable)
                                                .build(),
                                        options
                                );
                            }
                    );
                });
            }
        }
    }
}

class LinuxMSP extends MSProviderCollect {
    @Override
    String errorMsg(String os) {
        if (!os.equals("Linux")) return null;
        return "Only firefox supported now";
    }

    @Override
    void collect(Consumer<Callable<SeleniumProvider<?, ?>>> emit) throws Exception {
        // TODO: Chrome
        String type = commandProcessResult("xdg-settings", "get", "default-web-browser");
        if (type.toLowerCase().startsWith("firefox")) {
            emit.accept(() -> {
                FirefoxKit.fetch();
                File provider = FirefoxKit.parse();
                return firefoxProvider(
                        provider, true,
                        null, type
                );
            });
        }
    }
}

class MacOsMSP extends MSProviderCollect {
    @Override
    String errorMsg(String os) {
        if (!os.startsWith("Mac OS")) return null;
        return "Only Google Chrome supported now";
    }

    @Override
    void collect(Consumer<Callable<SeleniumProvider<?, ?>>> emit) throws Exception {
        Iterator<File> sysAppIterator = Stream.of(Objects.requireNonNull(
                        new File("/Applications").listFiles(),
                        "`new File(\"/Applications\").listFiles()` is `null`"
                ))
                .filter(it -> it.getName().endsWith(".app"))
                .iterator();
        while (sysAppIterator.hasNext()) {
            File sysApp = sysAppIterator.next();

            switch (sysApp.getName()) {
                case "Google Chrome.app": {
                    emit.accept(() -> {

                        String ver = commandProcessResult(
                                "/Applications/Google Chrome.app/Contents/MacOS/Google Chrome",
                                "--version"
                        ).replace("Google Chrome", "").trim();
                        String driverVer = ChromeKit.getDriverVersion(ver);
                        File chromedriverExecutable = NetKit.download(
                                new File(data, "chromedriver-" + driverVer + "-mac64"),
                                setOf(ChromeKit.address + driverVer + "/chromedriver_mac64.zip", ChromeKit.GOOGLE_API + driverVer + "/chromedriver_mac64.zip"),
                                "chromedriver-" + driverVer + "-mac64.zip",
                                FileKit.unzip("chromedriver")
                        );
                        return chromeProvider(
                                chromedriverExecutable,
                                false, null,
                                sysApp.getName()
                        );
                    });
                    break;
                }
            }
            // TODO: Firefox
        }
    }
}

class MSPCTestMain {
    public static void main(String[] args) throws Throwable {
        Collection<SeleniumProvider<?, ?>> providers = MSProviderCollect.collectAll();
        for (SeleniumProvider<?, ?> prov : providers) {
            System.out.println(prov);
            if (prov instanceof ExceptionProvider) {
                Throwable exception = ((ExceptionProvider) prov).exception;
                if (exception != null)
                    exception.printStackTrace(System.out);
            }
            /*if (prov.getName().startsWith("MSEdge")) {
                RemoteWebDriver driver = prov.newDriver();
                driver.get("http://www.baidu.com");
                Thread.sleep(10000);
                driver.close();
            }*/
        }
    }
}
