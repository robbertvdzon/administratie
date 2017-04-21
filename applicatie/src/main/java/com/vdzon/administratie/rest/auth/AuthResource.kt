package com.vdzon.administratie.rest.auth

import com.vdzon.administratie.util.JsonUtil
import spark.Route
import spark.Spark

import javax.inject.Inject

class AuthResource
@Inject
constructor(authService: AuthService) {
    init {
        Spark.post("/log-in", Route { req, res -> authService.login(req, res) }, JsonUtil.json())
        Spark.get("/logout", Route { req, res -> authService.logout(req, res) }, JsonUtil.json())
        Spark.post("/register", Route { req, res -> authService.register(req, res) }, JsonUtil.json())
    }
}
