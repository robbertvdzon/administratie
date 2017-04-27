package com.vdzon.administratie.rest.rubriceren

import com.vdzon.administratie.util.JsonUtil
import spark.Route
import spark.Spark

import javax.inject.Inject


class RubriceerResource
@Inject
constructor(rubriceerService: RubriceerService) {
    init {
        Spark.get("/rest/rebriceerregels/", Route { req, res -> rubriceerService.getRubriceerRegels(req, res) }, JsonUtil.json())
        Spark.post("/rest/rubriceer/", Route { req, res -> rubriceerService.rubriceerRegels(req, res) }, JsonUtil.json())
    }


}
