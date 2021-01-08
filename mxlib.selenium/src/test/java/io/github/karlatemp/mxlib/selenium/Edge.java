package io.github.karlatemp.mxlib.selenium;

import org.openqa.selenium.edgehtml.EdgeHtmlOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

public class Edge {
    public static void main(String[] args) throws Exception {
        //AppX90nv6nhay5n6a98fnetv7tpk64pp35es
        // HKEY_CLASSES_ROOT\AppX90nv6nhay5n6a98fnetv7tpk64pp35es\Application

        // HKEY_CLASSES_ROOT\AppX90nv6nhay5n6a98fnetv7tpk64pp35es\Application
        //    ApplicationName    REG_SZ    @{Microsoft.MicrosoftEdge_44.18362.449.0_neutral__8wekyb3d8bbwe?ms-resource://Microsoft.MicrosoftEdge/Resources/AppName}
        //    ApplicationCompany    REG_SZ    Microsoft Corporation
        //    ApplicationIcon    REG_SZ    @{Microsoft.MicrosoftEdge_44.18362.449.0_neutral__8wekyb3d8bbwe?ms-resource://Microsoft.MicrosoftEdge/Files/Assets/MicrosoftEdgeSquare44x44.png}
        //    ApplicationDescription    REG_SZ    ms-resource:AppDescription
        //    AppUserModelID    REG_SZ    Microsoft.MicrosoftEdge_8wekyb3d8bbwe!MicrosoftEdge

        // HKEY_CLASSES_ROOT\ChromeHTML\Application
        //    AppUserModelId    REG_SZ    Chrome
        //    ApplicationIcon    REG_SZ    C:\Program Files (x86)\Google\Chrome\Application\chrome.exe,0
        //    ApplicationName    REG_SZ    Google Chrome
        //    ApplicationDescription    REG_SZ    访问互联网
        //    ApplicationCompany    REG_SZ    Google LLC
        System.out.println(WindowsKit.queryApplicationInfo("AppX90nv6nhay5n6a98fnetv7tpk64pp35es"));
        System.setProperty("mxlib.selenium.browser", "AppX90nv6nhay5n6a98fnetv7tpk64pp35es");
        System.setProperty("webdriver.edge.driver", "E:\\IDEAProjects\\MXLib\\MxLibData\\selenium\\msedgedriver.exe");
        RemoteWebDriver driver = MxSelenium.newDriver(null, options -> {
            EdgeHtmlOptions opt = (EdgeHtmlOptions) options;
            //opt.addPreference("general.useragent.override", agent);
            // opt.setBinary("C:\\Windows\\SystemApps\\Microsoft.MicrosoftEdge_8wekyb3d8bbwe\\MicrosoftEdge.exe");
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
        //new RemoteWebDriver(new URL("http://localhost:9515/"), new EdgeOptions());
    }
}
