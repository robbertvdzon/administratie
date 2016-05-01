package com.vdzon.administratie;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.vdzon.administratie.auth.AuthResource;
import com.vdzon.administratie.factuur.FactuurResource;
import com.vdzon.administratie.gebruiker.GebruikerResource;
import com.vdzon.administratie.version.VersionResource;
import spark.Spark;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
        AuthResource authResource = injector.getInstance(AuthResource.class);
        VersionResource versionResource = injector.getInstance(VersionResource.class);
        FactuurResource factuurResource = injector.getInstance(FactuurResource.class);
        GebruikerResource gebruikerResource = injector.getInstance(GebruikerResource.class);


    }

    private static void loadVersion() {

        System.out.println("Load version from manifest");

        try {
            Enumeration<URL> resources = App.class.getClassLoader().getResources("META-INF/MANIFEST.MF");
            // walk through all manifest files (for each included jar there is a manifest,
            // we need find ours by checking the mainClass
            while (resources.hasMoreElements()) {
                URL url = resources.nextElement();
                Properties properties = new Properties();
                properties.load(url.openStream());
                String mainClass = properties.getProperty("Main-Class");
                if (mainClass != null && mainClass.equals(App.class.getCanonicalName())) {
                    //Correct manifest found
                    setVersion(properties.getProperty("Implementation-Build-Number"));
                    setBuildTime(reformatBuildTime(properties.getProperty("Build-Time")));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Version is " + getVersion());
        System.out.println("Buildtime is " + getBuildTime());
    }

    public static String getVersion() {
        return version;
    }

    private static String reformatBuildTime(String buildTime){
        try {
            Date date = (new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")).parse(buildTime.replaceAll("Z$", "+0000"));
            return (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "Unknown";

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