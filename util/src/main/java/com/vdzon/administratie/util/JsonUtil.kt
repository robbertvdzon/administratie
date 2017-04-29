package com.vdzon.administratie.util

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import spark.ResponseTransformer

object JsonUtil {
    fun <P> fromJson(json: String, clazz: Class<*>): P {
        val gson = GsonBuilder().create()
        return gson.fromJson<Any>(json, clazz) as P
    }

    fun toJson(`object`: Any): String {
        return Gson().toJson(`object`)
    }

    fun json(): ResponseTransformer {
        return ResponseTransformer { toJson(it) }
    }
}
