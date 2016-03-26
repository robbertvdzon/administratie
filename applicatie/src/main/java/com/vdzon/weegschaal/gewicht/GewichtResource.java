package com.vdzon.weegschaal.gewicht;

import com.vdzon.weegschaal.util.JsonUtil;
import spark.Spark;

import javax.inject.Inject;


public class GewichtResource {

    @Inject
    public GewichtResource(GewichtService gewichtService) {
        Spark.put("/rest/gewicht/", gewichtService::putGewicht, JsonUtil.json());
        Spark.get("/rest/gewicht/", gewichtService::getGewicht, JsonUtil.json());
        Spark.delete("/rest/gewicht/:uuid", gewichtService::deleteGewicht, JsonUtil.json());
        Spark.post("/rest/gewicht/", gewichtService::postGewicht, JsonUtil.json());
    }


}
