package com.vdzon.weegschaal;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.vdzon.weegschaal.auth.AuthResource;
import com.vdzon.weegschaal.gewicht.GewichtResource;
import com.vdzon.weegschaal.version.VersionResource;
import spark.Spark;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class App {
    public static void main(String[] args) {
        // init spark with web statics
        Spark.staticFileLocation("/web");
        // change port if needed
        if (args.length > 0) {
            Spark.port(Integer.parseInt(args[0]));
        }
        if (args.length > 1) {
            System.getProperties().setProperty("mongoDbPort", args[1]);
        }


        // default json response
        Spark.before((request, response) -> response.type("application/json"));

        // create guice injector
        Injector injector = Guice.createInjector(new AppInjector());

        // instanciate the objects that need injections
        GewichtResource testResource = injector.getInstance(GewichtResource.class);
        AuthResource authResource = injector.getInstance(AuthResource.class);
        VersionResource versionResource = injector.getInstance(VersionResource.class);

        System.out.println("Load version from manifest");
        try {
            InputStream inputStream = App.class.getResourceAsStream("/META-INF/MANIFEST.MF");
            Properties pr = new Properties();
            pr.load(inputStream);
            String version = pr.getProperty("Implementation-Build-Number");
            setVersion(version);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Version is "+getVersion());

    }

    private static String version = "Undetermined";

    public static String getVersion() {
        return version;
    }

    public static void setVersion(String version) {
        App.version = version;
    }
}
