package com.vdzon.weegschaal.gewicht;

import com.jayway.restassured.response.Response;
import com.vdzon.weegschaal.util.SingleAnswer;
import com.vdzon.weegschaal.model.Customer;
import com.vdzon.weegschaal.model.Gewicht;
import com.vdzon.weegschaal.testutil.AbstractRestIT;
import org.junit.Assert;
import org.junit.Test;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

public class GewichtResourceIT extends AbstractRestIT {

    @Test
    public void test_put_gewicht() throws Exception {

        // login
        login();

        // check then gewichten is 0
        Customer currentUser = getCurrentUser();
        Assert.assertEquals(currentUser.getGewichten().size(),0);

        // add gewicht
        String uuid = addGewicht("9911");
        Assert.assertNotNull(uuid);

        // get gewicht that is just added
        Gewicht gewicht = getGewicht(uuid);
        Assert.assertEquals("9911", gewicht.getGewicht());
        currentUser = getCurrentUser();
        Assert.assertEquals(currentUser.getGewichten().size(),1);

        // update gewicht
        updateGewicht("9912",uuid);
        gewicht = getGewicht(uuid);
        Assert.assertEquals("9912", gewicht.getGewicht());

        // remove gewicht
        removeGewicht(uuid);
        currentUser = getCurrentUser();
        Assert.assertEquals(currentUser.getGewichten().size(),0);
        gewicht = getGewicht(uuid);
        Assert.assertNull(gewicht);

    }

    private void login(){
        given().
                parameters("username", "q", "password", "q").
                when().
                post(getBaseUrl()+"login").
                then().statusCode(200).
                body(containsString("ok"));

    }

    private Customer getCurrentUser() {
        Response getResponse = given().
                when().
                get(getBaseUrl() + "users/getcurrentuser");

        return getAndCheckResponse(getResponse, Customer.class, 200);
    }

    private Gewicht getGewicht(String uuid) {
        Response getResponse = given().
                parameters("uuid", uuid).
                when().
                get(getBaseUrl() + "rest/gewicht/");

        return getAndCheckResponse(getResponse, Gewicht.class, 200);
    }

    private String addGewicht(String gewicht) {
        Response response = given().
                parameters("gewicht", gewicht).
                when().
                put(getBaseUrl() + "rest/gewicht/");

        SingleAnswer uuidSingleAnswer = getAndCheckResponse(response, SingleAnswer.class, 200);
        return uuidSingleAnswer.getAnswer();
    }

    private void removeGewicht(String uuid) {
        Response response = given().
                when().
                delete(getBaseUrl() + "rest/gewicht/"+uuid);

        SingleAnswer uuidSingleAnswer = getAndCheckResponse(response, SingleAnswer.class, 200);
    }

    private void updateGewicht(String gewicht, String uuid) {
        Response response = given().
                parameters("uuid", uuid).
                parameters("gewicht", gewicht).
                when().
                post(getBaseUrl() + "rest/gewicht/");

        SingleAnswer uuidSingleAnswer = getAndCheckResponse(response, SingleAnswer.class, 200);
    }

}