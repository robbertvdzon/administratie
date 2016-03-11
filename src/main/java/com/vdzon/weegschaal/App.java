package com.vdzon.weegschaal;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.vdzon.weegschaal.auth.AuthResource;
import com.vdzon.weegschaal.gewicht.GewichtResource;
import com.vdzon.weegschaal.version.VersionResource;
import spark.Spark;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Properties;

public class App {
    private static String version = "Undetermined";
    private static String buildTime = "Undetermined";

    public static void main(String[] args) {
        loadVersion();

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


    }

    private static void loadVersion() {

        System.out.println("Load version from manifest: list from jar, via props");


        Enumeration<URL> resources = null;
        try {
            resources = App.class.getClassLoader()
                    .getResources("META-INF/MANIFEST.MF");
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (resources.hasMoreElements()) {
            try {
                URL url = resources.nextElement();
                System.out.println("------- " + url.toString());
                Properties pr = new Properties();
                pr.load(url.openStream());
                for (Object key : pr.keySet()) {
                    System.out.println(key + "=" + pr.getProperty(key.toString()));
                }
                String version = pr.getProperty("Implementation-Build-Number");
                String buildTime = pr.getProperty("Build-Time");
                String mainClass = pr.getProperty("Main-Class");
                if (mainClass.equals(App.class.getCanonicalName())) {
                    System.out.println("Correct manifest found");
                    setVersion(version);
                    setBuildTime(buildTime);

                }


            } catch (IOException E) {
                // handle
            }
        }
        System.out.println("-----------");
        System.out.println("Version is " + getVersion());
        System.out.println("Buildtime is " + getBuildTime());
    }

    public static String getVersion() {
        return version;
    }

    public static void setVersion(String version) {
        App.version = version;
    }

    public static String getBuildTime() {
        return buildTime;
    }

    public static void setBuildTime(String buildTime) {
        App.buildTime = buildTime;
    }
}
