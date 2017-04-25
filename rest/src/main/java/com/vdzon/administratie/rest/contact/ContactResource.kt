package com.vdzon.administratie.rest.contact

import com.vdzon.administratie.util.JsonUtil
import spark.Route
import spark.Spark

import javax.inject.Inject


class ContactResource
@Inject
constructor(factuurService: ContactService) {
    init {
        Spark.put("/rest/contact/", Route { req, res -> factuurService.putContact(req, res) }, JsonUtil.json())
        Spark.post("/rest/contact/", Route { req, res -> factuurService.putContact(req, res) }, JsonUtil.json())
        Spark.delete("/rest/contact/:uuid", Route { req, res -> factuurService.removeContact(req, res) }, JsonUtil.json())
    }


}
