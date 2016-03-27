package com.vdzon.msw.cucumber.pages.view;

import com.vdzon.msw.cucumber.browsers.BrowserDriver;
import com.vdzon.msw.cucumber.pages.containers.HomePageContainer;
import org.openqa.selenium.support.PageFactory;

import java.util.logging.Logger;

public class HomeView {
    private static final Logger LOGGER = Logger.getLogger(HomeView.class.getName());
    private static final HomePageContainer homePageContainer = PageFactory.initElements(BrowserDriver.getCurrentDriver(), HomePageContainer.class);

    public static boolean isDisplayed() {
        LOGGER.info("Checking home page is displayed");
        BrowserDriver.waitForElementVisible(homePageContainer.pageTitle);
        return homePageContainer.pageTitle.isDisplayed();
    }
}
