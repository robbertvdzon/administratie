package com.vdzon.msw.cucumber.browsers;

import com.vdzon.msw.cucumber.constants.Browsers;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;

import java.awt.*;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class BrowserFactory {

    private static final String BROWSER_PROP_KEY = "browser";

    /**
     * creates the browser driver specified in the system property "browser"
     * if no property is set then a firefox browser driver is created.
     * The allow properties are firefox, safari and chrome
     * e.g to run with safari, pass in the option -Dbrowser=safari at runtime
     *
     * @return WebDriver
     */
    public static WebDriver getBrowser() {
        Browsers browser;
        WebDriver driver;

        if (System.getProperty(BROWSER_PROP_KEY) == null) {
            browser = Browsers.FIREFOX;
        } else {
            browser = Browsers.browserForName(System.getProperty(BROWSER_PROP_KEY));
        }
        switch (browser) {
            case REMOTE:
                driver = createRemoteDriver();
                break;
            case CHROME:
                driver = createChromeDriver();
                break;
            case SAFARI:
                driver = createSafariDriver();
                break;
            case FIREFOX:
            default:
                driver = createFirefoxDriver(getFirefoxProfile());
                break;
        }
        addAllBrowserSetup(driver);
        return driver;
    }

    private static WebDriver createSafariDriver() {
        return new SafariDriver();
    }

    private static WebDriver createChromeDriver() {
        System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver");
        return new ChromeDriver();
    }

    private static WebDriver createFirefoxDriver(FirefoxProfile firefoxProfile) {
        File pathToBinary = new File("C:\\apps\\FirefoxPortable\\firefoxportable.exe");
        FirefoxBinary ffBinary = new FirefoxBinary(pathToBinary);
        return new FirefoxDriver(ffBinary, firefoxProfile);
    }

    public static WebDriver createRemoteDriver() {
        System.out.println("***** CREATE REMOTE DRIVER 1");
        DesiredCapabilities capability = DesiredCapabilities.firefox();
//        DesiredCapabilities capability = DesiredCapabilities.internetExplorer();
        RemoteWebDriver driver = null;
        String seleniumurl = System.getProperty("seleniumurl");
        if (seleniumurl==null){
            seleniumurl = "http://localhost:4444/wd/hub";
        }
        System.out.println("Selanium server:"+seleniumurl);

        try {
            driver = new RemoteWebDriver(new URL(seleniumurl), capability);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
        driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
        driver.manage().window().setSize(new Dimension(1920, 1080));
        return driver;
    }

    private static WebDriver createFirefoxDriverLocal(FirefoxProfile firefoxProfile) {
        File pathToBinary = new File("C:\\apps\\FirefoxPortable\\firefoxportable.exe");
        FirefoxBinary ffBinary = new FirefoxBinary(pathToBinary);
        return new FirefoxDriver(ffBinary, firefoxProfile);
    }

    private static FirefoxProfile getFirefoxProfile() {
        FirefoxProfile firefoxProfile = new FirefoxProfile();
        return firefoxProfile;
    }

    private static void addAllBrowserSetup(WebDriver driver) {
        driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
        driver.manage().window().setPosition(new Point(0, 0));
        java.awt.Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dim = new Dimension((int) screenSize.getWidth(), (int) screenSize.getHeight());
        driver.manage().window().setSize(dim);
    }

}
