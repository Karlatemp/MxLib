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
import io.github.karlatemp.mxlib.logger.MLogger;
import io.github.karlatemp.mxlib.utils.Toolkit;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;

@SuppressWarnings({"ResultOfMethodCallIgnored", "unchecked", "rawtypes"})
public class MxSelenium {
    static final Charset STD_CHARSET;
    static final File JAVA_EXECUTABLE;
    static final File data = new File(MxLib.getDataStorage(), "selenium");
    static final HttpClient client = HttpClientBuilder.create().build();
    static boolean IS_SUPPORT, DEBUG = System.getProperty("mxlib.selenium.debug") != null;

    public static boolean isSupported() {
        return IS_SUPPORT;
    }

    static MLogger getLogger() {
        return MxLib.getLoggerOrStd("MxLib Selenium");
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

    private static volatile boolean initialized;
    private static Collection<SeleniumProvider<?, ?>> providers;
    private static SeleniumProvider<?, ?> defaultProvider;

    static Collection<String> setOf(String... urls) {
        ArrayList<String> result = new ArrayList<>(urls.length);
        for (String u : urls) {
            if (!result.contains(u))
                result.add(u);
        }
        return result;
    }

    public static void initialize() {
        if (initialized) return;
        synchronized (MxSelenium.class) {
            if (initialized) return;
            initialize0();
        }
    }

    /**
     * @since 3.0-dev-21
     */
    @Deprecated
    public static void reinitialize() {
        initialized = false;
        initialize();
    }

    private static void initialize0() {
        providers = MSProviderCollect.collectAll();
        for (SeleniumProvider<?, ?> provider : providers) {
            if (provider.isSysDefault()) {
                defaultProvider = provider;
                return;
            }
        }
        for (SeleniumProvider<?, ?> provider : providers) {
            if (!(provider instanceof ExceptionProvider)) {
                defaultProvider = provider;
                return;
            }
        }
        String errMsg = null;
        for (SeleniumProvider<?, ?> provider : providers) {
            if ((errMsg = ((ExceptionProvider) provider).msg) != null) {
                break;
            }
        }
        String errMsgFinal = errMsg;
        defaultProvider = new SimpleSeleniumProvider<>(
                "ERROR",
                RemoteWebDriver.class,
                Capabilities.class,
                false, false,
                (a, b) -> {
                    UnsupportedOperationException err0 = new UnsupportedOperationException(errMsgFinal);
                    for (ExceptionProvider provider : (Collection<ExceptionProvider>) (Collection) providers) {
                        Throwable exception = provider.exception;
                        if (exception != null) err0.addSuppressed(exception);
                    }
                    throw err0;
                }
        );
    }

    public static RemoteWebDriver newDriver() {
        initialize();
        return defaultProvider.newDriver();
    }

    public static RemoteWebDriver newDriver(String useragent) {
        initialize();
        return defaultProvider.newDriver(useragent);
    }

    public static Class<? extends RemoteWebDriver> getDriverClass() {
        initialize();
        return defaultProvider.getDriverClass();
    }

    public static RemoteWebDriver newDriver(String useragent, Consumer<Capabilities> consumer) {
        initialize();
        return defaultProvider.newDriver(useragent, (Consumer) consumer);
    }

    /**
     * @since 3.0-dev-21
     */
    public static SeleniumProvider<?, ?> getDefaultProvider() {
        initialize();
        return defaultProvider;
    }

    /**
     * @since 3.0-dev-21
     */
    public static Collection<SeleniumProvider<?, ?>> getProviders() {
        initialize();
        return providers.stream()
                .filter(it -> !(it instanceof ExceptionProvider))
                .collect(Collectors.toList());
    }

    /**
     * @since 3.0-dev-21
     */
    public static Collection<Throwable> getInitExceptions() {
        initialize();
        return providers.stream()
                .map(it -> {
                    if (it instanceof ExceptionProvider) {
                        return ((ExceptionProvider) it).exception;
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
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
