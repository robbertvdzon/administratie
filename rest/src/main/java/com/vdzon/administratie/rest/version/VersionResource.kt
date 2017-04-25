package com.vdzon.administratie.rest.version

import com.vdzon.administratie.util.JsonUtil
import spark.Request
import spark.Response
import spark.Route
import spark.Spark
import javax.inject.Inject


class VersionResource
@Inject
constructor(val versionData: VersionData) {
    init {
        Spark.get("/rest/version", Route { req, res -> this.getVersion(req, res) }, JsonUtil.json())
        Spark.get("/rest/buildtime", Route { req, res -> this.getBuildtime(req, res) }, JsonUtil.json())
    }

    @Throws(Exception::class)
    protected fun getVersion(req: Request, res: Response): Any {
        return versionData.version
    }

    @Throws(Exception::class)
    protected fun getBuildtime(req: Request, res: Response): Any {
        return versionData.buildTime
    }


}
