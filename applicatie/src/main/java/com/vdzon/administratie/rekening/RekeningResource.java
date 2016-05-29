package com.vdzon.administratie.rekening;

import com.vdzon.administratie.util.JsonUtil;
import spark.Spark;

import javax.inject.Inject;


public class RekeningResource {

    @Inject
    public RekeningResource(RekeningService rekeningService) {
        Spark.put("/rest/rekening/", rekeningService::putRekening, JsonUtil.json());
        Spark.post("/rest/rekening/", rekeningService::putRekening, JsonUtil.json());
        Spark.delete("/rest/rekening/:uuid", rekeningService::removeRekening, JsonUtil.json());
    }


}
