package com.vdzon.weegschaal.version;

import com.vdzon.weegschaal.App;
import com.vdzon.weegschaal.util.JsonUtil;
import spark.Request;
import spark.Response;
import spark.Spark;

import javax.inject.Inject;
import java.io.InputStream;
import java.util.Properties;
import java.util.jar.Attributes;
import java.util.jar.Manifest;


public class VersionResource {

    @Inject
    public VersionResource() {
        Spark.get("/rest/version", this::getVersion, JsonUtil.json());
    }

    protected Object getVersion(Request req, Response res) throws Exception {
        return "&nbsp;&nbsp;&nbsp;Build on :"+App.getBuildTime()+"&nbsp;&nbsp;&nbsp;Versie : "+App.getVersion();
    }


}
