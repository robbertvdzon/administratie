package com.vdzon.msw.cucumber.pages.view;

import com.vdzon.msw.cucumber.browsers.BrowserDriver;
import com.vdzon.msw.cucumber.pages.containers.CompetitionPageContainer;
import org.openqa.selenium.support.PageFactory;

import java.util.logging.Logger;

public class CompetitionView {
    private static final Logger LOGGER = Logger.getLogger(CompetitionView.class.getName());
    private static final CompetitionPageContainer competitionPageContainer = PageFactory.initElements(BrowserDriver.getCurrentDriver(), CompetitionPageContainer.class);

    public static boolean isDisplayed() {
        LOGGER.info("Checking home page is displayed");
        BrowserDriver.waitForElementVisible(competitionPageContainer.competitions_title);
        return competitionPageContainer.competitions_title.isDisplayed();
    }
}
