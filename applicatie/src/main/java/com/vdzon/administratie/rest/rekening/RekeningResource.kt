package com.vdzon.administratie.rest.rekening

import com.vdzon.administratie.util.JsonUtil
import spark.Route
import spark.Spark

import javax.inject.Inject


class RekeningResource
@Inject
constructor(rekeningService: RekeningService) {
    init {
        Spark.put("/rest/rekening/", Route { req, res -> rekeningService.putRekening(req, res) }, JsonUtil.json())
        Spark.post("/rest/rekening/", Route { req, res -> rekeningService.putRekening(req, res) }, JsonUtil.json())
        Spark.delete("/rest/rekening/:uuid", Route { req, res -> rekeningService.removeRekening(req, res) }, JsonUtil.json())
    }


}
