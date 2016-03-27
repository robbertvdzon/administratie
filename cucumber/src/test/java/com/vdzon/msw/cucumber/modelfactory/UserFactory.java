package com.vdzon.msw.cucumber.modelfactory;

import com.vdzon.msw.cucumber.model.User;

public class UserFactory {
    public static User createValidUser() {
        User user = new User();
        user.withUserName("q").withPassword("q");
        return user;
    }

    public static User createInvalidUser() {
        User user = new User();
        user.withUserName("not valid").withPassword("not valid");
        return user;
    }
}
