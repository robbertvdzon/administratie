package com.vdzon.administratie.rubriceren.rest;

import com.vdzon.administratie.util.JsonUtil;
import spark.Spark;

import javax.inject.Inject;


public class RubriceerResource {

    @Inject
    public RubriceerResource(RubriceerService rubriceerService) {
        Spark.get("/rest/rebriceerregels/", rubriceerService::getRubriceerRegels, JsonUtil.json());
        Spark.post("/rest/rubriceer/", rubriceerService::rubriceerRegels, JsonUtil.json());
    }


}
