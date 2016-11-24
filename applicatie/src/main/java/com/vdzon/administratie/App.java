package com.vdzon.administratie;

import com.github.mongobee.exception.MongobeeException;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.vdzon.administratie.mongo.Mongo;
import com.vdzon.administratie.rest.administratie.AdministratieResource;
import com.vdzon.administratie.rest.afschrift.AfschriftResource;
import com.vdzon.administratie.rest.auth.AuthResource;
import com.vdzon.administratie.rest.bestelling.BestellingResource;
import com.vdzon.administratie.rest.checkandfix.CheckAndFixResource;
import com.vdzon.administratie.exceptions.ForbiddenException;
import com.vdzon.administratie.rest.contact.ContactResource;
import com.vdzon.administratie.data.DataResource;
import com.vdzon.administratie.rest.declaratie.DeclaratieResource;
import com.vdzon.administratie.rest.factuur.FactuurResource;
import com.vdzon.administratie.rest.gebruiker.GebruikerResource;
import com.vdzon.administratie.rest.overzicht.OverzichtResource;
import com.vdzon.administratie.rest.rekening.RekeningResource;
import com.vdzon.administratie.rest.rubriceren.RubriceerResource;
import com.vdzon.administratie.rest.version.VersionResource;
import spark.Spark;

import java.io.File;
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

    public static void main(String[] args) throws MongobeeException {

        // init Mongo
        Mongo.start();


        loadVersion();

        // init spark with web statics
        if (new File("C:\\workspace\\administratie\\applicatie\\src\\main\\resources\\web").exists()){
            Spark.externalStaticFileLocation("C:\\workspace\\administratie\\applicatie\\src\\main\\resources\\web");
        }
        else{
            Spark.staticFileLocation("/web");
        }

        // Handle exceptions
        Spark.exception(ForbiddenException.class, (exception, request, response) -> {
            response.status(403);
            response.body(exception.getMessage());
        });
        Spark.exception(Exception.class, (exception, request, response) -> {
            exception.printStackTrace();
            response.status(500);
            response.body(exception.getMessage());
        });


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
        DataResource dataResource = injector.getInstance(DataResource.class);
        AuthResource authResource = injector.getInstance(AuthResource.class);
        VersionResource versionResource = injector.getInstance(VersionResource.class);
        FactuurResource factuurResource = injector.getInstance(FactuurResource.class);
        ContactResource contactResource = injector.getInstance(ContactResource.class);
        GebruikerResource gebruikerResource = injector.getInstance(GebruikerResource.class);
        RekeningResource rekeningResource = injector.getInstance(RekeningResource.class);
        DeclaratieResource declaratieResource = injector.getInstance(DeclaratieResource.class);
        AfschriftResource afschriftResource = injector.getInstance(AfschriftResource.class);
        AdministratieResource administratieResource = injector.getInstance(AdministratieResource.class);
        BestellingResource bestellingResource = injector.getInstance(BestellingResource.class);
        RubriceerResource rubriceerResource  = injector.getInstance(RubriceerResource.class);
        OverzichtResource overzichtResource = injector.getInstance(OverzichtResource.class);
        CheckAndFixResource checkAndFixResource = injector.getInstance(CheckAndFixResource.class);
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
                System.out.println("mainClass:"+mainClass);
                System.out.println("App.class.getCanonicalName():"+App.class.getCanonicalName());
                if (mainClass != null && mainClass.equals(App.class.getCanonicalName())) {
                    //Correct manifest found
                    setVersion(properties.getProperty("Implementation-Version"));
                    setBuildTime(reformatBuildTime(properties.getProperty("Build-Time")));
                    System.out.println("props:"+properties);
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
