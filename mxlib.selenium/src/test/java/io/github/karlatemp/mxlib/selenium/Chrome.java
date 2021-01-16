/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-selenium.test/Chrome.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.selenium;

import org.openqa.selenium.remote.RemoteWebDriver;

public class Chrome {
    public static void main(String[] args) {
        System.setProperty("mxlib.selenium.browser", "Chrome");
        RemoteWebDriver driver = MxSelenium.newDriver(null, options -> {
        });
        try {
            while (true) {
                Thread.sleep(1000);
                System.out.println(driver.getWindowHandles());
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            driver.quit();
        }
    }
}
