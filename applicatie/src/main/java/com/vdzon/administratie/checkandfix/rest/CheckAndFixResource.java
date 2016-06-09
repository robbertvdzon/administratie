package com.vdzon.administratie.checkandfix.rest;

import com.vdzon.administratie.util.JsonUtil;
import spark.Spark;

import javax.inject.Inject;


public class CheckAndFixResource {

    @Inject
    public CheckAndFixResource(CheckAndFixService rubricecheckAndFixServicerService) {
        Spark.get("/rest/checkandfixregels/", rubricecheckAndFixServicerService::getCheckAndFixRegels, JsonUtil.json());
        Spark.post("/rest/fix/", rubricecheckAndFixServicerService::fix, JsonUtil.json());
    }


}
