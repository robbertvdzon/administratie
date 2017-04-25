package com.vdzon.administratie.rest.data

import com.vdzon.administratie.util.JsonUtil
import spark.Route
import spark.Spark

import javax.inject.Inject

class DataResource
@Inject
constructor(dataService: DataService) {
    init {
        Spark.get("/load", Route { req, res -> dataService.loadData(req, res) }, JsonUtil.json())
    }
}
