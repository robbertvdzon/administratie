package com.vdzon.administratie.util;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import spark.ResponseTransformer;

import static spark.Spark.get;

public class JsonUtil {
    public static <P>P fromJson(String json, Class clazz) {
        Gson gson = new GsonBuilder().create();
        return (P)gson.fromJson(json, clazz);
    }

    public static String toJson(Object object) {
        return new Gson().toJson(object);
    }

    public static ResponseTransformer json() {
        return JsonUtil::toJson;
    }
}
