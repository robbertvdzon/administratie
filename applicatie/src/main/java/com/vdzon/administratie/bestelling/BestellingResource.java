package com.vdzon.administratie.bestelling;

import com.vdzon.administratie.util.JsonUtil;
import spark.Spark;

import javax.inject.Inject;


public class BestellingResource {

    @Inject
    public BestellingResource(BestellingService bestellingService) {
        Spark.put("/rest/bestelling/", bestellingService::putBestelling, JsonUtil.json());
        Spark.post("/rest/bestelling/", bestellingService::putBestelling, JsonUtil.json());
        Spark.delete("/rest/bestelling/:uuid", bestellingService::removeBestelling, JsonUtil.json());
    }


}
