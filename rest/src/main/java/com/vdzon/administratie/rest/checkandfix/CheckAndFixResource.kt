package com.vdzon.administratie.rest.checkandfix

import com.vdzon.administratie.util.JsonUtil
import spark.Route
import spark.Spark

import javax.inject.Inject


class CheckAndFixResource
@Inject
constructor(rubricecheckAndFixServicerService: CheckAndFixService) {
    init {
        Spark.get("/rest/checkandfixregels/", Route { req, res -> rubricecheckAndFixServicerService.getCheckAndFixRegels(req, res) }, JsonUtil.json())
        Spark.post("/rest/fix/", Route { req, res -> rubricecheckAndFixServicerService.fix(req, res) }, JsonUtil.json())
    }
}
