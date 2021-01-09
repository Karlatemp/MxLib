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
