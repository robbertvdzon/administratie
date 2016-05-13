package com.vdzon.administratie.factuur;

import com.vdzon.administratie.util.JsonUtil;
import spark.Spark;

import javax.inject.Inject;


public class FactuurResource {

    @Inject
    public FactuurResource(FactuurService factuurService) {
        Spark.put("/rest/factuur/", factuurService::putFactuur, JsonUtil.json());
        Spark.post("/rest/factuur/", factuurService::putFactuur, JsonUtil.json());
        Spark.delete("/rest/factuur/:uuid", factuurService::removeFactuur, JsonUtil.json());
    }


}
