/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-selenium.main/SeleniumProvider.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.selenium;

import io.github.karlatemp.mxlib.reflect.Reflections;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.util.function.BiFunction;
import java.util.function.Consumer;

/**
 * @since 3.0-dev-21
 */
public interface SeleniumProvider<T extends RemoteWebDriver, C extends Capabilities> {
    public Class<T> getDriverClass();

    public boolean isSysDefault();

    public Class<?> getCapabilitiesClass();

    public boolean isUserAgentSupported();

    public default T newDriver() {
        return newDriver(null, null);
    }

    public default T newDriver(String useragent) {
        return newDriver(useragent, null);
    }

    public default T newDriver(Consumer<C> consumer) {
        return newDriver(null, consumer);
    }

    public T newDriver(String useragent, Consumer<C> consumer);

    public String getName();
}

abstract class AbstractSeleniumProvider<T extends RemoteWebDriver, C extends Capabilities> implements SeleniumProvider<T, C> {
    private final Class<T> driver;
    private final String name;
    private final Class<C> capabilities;
    private final boolean sysDefault;
    private final boolean userAgentSupport;

    AbstractSeleniumProvider(
            String name,
            Class<T> driver,
            Class<C> capabilities,
            boolean sysDefault,
            boolean userAgentSupport
    ) {
        this.name = name;
        this.driver = driver;
        this.capabilities = capabilities;
        this.sysDefault = sysDefault;
        this.userAgentSupport = userAgentSupport;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Class<T> getDriverClass() {
        return driver;
    }

    @Override
    public boolean isSysDefault() {
        return sysDefault;
    }

    @Override
    public Class<C> getCapabilitiesClass() {
        return capabilities;
    }

    @Override
    public boolean isUserAgentSupported() {
        return userAgentSupport;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "name = '" + name + '\'' +
                ", driver=" + driver.getSimpleName() +
                ", capabilities=" + capabilities.getSimpleName() +
                ", sysDefault=" + sysDefault +
                ", userAgentSupport=" + userAgentSupport +
                '}';
    }
}

class SimpleSeleniumProvider<T extends RemoteWebDriver, C extends Capabilities> extends AbstractSeleniumProvider<T, C> {

    private final BiFunction<String, Consumer<C>, T> alloc;

    SimpleSeleniumProvider(
            String name,
            Class<T> driver,
            Class<C> capabilities,
            boolean sysDefault,
            boolean userAgentSupport,
            BiFunction<String, Consumer<C>, T> alloc
    ) {
        super(name, driver, capabilities, sysDefault, userAgentSupport);
        this.alloc = alloc;
    }

    @Override
    public T newDriver(String useragent, Consumer<C> consumer) {
        boolean overrideCCL = false;
        Thread currentThread = Thread.currentThread();
        ClassLoader ccl = null;
        try {
            ccl = currentThread.getContextClassLoader();
            currentThread.setContextClassLoader(Reflections.getClassLoader(getClass()));
            overrideCCL = true;
        } catch (Throwable ignore) {
        }
        try {
            return alloc.apply(useragent, consumer);
        } finally {
            if (overrideCCL) {
                try {
                    currentThread.setContextClassLoader(ccl);
                } catch (Throwable ignore) {
                }
            }
        }
    }
}

class ExceptionProvider implements SeleniumProvider<RemoteWebDriver, Capabilities> {
    final Throwable exception;
    final String msg;

    ExceptionProvider(Throwable exception) {
        this.exception = exception;
        this.msg = null;
    }

    ExceptionProvider(String msg) {
        exception = null;
        this.msg = msg;
    }

    @Override
    public boolean isSysDefault() {
        return false;
    }

    @Override
    public Class<RemoteWebDriver> getDriverClass() {
        return null;
    }

    @Override
    public Class<?> getCapabilitiesClass() {
        return null;
    }

    @Override
    public boolean isUserAgentSupported() {
        return false;
    }

    @Override
    public RemoteWebDriver newDriver(String useragent, Consumer<Capabilities> consumer) {
        return null;
    }

    @Override
    public String getName() {
        if (msg != null) return "SysNoProviderMsg: '" + msg + '\'';
        return "Exception: " + exception.getClass().getName();
    }

    @Override
    public String toString() {
        if (msg != null) {
            return "SysNoProviderMsg{" + msg + "}";
        }
        return "ExceptionProvider{" +
                "exception=" + exception +
                '}';
    }
}
