package com.vdzon.administratie.declaratie;

import com.vdzon.administratie.util.JsonUtil;
import spark.Spark;

import javax.inject.Inject;


public class DeclaratieResource {

    @Inject
    public DeclaratieResource(DeclaratieService declaratieService) {
        Spark.put("/rest/declaratie/", declaratieService::putDeclaratie, JsonUtil.json());
        Spark.post("/rest/declaratie/", declaratieService::putDeclaratie, JsonUtil.json());
        Spark.delete("/rest/declaratie/:uuid", declaratieService::removeDeclaratie, JsonUtil.json());
    }


}
