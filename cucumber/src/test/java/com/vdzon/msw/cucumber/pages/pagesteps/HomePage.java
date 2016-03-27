package com.vdzon.msw.cucumber.pages.pagesteps;

import com.vdzon.msw.cucumber.pages.view.HomeView;
import junit.framework.Assert;

public class HomePage {


    public void then_homepage_is_displayed() {
        Assert.assertFalse(HomeView.isDisplayed());
    }

}
