package com.vdzon.msw.cucumber.browsers;

import org.openqa.selenium.*;
import org.openqa.selenium.remote.UnreachableBrowserException;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class BrowserDriver {
    private static final Logger LOGGER = Logger.getLogger(BrowserDriver.class.getName());
    private static WebDriver mDriver;

    public synchronized static WebDriver getCurrentDriver() {
        if (mDriver == null) {
            try {
                mDriver = BrowserFactory.getBrowser();
                System.out.println("succeeded");
            } catch (UnreachableBrowserException e) {
                e.printStackTrace();
                System.out.println("try to get the browser again!");
                mDriver = BrowserFactory.getBrowser();
                System.out.println("succeeded");
            } catch (WebDriverException e) {
                e.printStackTrace();
                System.out.println("try to get the browser again!");
                mDriver = BrowserFactory.getBrowser();
                System.out.println("succeeded");
            } finally {
                System.out.println("add shutdown hook");
                Runtime.getRuntime().addShutdownHook(new Thread(new BrowserCleanup()));
            }
        }
        return mDriver;
    }

    public static void close() {
        try {
            getCurrentDriver().quit();
            mDriver = null;
            LOGGER.info("closing the browser");
        } catch (UnreachableBrowserException e) {
            LOGGER.info("cannot close browser: unreachable browser");
        }
    }

    private static class BrowserCleanup implements Runnable {
        public void run() {
            close();
        }
    }

    public static void loadPage(String url) {
        getCurrentDriver();
        LOGGER.info("try to loadPage [" + url + "]");
        getCurrentDriver().get(url);
    }

    public static boolean elementExists(WebElement element) {
        getCurrentDriver().manage().timeouts().implicitlyWait(0, TimeUnit.MILLISECONDS);
        try {
            return getCurrentDriver().findElements(By.id(element.getAttribute("id"))).size() != 0;
        } catch (Exception ex) {
            return false;
        } finally {
            getCurrentDriver().manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
        }
    }

    public static WebElement waitForElementVisible(WebElement elementToWaitFor) {
        return waitForElementVisible(elementToWaitFor, null);
    }

    public static WebElement waitForElementVisible(WebElement elementToWaitFor, Integer waitTimeInSeconds) {
        if (waitTimeInSeconds == null) {
            waitTimeInSeconds = 10;
        }

        WebDriverWait wait = new WebDriverWait(getCurrentDriver(), waitTimeInSeconds);
        return wait.until(ExpectedConditions.visibilityOf(elementToWaitFor));
    }

    public static void waitForLoad() {
        ExpectedCondition<Boolean> pageLoadCondition = new
                ExpectedCondition<Boolean>() {
                    public Boolean apply(WebDriver driver) {
                        return ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete");
                    }
                };
        WebDriverWait wait = new WebDriverWait(getCurrentDriver(), 30);
        wait.until(pageLoadCondition);
    }

    protected static Object executeScript(final String script) {
        return ((JavascriptExecutor) getCurrentDriver()).executeScript(script);
    }


}

