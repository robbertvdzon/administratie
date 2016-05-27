package com.vdzon.administratie.administratie;

import com.vdzon.administratie.util.JsonUtil;
import spark.Spark;

import javax.inject.Inject;

public class AdministratieResource {

    @Inject
    public AdministratieResource(AdministratieService administratieService) {
        Spark.post("/rest/administratie", administratieService::putAdministratie, JsonUtil.json());
    }
}
