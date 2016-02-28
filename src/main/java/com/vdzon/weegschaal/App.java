package com.vdzon.weegschaal;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.vdzon.weegschaal.auth.AuthResource;
import com.vdzon.weegschaal.gewicht.GewichtResource;
import spark.Spark;

public class App {
    public static void main(String[] args) {
        // init spark with web statics
        Spark.staticFileLocation("/web");
        // change port if needed
        if (args.length>0) {
            Spark.port(Integer.parseInt(args[0]));
        }
        if (args.length>1) {
            System.getProperties().setProperty("mongoDbPort", args[1]);
        }


        // default json response
        Spark.before((request, response) -> response.type("application/json"));

        // create guice injector
        Injector injector = Guice.createInjector(new AppInjector());

        // instanciate the objects that need injections
        GewichtResource testResource = injector.getInstance(GewichtResource.class);
        AuthResource authResource = injector.getInstance(AuthResource.class);
    }
}
