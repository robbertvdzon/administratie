package com.vdzon.administratie.rest

import org.pac4j.core.context.HttpConstants
import org.pac4j.sparkjava.DefaultHttpActionAdapter
import org.pac4j.sparkjava.SparkWebContext

import spark.Spark.halt

class DemoHttpActionAdapter : DefaultHttpActionAdapter() {

    override fun adapt(code: Int, context: SparkWebContext): Any? {
        if (code == HttpConstants.UNAUTHORIZED) {
            halt(401)
        } else if (code == HttpConstants.FORBIDDEN) {
            halt(403)
        } else {
            return super.adapt(code, context)
        }
        return null
    }
}
