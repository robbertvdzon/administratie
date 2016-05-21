package com.vdzon.administratie.data;

import com.vdzon.administratie.util.JsonUtil;
import spark.Spark;

import javax.inject.Inject;

public class DataResource {

    @Inject
    public DataResource(DataService dataService) {
        Spark.get("/load", dataService::loadData, JsonUtil.json());
    }
}
