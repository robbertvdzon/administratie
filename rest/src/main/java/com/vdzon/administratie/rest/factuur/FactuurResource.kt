package com.vdzon.administratie.rest.factuur

import com.vdzon.administratie.util.JsonUtil
import spark.Route
import spark.Spark

import javax.inject.Inject


class FactuurResource
@Inject
constructor(factuurService: FactuurService) {
    init {
        Spark.put("/rest/factuur/", Route { req, res -> factuurService.putFactuur(req, res) }, JsonUtil.json())
        Spark.post("/rest/factuur/", Route { req, res -> factuurService.putFactuur(req, res) }, JsonUtil.json())
        Spark.delete("/rest/factuur/:uuid", Route { req, res -> factuurService.removeFactuur(req, res) }, JsonUtil.json())
        Spark.get("/rest/factuur/pdf/:uuid", Route { req, res -> factuurService.getPdf(req, res) }, JsonUtil.json())
    }


}
