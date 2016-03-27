package com.vdzon.msw.cucumber.pages.containers;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

public class LoginPageContainer {

    @FindBy(how = How.ID, using = "login_dialog")
    public WebElement loginDialog;

    @FindBy(how = How.ID, using = "username")
    public WebElement usernameInput;

    @FindBy(how = How.ID, using = "password")
    public WebElement passwordInput;

    @FindBy(how = How.ID, using = "submit_button")
    public WebElement submitButton;

    @FindBy(how = How.ID, using = "auth_failed")
    public WebElement authFailedDiv;
}
