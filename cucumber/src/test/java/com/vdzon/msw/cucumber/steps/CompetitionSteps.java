package com.vdzon.msw.cucumber.steps;

import com.vdzon.msw.cucumber.pages.PageFactory;
import cucumber.annotation.en.Then;
import cucumber.annotation.en.When;

import java.util.logging.Logger;

public class CompetitionSteps {
    private static final Logger LOGGER = Logger.getLogger(CompetitionSteps.class.getName());

    @When("^I press competition main button$")
    public void when_I_open_competiton_page() {
        LOGGER.info("Open competition page");
        PageFactory.getNavigationPage().when_I_press_main_button_competition();
    }

    @Then("^I should see the compotitions page$")
    public void then_I_see_the_competition_page() {
        LOGGER.info("Entering: I should see the competition page");
        PageFactory.getCompetitionPage().then_competition_is_displayed();
    }
}
