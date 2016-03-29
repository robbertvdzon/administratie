package com.vdzon.administratie.auth;

import com.jayway.restassured.RestAssured;
import com.vdzon.administratie.testutil.AbstractRestIT;
import org.junit.Test;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

public class AuthResourceIT extends AbstractRestIT {

    @Test
    public void test_valid_user() throws Exception {
        given().
                parameters("username", "q", "password", "q").
                when().
                post(getBaseUrl()+"login").
                then().statusCode(200).
                body(containsString("ok"));
    }

    @Test
    public void test_invalid_user() throws Exception {
        RestAssured.given().
                parameters("username", "John", "password", "Doe").
                when().
                post(getBaseUrl() + "login").
                then().statusCode(401).
                body(containsString("not authorized"));
    }


}