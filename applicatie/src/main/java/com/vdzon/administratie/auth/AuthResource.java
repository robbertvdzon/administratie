package com.vdzon.administratie.auth;

import com.vdzon.administratie.util.JsonUtil;
import spark.Spark;

import javax.inject.Inject;

public class AuthResource {

    @Inject
    public AuthResource(AuthService authService) {
        Spark.post("/login", authService::login, JsonUtil.json());
        Spark.get("/logout", authService::logout, JsonUtil.json());
        Spark.get("/load", authService::getCurrentAdministratie, JsonUtil.json());
    }
}
