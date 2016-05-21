package com.vdzon.administratie.auth;

import com.vdzon.administratie.util.JsonUtil;
import spark.Spark;

import javax.inject.Inject;

public class AuthResource {

    @Inject
    public AuthResource(AuthService authService) {
        Spark.post("/log-in", authService::login, JsonUtil.json());
        Spark.get("/logout", authService::logout, JsonUtil.json());
        Spark.post("/register", authService::register, JsonUtil.json());
    }
}
