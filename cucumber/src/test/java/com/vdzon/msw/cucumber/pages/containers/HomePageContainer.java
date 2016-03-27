package com.vdzon.msw.cucumber.pages.containers;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

public class HomePageContainer {

    @FindBy(how = How.ID, using = "homepage_title")
    public WebElement pageTitle;

}
