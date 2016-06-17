package com.vdzon.administratie.rest.afschrift;

import com.vdzon.administratie.util.JsonUtil;
import spark.Spark;

import javax.inject.Inject;


public class AfschriftResource {

    @Inject
    public AfschriftResource(AfschriftService afschriftService) {
        Spark.put("/rest/afschrift/", afschriftService::putAfschrift, JsonUtil.json());
        Spark.post("/rest/afschrift/", afschriftService::putAfschrift, JsonUtil.json());
        Spark.delete("/rest/afschrift/:nummer", afschriftService::removeAfschrift, JsonUtil.json());
        Spark.post("/rest/afschrift/uploadabn", "multipart/form-data", afschriftService::uploadabn, JsonUtil.json());
    }


}
