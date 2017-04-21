package com.vdzon.administratie.rest.declaratie

import com.vdzon.administratie.util.JsonUtil
import spark.Route
import spark.Spark

import javax.inject.Inject


class DeclaratieResource
@Inject
constructor(declaratieService: DeclaratieService) {
    init {
        Spark.put("/rest/declaratie/", Route { req, res -> declaratieService.putDeclaratie(req, res) }, JsonUtil.json())
        Spark.post("/rest/declaratie/", Route { req, res -> declaratieService.putDeclaratie(req, res) }, JsonUtil.json())
        Spark.delete("/rest/declaratie/:uuid", Route { req, res -> declaratieService.removeDeclaratie(req, res) }, JsonUtil.json())
    }


}
