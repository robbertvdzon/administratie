package com.vdzon.administratie.rest.overzicht;

import com.vdzon.administratie.util.JsonUtil;
import spark.Spark;

import javax.inject.Inject;


public class OverzichtResource {

    @Inject
    public OverzichtResource(OverzichtService overzichtService) {
        Spark.get("/rest/overzicht/pdf/:beginDate/:endDate", overzichtService::getPdf, JsonUtil.json());
    }


}
