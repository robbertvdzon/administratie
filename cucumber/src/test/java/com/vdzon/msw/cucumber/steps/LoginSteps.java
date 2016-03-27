package com.vdzon.msw.cucumber.steps;

import com.vdzon.msw.cucumber.pages.PageFactory;
import cucumber.annotation.en.Given;
import cucumber.annotation.en.Then;
import cucumber.annotation.en.When;

import java.util.logging.Logger;

public class LoginSteps {
    private static final Logger LOGGER = Logger.getLogger(LoginSteps.class.getName());

    @Given("^I navigate to the home page$")
    public void given_I_navigate_to_the_home_page() {
        LOGGER.info("Entering: I navigate to the home application");
        PageFactory.getNavigationPage().given_I_navigate_to_the_home_page();
    }

    @When("^I logout")
    public void when_I_logout() {
        LOGGER.info("Logout");
        PageFactory.getNavigationPage().when_I_logout();
    }

    @When("^I press login$")
    public void when_I_press_login() {
        LOGGER.info("Login");
        PageFactory.getNavigationPage().when_I_press_login();
    }

    @When("^I try to login with '(.+)' credentials$")
    public void when_I_try_to_login(String credentialsType) {
        LOGGER.info("Entering: I try to login with " + credentialsType + " credentials");
        PageFactory.getLoginPage().when_I_try_to_login(credentialsType);
    }

    @Then("^I should see that I logged in '(.+)'$")
    public void then_I_login(String outcome) {
        LOGGER.info("Entering: I should see that I logged in " + outcome);
        PageFactory.getLoginPage().then_I_login(outcome);
    }
}
