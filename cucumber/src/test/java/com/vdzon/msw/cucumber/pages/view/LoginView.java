package com.vdzon.msw.cucumber.pages.view;

import com.vdzon.msw.cucumber.browsers.BrowserDriver;
import com.vdzon.msw.cucumber.pages.containers.HomePageContainer;
import com.vdzon.msw.cucumber.pages.containers.LoginPageContainer;
import junit.framework.Assert;
import org.openqa.selenium.support.PageFactory;

import java.util.logging.Logger;

public class LoginView {
    private static final Logger LOGGER = Logger.getLogger(LoginView.class.getName());
    private static final LoginPageContainer loginContainer = PageFactory.initElements(BrowserDriver.getCurrentDriver(), LoginPageContainer.class);
    private static final HomePageContainer homePageContainer = PageFactory.initElements(BrowserDriver.getCurrentDriver(), HomePageContainer.class);


    public static boolean isDisplayed() {
        LOGGER.info("Checking login page is displayed");
        if (!BrowserDriver.elementExists(loginContainer.loginDialog)) return false;
        return loginContainer.loginDialog.isDisplayed();
    }


    public static void login(String username, String password) {
        LOGGER.info("Logging in with username:" + username + " password:" + password);
        BrowserDriver.waitForElementVisible(loginContainer.loginDialog);
        loginContainer.usernameInput.clear();
        loginContainer.passwordInput.clear();
        loginContainer.usernameInput.sendKeys(username);
        loginContainer.passwordInput.sendKeys(password);
        loginContainer.submitButton.click();
        LOGGER.info("Login submitted");
    }

    public static void checkLoginErrors() {
        LOGGER.info("Check login errors displayed");
        BrowserDriver.waitForElementVisible(loginContainer.authFailedDiv);
        Assert.assertTrue(loginContainer.authFailedDiv.isDisplayed());
    }
}
