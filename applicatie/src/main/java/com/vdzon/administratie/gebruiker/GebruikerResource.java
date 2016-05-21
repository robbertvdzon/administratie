package com.vdzon.administratie.gebruiker;

import com.vdzon.administratie.util.JsonUtil;
import spark.Spark;

import javax.inject.Inject;


public class GebruikerResource {

    @Inject
    public GebruikerResource(GebruikerService gebruikerService) {
        Spark.post("/rest/gebruiker/", gebruikerService::postGebruiker, JsonUtil.json());
        Spark.put("/rest/gebruiker/", gebruikerService::postGebruiker, JsonUtil.json());
        Spark.delete("/rest/gebruiker/:uuid", gebruikerService::removeGebruiker, JsonUtil.json());
        Spark.post("/rest/gebruiker/updatePassword/:uuid/:newPassword", gebruikerService::updatePassword, JsonUtil.json());
    }


}
