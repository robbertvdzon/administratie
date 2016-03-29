package com.vdzon.msw.cucumber.pages.pagesteps;

import com.vdzon.msw.cucumber.browsers.BrowserDriver;
import com.vdzon.msw.cucumber.pages.PageFactory;
import com.vdzon.msw.cucumber.pages.view.NavigationView;

public class NavigationPage {

    private String getBaseUrl(){
        String url = System.getProperty("testurl");
        System.out.println("URL TO TEST="+url);
//        return url == null ? "http://149.210.235.58" : url;
        return url == null ? "http://localhost:4567" : url;
    }

    public HomePage when_I_logout() {
        BrowserDriver.loadPage(getBaseUrl()+"/logout");
        return PageFactory.getHomePage();
    }

    public HomePage given_I_navigate_to_the_home_page() {
        BrowserDriver.loadPage(getBaseUrl()+"/");
        return PageFactory.getHomePage();
    }

    public LoginPage when_I_press_login() {
        NavigationView.pressLoginButton();
        return PageFactory.getLoginPage();
    }

    public CompetitionPage when_I_press_main_button_competition() {
        NavigationView.pressMainButtonCompetition();
        return PageFactory.getCompetitionPage();
    }

}
