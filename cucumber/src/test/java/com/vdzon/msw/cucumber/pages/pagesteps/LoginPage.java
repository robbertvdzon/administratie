package com.vdzon.msw.cucumber.pages.pagesteps;

import com.vdzon.msw.cucumber.constants.CredentialsType;
import com.vdzon.msw.cucumber.constants.Outcome;
import com.vdzon.msw.cucumber.model.User;
import com.vdzon.msw.cucumber.modelfactory.UserFactory;
import com.vdzon.msw.cucumber.pages.view.LoginView;
import com.vdzon.msw.cucumber.pages.view.NavigationView;
import junit.framework.Assert;

public class LoginPage {

    public void when_I_try_to_login(String credentialsType) {
        User user = null;
        CredentialsType ct = CredentialsType.credentialsTypeForName(credentialsType);
        switch (ct) {
            case VALID:
                user = UserFactory.createValidUser();
                break;
            case INVALID:
                user = UserFactory.createInvalidUser();
                break;
        }
        LoginView.login(user.getUsername(), user.getPassword());
    }

    public void then_I_login(String outcomeString) {
        Outcome outcome = Outcome.outcomeForName(outcomeString);
        switch (outcome) {
            case SUCCESS:
                NavigationView.waitForLogoffButton();
                Assert.assertTrue(NavigationView.isLogoffButtonVisible());
                Assert.assertFalse(NavigationView.isLoginButtonVisible());
                Assert.assertFalse(LoginView.isDisplayed());
                break;
            case FAILURE:
                Assert.assertTrue(NavigationView.isLoginButtonVisible());
                Assert.assertFalse(NavigationView.isLogoffButtonVisible());
                Assert.assertTrue(LoginView.isDisplayed());
                LoginView.checkLoginErrors();
                break;
        }
    }
}
