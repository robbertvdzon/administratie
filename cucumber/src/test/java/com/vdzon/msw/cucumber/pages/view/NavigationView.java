package com.vdzon.msw.cucumber.pages.view;

import com.vdzon.msw.cucumber.browsers.BrowserDriver;
import com.vdzon.msw.cucumber.pages.containers.HomePageContainer;
import com.vdzon.msw.cucumber.pages.containers.LoginPageContainer;
import com.vdzon.msw.cucumber.pages.containers.NavigationPageContainer;
import org.openqa.selenium.support.PageFactory;

import java.util.logging.Logger;

public class NavigationView {
    private static final Logger LOGGER = Logger.getLogger(NavigationView.class.getName());
    private static final NavigationPageContainer navigationPageContainer = PageFactory.initElements(BrowserDriver.getCurrentDriver(), NavigationPageContainer.class);


    public static boolean isLogoffButtonVisible() {
        LOGGER.info("Check logoff button visible");
        return navigationPageContainer.logoff_button.isDisplayed();
    }

    public static boolean isLoginButtonVisible() {
        LOGGER.info("Check login button visible");
        return navigationPageContainer.login_button.isDisplayed();
    }

    public static void pressLoginButton() {
        LOGGER.info("Press login");
        BrowserDriver.waitForLoad();
        BrowserDriver.waitForElementVisible(navigationPageContainer.login_button);
        navigationPageContainer.login_button.click();
    }

    public static void waitForLogoffButton() {
        BrowserDriver.waitForElementVisible(navigationPageContainer.logoff_button);
    }

    public static void pressMainButtonCompetition() {
        LOGGER.info("Press competition");
        BrowserDriver.waitForElementVisible(navigationPageContainer.competitions_main_button);
        navigationPageContainer.competitions_main_button.click();
    }
}
