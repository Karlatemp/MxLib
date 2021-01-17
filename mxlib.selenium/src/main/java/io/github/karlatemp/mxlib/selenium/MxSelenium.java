/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-selenium.main/MxSelenium.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.selenium;

import com.google.common.base.Splitter;
import io.github.karlatemp.mxlib.MxLib;
import io.github.karlatemp.mxlib.common.utils.IOUtils;
import io.github.karlatemp.mxlib.utils.Toolkit;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.*;
import java.nio.charset.Charset;
import java.time.Duration;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipFile;

import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class MxSelenium {
    static final Charset STD_CHARSET;
    static final File JAVA_EXECUTABLE;
    static final File data = new File(MxLib.getDataStorage(), "selenium");
    static final HttpClient client = HttpClientBuilder.create().build();
    static boolean IS_SUPPORT, DEBUG = System.getProperty("mxlib.selenium.debug") != null;

    public static boolean isSupported() {
        return IS_SUPPORT;
    }

    static {
        {
            File je;
            String sbt = System.getProperty("sun.boot.library.path");
            File exec_windows = new File(sbt, "java.exe");
            File exec = new File(sbt, "java");
            if (exec_windows.isFile()) {
                je = exec_windows;
            } else if (exec.exists() && exec.canExecute()) {
                je = exec;
            } else {
                String jhome = System.getProperty("java.home");
                exec_windows = new File(jhome, "bin/java.exe");
                exec = new File(jhome, "bin/java");
                if (exec_windows.isFile()) {
                    je = exec_windows;
                } else if (exec.exists() && exec.canExecute()) {
                    je = exec;
                } else {
                    je = null;
                }
            }
            JAVA_EXECUTABLE = je;
        }
        {
            Charset c;
            try {
                String property = System.getProperty("sun.stdout.encoding");
                if (property == null) {
                    if (JAVA_EXECUTABLE == null) {
                        throw new UnsupportedOperationException("Java Executable not found.");
                    }
                    try {
                        String result = commandProcessResult(true, JAVA_EXECUTABLE.getPath(), "-XshowSettings:properties", "-version");
                        Optional<String> fileEncoding = Splitter.on('\n').splitToList(result).stream()
                                .filter(it -> it.trim().startsWith("file.encoding"))
                                .findFirst();
                        String rex = fileEncoding.orElse(null);
                        if (rex != null) {
                            property = Splitter.on('=').limit(2)
                                    .splitToList(rex)
                                    .get(1)
                                    .trim();
                        }
                    } catch (Throwable ignore) {
                    }
                }
                //noinspection ConstantConditions
                c = Charset.forName(property);
            } catch (Throwable ignored) {
                try {
                    c = Charset.forName(System.getProperty("file.encoding"));
                } catch (Throwable ignored0) {
                    c = Charset.defaultCharset();
                }
            }
            STD_CHARSET = c;
        }
        data.mkdirs();
    }

    static String commandProcessResult(String... cmd) throws IOException {
        return commandProcessResult(false, cmd);
    }

    static String commandProcessResult(boolean errorStream, String... cmd) throws IOException {
        ProcessBuilder builder = new ProcessBuilder(cmd);
        if (errorStream) {
            builder.redirectOutput(Toolkit.IO.REDIRECT_DISCARD);
        } else {
            builder.redirectError(Toolkit.IO.REDIRECT_DISCARD);
        }
        Process process = builder.start();
        return new String(IOUtils.readAllBytes(
                errorStream ? process.getErrorStream() : process.getInputStream()
        ), STD_CHARSET == null ? Charset.defaultCharset() : STD_CHARSET);
    }

    private static boolean initialized;
    private static BiFunction<String, Consumer<Capabilities>, RemoteWebDriver> driverSupplier;

    public static void initialize() throws Exception {
        if (initialized) return;
        synchronized (MxSelenium.class) {
            if (initialized) return;
            try {
                initialize0();
            } catch (Throwable throwable) {
                driverSupplier = (a, b) -> {
                    throw new RuntimeException(throwable);
                };
                IS_SUPPORT = false;
                throw throwable;
            }
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    private static void initialize0() throws Exception {
        if (initialized) return;
        synchronized (MxSelenium.class) {
            if (initialized) return;
            initialized = true;
            String os = System.getProperty("os.name");
            if (os.toLowerCase().startsWith("windows ")) {
                String browser = WindowsKit.queryBrowserUsing();
                if (browser.startsWith("Chrome")) {
                    // region chrome
                    File bat = new File(data, "chromever.bat");
                    if (!bat.isFile()) {
                        data.mkdirs();
                        try (OutputStream out = new BufferedOutputStream(new FileOutputStream(bat));
                             InputStream res = MxSelenium.class.getResourceAsStream("resources/chromever.bat")) {
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

                    String driverVer = ChromeKit.getDriverVersion(chromeverx);
                    File chromedriverExecutable = NetKit.download(
                            new File(data, "chromedriver-" + driverVer + ".exe"),
                            ChromeKit.address + driverVer + "/chromedriver_win32.zip",
                            "chromedriver-" + driverVer + ".zip",
                            (tar, zipFile) -> {
                                try (ZipFile zip = new ZipFile(zipFile)) {
                                    try (InputStream inp = zip.getInputStream(zip.getEntry("chromedriver.exe"));
                                         OutputStream out = new BufferedOutputStream(new FileOutputStream(tar))) {
                                        Toolkit.IO.writeTo(inp, out);
                                    }
                                }
                            }
                    );
                    System.setProperty("webdriver.chrome.driver", chromedriverExecutable.getPath());
                    driverSupplier = (agent, c) -> {
                        ChromeOptions options = new ChromeOptions();
                        if (agent != null) options.addArguments("user-agent=" + agent);
                        if (c != null) c.accept(options);
                        return new ChromeDriver(options);
                    };
                    IS_SUPPORT = true;
                    // endregion
                } else if (browser.toLowerCase().startsWith("firefox")) {
                    FirefoxKit.fetch();
                    File provider = FirefoxKit.parse();
                    System.setProperty("webdriver.gecko.driver", provider.getPath());
                    driverSupplier = firefox();
                    IS_SUPPORT = true;
                } else {
                    Map<String, String> applicationInfo = WindowsKit.queryApplicationInfo(browser);
                    String appUserModelID = applicationInfo.get("AppUserModelID");
                    if (appUserModelID != null) {
                        if (appUserModelID.startsWith("Microsoft.MicrosoftEdge")) {
                            /*
                            // EdgeHTML cannot set UserAgent. not in support
                            driverSupplier = (agent, c) -> {
                                EdgeHtmlOptions options = new EdgeHtmlOptions();
                                if (c != null) c.accept(options);
                                return new EdgeHtmlDriver(options);
                            };
                            IS_SUPPORT = true;
                            */
                        }
                    }

                    if (!IS_SUPPORT) {
                        driverSupplier = (agent, c) -> {
                            throw new UnsupportedOperationException("Unsupported browser: " + browser + ", Only chrome/firefox supported");
                        };
                        IS_SUPPORT = false;
                    }
                }
            } else if (os.equals("Linux")) {
                try {
                    String type = commandProcessResult("xdg-settings", "get", "default-web-browser");
                    if (type.toLowerCase().startsWith("firefox")) {
                        FirefoxKit.fetch();
                        File provider = FirefoxKit.parse();
                        System.setProperty("webdriver.gecko.driver", provider.getPath());
                        driverSupplier = firefox();
                        IS_SUPPORT = true;
                    } else {
                        driverSupplier = (agent, c) -> {
                            throw new UnsupportedOperationException("Unsupported Platform: " + os + ", " + type + ", Only FireFox browser supported now.");
                        };
                        IS_SUPPORT = false;
                    }
                } catch (Throwable error) {
                    throw new UnsupportedOperationException("Unsupported Platform: " + os, error);
                }
            } else if (os.startsWith("Mac OS")) {
                try {
                    //noinspection ArraysAsListWithZeroOrOneArgument
                    Collection<String> supportedBrowsers = new HashSet<>(Arrays.asList(
                            "Google Chrome"
                    ));
                    //noinspection ArraysAsListWithZeroOrOneArgument
                    Collection<String> browsers = new HashSet<>(Arrays.asList(
                            "Safari" // Safari not for support. Safari cannot set UserAgent
                    ));
                    browsers.addAll(supportedBrowsers);
                    List<String> browsersInstalled = Stream.of(
                            Objects.requireNonNull(
                                    new File("/Applications").listFiles(),
                                    "`new File(\"/Applications\").listFiles()` is `null`"
                            )
                    )
                            .filter(it -> it.getName().endsWith(".app"))
                            .map(file -> {
                                String name = file.getName();
                                return name.substring(0, name.length() - 4);
                            })
                            .filter(browsers::contains)
                            .collect(Collectors.toList());
                    top:
                    for (String browser : browsersInstalled) {
                        switch (browser) {
                            // TODO: Firefox
                            case "Google Chrome": {
                                String ver = commandProcessResult(
                                        "/Applications/Google Chrome.app/Contents/MacOS/Google Chrome",
                                        "--version"
                                ).replace("Google Chrome", "").trim();
                                // chromedriver_mac64.zip
                                String driverVer = ChromeKit.getDriverVersion(ver);
                                File chromedriverExecutable = NetKit.download(
                                        new File(data, "chromedriver-" + driverVer + "-mac64"),
                                        ChromeKit.address + driverVer + "/chromedriver_mac64.zip",
                                        "chromedriver-" + driverVer + "-mac64.zip",
                                        (tar, zipFile) -> {
                                            try (ZipFile zip = new ZipFile(zipFile)) {
                                                try (InputStream inp = zip.getInputStream(zip.getEntry("chromedriver"));
                                                     OutputStream out = new BufferedOutputStream(new FileOutputStream(tar))) {
                                                    Toolkit.IO.writeTo(inp, out);
                                                }
                                            }
                                            tar.setExecutable(true);
                                        }
                                );
                                System.setProperty("webdriver.chrome.driver", chromedriverExecutable.getPath());

                                IS_SUPPORT = true;
                                driverSupplier = (agent, c) -> {
                                    ChromeOptions options = new ChromeOptions();
                                    if (agent != null) options.addArguments("user-agent=" + agent);
                                    if (c != null) c.accept(options);
                                    return new ChromeDriver(options);
                                };
                                break top;
                            }
                        }
                    }
                    if (!IS_SUPPORT) {
                        if (browsersInstalled.isEmpty()) {
                            driverSupplier = (agent, c) -> {
                                throw new UnsupportedOperationException("Unsupported Platform: " + os + ", No browser found. Please install one of the following browsers: " + supportedBrowsers);
                            };
                        } else {
                            driverSupplier = (agent, c) -> {
                                throw new UnsupportedOperationException("Unsupported Platform: " + os + ", No supported browser found. installed " + browsersInstalled + ". Please install one of the following browsers: " + supportedBrowsers);
                            };
                        }
                    }
                } catch (Throwable error) {
                    throw new UnsupportedOperationException("Unsupported Platform: " + os, error);
                }
            }
            if (driverSupplier == null) {
                driverSupplier = (agent, c) -> {
                    throw new UnsupportedOperationException("Unsupported Platform: " + os);
                };
                IS_SUPPORT = false;
            }
            if (!IS_SUPPORT) {
                driverSupplier.apply(null, null); // Throw error
            }
        }
    }

    private static BiFunction<String, Consumer<Capabilities>, RemoteWebDriver> firefox() {
        return (agent, c) -> {
            FirefoxOptions options = new FirefoxOptions();
            if (agent != null) options.addPreference("general.useragent.override", agent);
            if (c != null) c.accept(options);
            return new FirefoxDriver(options);
        };
    }

    public static RemoteWebDriver newDriver() {
        return newDriver(null);
    }

    public static RemoteWebDriver newDriver(String useragent) {
        return newDriver(useragent, null);
    }

    public static RemoteWebDriver newDriver(String useragent, Consumer<Capabilities> consumer) {
        try {
            initialize();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return driverSupplier.apply(useragent, consumer);
    }

    public static void main(String[] args) throws Throwable {
        WebDriver driver = newDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10000).getSeconds());
        driver.get("https://google.com/ncr");
        driver.findElement(By.name("q")).sendKeys("cheese" + Keys.ENTER);
        WebElement firstResult = wait.until(presenceOfElementLocated(By.cssSelector("h3>div")));
        System.out.println(firstResult.getAttribute("textContent"));
        driver.quit();
    }
}
