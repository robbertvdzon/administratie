package com.vdzon.administratie.rest.overzicht

import com.vdzon.administratie.util.JsonUtil
import spark.Route
import spark.Spark

import javax.inject.Inject


class OverzichtResource
@Inject
constructor(overzichtService: OverzichtService) {
    init {
        Spark.get("/rest/overzicht/pdf/:beginDate/:endDate", Route { req, res -> overzichtService.getPdf(req, res) }, JsonUtil.json())
    }


}
