package com.vdzon.administratie

import com.github.mongobee.exception.MongobeeException
import com.google.inject.Guice
import com.google.inject.Injector
import com.vdzon.administratie.mongo.Mongo
import com.vdzon.administratie.rest.administratie.AdministratieResource
import com.vdzon.administratie.rest.afschrift.AfschriftResource
import com.vdzon.administratie.rest.auth.AuthResource
import com.vdzon.administratie.rest.bestelling.BestellingResource
import com.vdzon.administratie.rest.checkandfix.CheckAndFixResource
import com.vdzon.administratie.exceptions.ForbiddenException
import com.vdzon.administratie.rest.contact.ContactResource
import com.vdzon.administratie.data.DataResource
import com.vdzon.administratie.rest.declaratie.DeclaratieResource
import com.vdzon.administratie.rest.factuur.FactuurResource
import com.vdzon.administratie.rest.gebruiker.GebruikerResource
import com.vdzon.administratie.rest.overzicht.OverzichtResource
import com.vdzon.administratie.rest.rekening.RekeningResource
import com.vdzon.administratie.rest.rubriceren.RubriceerResource
import com.vdzon.administratie.rest.version.VersionResource
import spark.Filter
import spark.Request
import spark.Response
import spark.Spark

import java.io.File
import java.io.IOException
import java.net.URL
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Enumeration
import java.util.Properties

object App {
    var version = "Undetermined"
    var buildTime = "Undetermined"

    @Throws(MongobeeException::class)
    @JvmStatic fun main(args: Array<String>) {

        // init Mongo
        Mongo.start()


        loadVersion()

        // init spark with web statics
        if (File("C:\\git\\administratie\\applicatie\\src\\main\\resources\\web").exists()) {
            Spark.externalStaticFileLocation("C:\\git\\administratie\\applicatie\\src\\main\\resources\\web")
        } else {
            Spark.staticFileLocation("/web")
        }

        // Handle exceptions
        Spark.exception(ForbiddenException::class.java) { exception, request, response ->
            response.status(403)
            response.body(exception.message)
        }
        Spark.exception(Exception::class.java) { exception, request, response ->
            exception.printStackTrace()
            response.status(500)
            response.body(exception.message)
        }


        // change port if needed
        if (args.size > 0) {
            Spark.port(Integer.parseInt(args[0]))
        }
        if (args.size > 1) {
            System.getProperties().setProperty("mongoDbPort", args[1])
        }


        // default json response
        val f:Filter = Filter(){ request: Request, response: Response -> response.type("application/json")}
        Spark.before(f)

        // create guice injector
        val injector = Guice.createInjector(AppInjector())

        // instanciate the objects that need injections
        val dataResource = injector.getInstance(DataResource::class.java)
        val authResource = injector.getInstance(AuthResource::class.java)
        val versionResource = injector.getInstance(VersionResource::class.java)
        val factuurResource = injector.getInstance(FactuurResource::class.java)
        val contactResource = injector.getInstance(ContactResource::class.java)
        val gebruikerResource = injector.getInstance(GebruikerResource::class.java)
        val rekeningResource = injector.getInstance(RekeningResource::class.java)
        val declaratieResource = injector.getInstance(DeclaratieResource::class.java)
        val afschriftResource = injector.getInstance(AfschriftResource::class.java)
        val administratieResource = injector.getInstance(AdministratieResource::class.java)
        val bestellingResource = injector.getInstance(BestellingResource::class.java)
        val rubriceerResource = injector.getInstance(RubriceerResource::class.java)
        val overzichtResource = injector.getInstance(OverzichtResource::class.java)
        val checkAndFixResource = injector.getInstance(CheckAndFixResource::class.java)
    }

    private fun loadVersion() {

        println("Load version from manifest")

        try {
            val resources = App::class.java.classLoader.getResources("META-INF/MANIFEST.MF")
            // walk through all manifest files (for each included jar there is a manifest,
            // we need find ours by checking the mainClass
            while (resources.hasMoreElements()) {
                val url = resources.nextElement()
                val properties = Properties()
                properties.load(url.openStream())
                val mainClass = properties.getProperty("Main-Class")
                if (mainClass != null && mainClass == App::class.java.canonicalName) {
                    //Correct manifest found
                    version = properties.getProperty("Implementation-Version")
                    buildTime = reformatBuildTime(properties.getProperty("Build-Time"))
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        println("Version is " + version)
        println("Buildtime is " + buildTime)
    }

    private fun reformatBuildTime(buildTime: String): String {
        try {
            val date = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").parse(buildTime.replace("Z$".toRegex(), "+0000"))
            return SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return "Unknown"

    }
}
