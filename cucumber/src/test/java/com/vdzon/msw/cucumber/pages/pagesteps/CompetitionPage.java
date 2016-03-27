package com.vdzon.msw.cucumber.pages.pagesteps;

import com.vdzon.msw.cucumber.pages.view.CompetitionView;
import junit.framework.Assert;

public class CompetitionPage {


    public void then_competition_is_displayed() {
        Assert.assertTrue(CompetitionView.isDisplayed());
    }

}
