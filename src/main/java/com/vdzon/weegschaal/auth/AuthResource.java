package com.vdzon.weegschaal.auth;

import com.vdzon.weegschaal.auth.AuthService;
import com.vdzon.weegschaal.util.JsonUtil;
import spark.Spark;

import javax.inject.Inject;

public class AuthResource {

    @Inject
    public AuthResource(AuthService authService) {
        Spark.post("/login", authService::login, JsonUtil.json());
        Spark.get("/logout", authService::logout, JsonUtil.json());
        Spark.get("/users/getcurrentuser", authService::getcurrentuser, JsonUtil.json());
    }
}
