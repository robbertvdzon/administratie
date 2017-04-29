package com.vdzon.administratie.rest.gebruiker

import com.vdzon.administratie.util.JsonUtil
import spark.Route
import spark.Spark

import javax.inject.Inject


class GebruikerResource
@Inject
constructor(gebruikerService: GebruikerService) {
    init {
        Spark.post("/rest/gebruiker/", Route { req, res -> gebruikerService.postGebruiker(req, res) }, JsonUtil.json())
        Spark.put("/rest/gebruiker/", Route { req, res -> gebruikerService.postGebruiker(req, res) }, JsonUtil.json())
        Spark.delete("/rest/gebruiker/:uuid", Route { req, res -> gebruikerService.removeGebruiker(req, res) }, JsonUtil.json())
    }


}
