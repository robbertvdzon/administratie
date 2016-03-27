package com.vdzon.msw.cucumber.pages;

import com.vdzon.msw.cucumber.pages.pagesteps.CompetitionPage;
import com.vdzon.msw.cucumber.pages.pagesteps.HomePage;
import com.vdzon.msw.cucumber.pages.pagesteps.LoginPage;
import com.vdzon.msw.cucumber.pages.pagesteps.NavigationPage;

public class PageFactory {
    private static NavigationPage navigationPage = null;
    private static CompetitionPage competitionPage = null;
    private static HomePage homePage = null;
    private static LoginPage loginPage = null;

    public static synchronized NavigationPage getNavigationPage() {
        if (navigationPage == null) {
            navigationPage = new NavigationPage();
        }
        return navigationPage;
    }

    public static synchronized HomePage getHomePage() {
        if (homePage == null) {
            homePage = new HomePage();
        }
        return homePage;
    }

    public static synchronized LoginPage getLoginPage() {
        if (loginPage == null) {
            loginPage = new LoginPage();
        }
        return loginPage;
    }

    public static synchronized CompetitionPage getCompetitionPage() {
        if (competitionPage == null) {
            competitionPage = new CompetitionPage();
        }
        return competitionPage;
    }


}
