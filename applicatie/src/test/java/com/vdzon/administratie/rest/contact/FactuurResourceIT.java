//package com.vdzon.administratie.contact;
//
//import com.jayway.restassured.filter.session.SessionFilter;
//import com.jayway.restassured.response.Response;
//import com.vdzon.administratie.model.FactuurRegel;
//import com.vdzon.administratie.model.Klant;
//import com.vdzon.administratie.util.SingleAnswer;
//import com.vdzon.administratie.model.Gebruiker;
//import com.vdzon.administratie.model.Factuur;
//import com.vdzon.administratie.testutil.AbstractRestIT;
//import org.junit.Assert;
//import org.junit.Test;
//
//import java.time.LocalDate;
//import java.util.ArrayList;
//import java.util.List;
//
//import static com.jayway.restassured.RestAssured.given;
//import static org.hamcrest.Matchers.containsString;
//
//public class FactuurResourceIT extends AbstractRestIT {
//
//    SessionFilter sessionFilter = new SessionFilter();
//
//    @Test
//    public void test_put_gewicht() throws Exception {
//
//        // login
//        login();
//
//        // check then test waarden die gezet worden als er nog geen robbert user is
//        Gebruiker currentUser = getCurrentUser();
//        Assert.assertEquals(2, currentUser.getAdresboek().size());
//        Assert.assertEquals(2, currentUser.getFacturen().size());
//
//        Factuur newFactuur = createFactuur();
//
//        // add factuur
//        addFactuur(newFactuur);
//
//        currentUser = getCurrentUser();
//        Assert.assertEquals(3,currentUser.getFacturen().size());
//
//        // remove all facturen except 2016003
//        currentUser.getFacturen().stream().filter(factuur -> !factuur.getBestellingNummer().equals("2016003")).forEach(factuur -> removeBestelling(factuur.getBestellingNummer()));
//
//        currentUser = getCurrentUser();
//        Assert.assertEquals(1,currentUser.getFacturen().size());
//
//        // remove non existing factuur
//        removeBestelling("123123123123");
//
//        Assert.assertEquals(1,currentUser.getFacturen().size());
//
//        // check factuur for date
//        Factuur factuur = currentUser.getFacturen().get(0);
//        Assert.assertEquals(LocalDate.now(), factuur.getDeclaratieDate());
//
//        // change date
//        factuur.setDeclaratieDate(LocalDate.ofYearDay(2014,4));
//        updateFactuur(factuur);
//
//        // check factuur for omschrijving
//        factuur = currentUser.getFacturen().get(0);
//        Assert.assertEquals(LocalDate.ofYearDay(2014,4), factuur.getDeclaratieDate());
//
//        // remove last factuur
//        removeBestelling("2016003");
//        currentUser = getCurrentUser();
//
//        Assert.assertEquals(0,currentUser.getFacturen().size());
//    }
//
//    private Factuur createFactuur() {
//        FactuurRegel regel1 = new FactuurRegel("ontwikkeling",12,100,21,"test");
//        FactuurRegel regel2 = new FactuurRegel("ontwikkeling",13,80,21,"test2");
//        List<FactuurRegel> regels1 = new ArrayList<>();
//        regels1.add(regel1);
//        regels1.add(regel2);
//        return new Factuur("2016003", LocalDate.now(),new Klant("123","001","loxia","Utrecht","Weg1","1234AB","NL"), false,regels1, "1234");
//    }
//
//
//    private void login(){
//        given().
//                filter(sessionFilter).
//                parameters("username", "q", "password", "q").
//                when().
//                post(getBaseUrl()+"login").
//                then().statusCode(200).
//                body(containsString("ok"));
//
//    }
//
//    private Gebruiker getCurrentUser() {
//        Response getResponse = given().
//                filter(sessionFilter).
//                when().
//                get(getBaseUrl() + "users/getcurrentuser");
//
//        return getAndCheckResponse(getResponse, Gebruiker.class, 200);
//    }
//
//    private String addFactuur(Factuur factuur) {
//        Response response = given().
//                filter(sessionFilter).
//                parameters("factuur", factuur).
//                when().
//                put(getBaseUrl() + "rest/factuur/");
//
//        SingleAnswer uuidSingleAnswer = getAndCheckResponse(response, SingleAnswer.class, 200);
//        return uuidSingleAnswer.getAnswer();
//    }
//
//    private String removeBestelling(String factuurNummer) {
//        Response response = given().
//                filter(sessionFilter).
//                when().
//                delete(getBaseUrl() + "rest/factuur/"+factuurNummer);
//
//        SingleAnswer uuidSingleAnswer = getAndCheckResponse(response, SingleAnswer.class, 200);
//        return uuidSingleAnswer.getAnswer();
//    }
//
//    private String updateFactuur(Factuur factuur) {
//        Response response = given().
//                filter(sessionFilter).
//                parameters("factuur", factuur).
//                when().
//                post(getBaseUrl() + "rest/factuur/");
//
//        SingleAnswer uuidSingleAnswer = getAndCheckResponse(response, SingleAnswer.class, 200);
//        return uuidSingleAnswer.getAnswer();
//    }
//
//}