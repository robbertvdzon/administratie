package com.vdzon.weegschaal.version;

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
        InputStream inputStream = this.getClass().getResourceAsStream("/META-INF/MANIFEST.MF");
        Properties pr = new Properties();
        pr.load(inputStream);
        String version = pr.getProperty("Implementation-Build-Number");
        return version;
    }


}
