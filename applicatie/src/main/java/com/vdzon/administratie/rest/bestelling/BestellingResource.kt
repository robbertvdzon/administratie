package com.vdzon.administratie.rest.bestelling

import com.vdzon.administratie.util.JsonUtil
import spark.Route
import spark.Spark

import javax.inject.Inject


class BestellingResource
@Inject
constructor(bestellingService: BestellingService) {
    init {
        Spark.put("/rest/bestelling/", Route { req, res -> bestellingService.putBestelling(req, res) }, JsonUtil.json())
        Spark.post("/rest/bestelling/", Route { req, res -> bestellingService.putBestelling(req, res) }, JsonUtil.json())
        Spark.delete("/rest/bestelling/:uuid", Route { req, res -> bestellingService.removeBestelling(req, res) }, JsonUtil.json())
    }


}
