package com.vdzon.administratie.rest.afschrift

import com.vdzon.administratie.util.JsonUtil
import spark.Route
import spark.Spark

import javax.inject.Inject


class AfschriftResource
@Inject
constructor(afschriftService: AfschriftService) {
    init {
        Spark.put("/rest/afschrift/", Route { req, res -> afschriftService.putAfschrift(req, res) }, JsonUtil.json())
        Spark.post("/rest/afschrift/", Route { req, res -> afschriftService.putAfschrift(req, res) }, JsonUtil.json())
        Spark.delete("/rest/afschrift/:nummer", Route { req, res -> afschriftService.removeAfschrift(req, res) }, JsonUtil.json())
        Spark.post("/rest/afschrift/uploadabn", "multipart/form-data", Route { request, response -> afschriftService.uploadabn(request, response) }, JsonUtil.json())
    }


}
