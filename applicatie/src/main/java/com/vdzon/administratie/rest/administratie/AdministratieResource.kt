package com.vdzon.administratie.rest.administratie

import com.vdzon.administratie.util.JsonUtil
import spark.Route
import spark.Spark

import javax.inject.Inject

class AdministratieResource
@Inject
constructor(administratieService: AdministratieService) {
    init {
        Spark.post("/rest/administratie", Route { req, res -> administratieService.putAdministratie(req, res) }, JsonUtil.json())
    }
}
