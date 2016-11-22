package com.vdzon.administratie.rest.version;

import com.vdzon.administratie.App;
import com.vdzon.administratie.util.JsonUtil;
import spark.Request;
import spark.Response;
import spark.Spark;

import javax.inject.Inject;


public class VersionResource {

    @Inject
    public VersionResource() {
        Spark.get("/rest/version", this::getVersion, JsonUtil.json());
        Spark.get("/rest/buildtime", this::getBuildtime, JsonUtil.json());
    }

    protected Object getVersion(Request req, Response res) throws Exception {
        return App.getVersion();
    }

    protected Object getBuildtime(Request req, Response res) throws Exception {
        return App.getBuildTime();
    }


}
