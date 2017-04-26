package com.vdzon.administratie

import com.github.mongobee.exception.MongobeeException
import com.google.inject.*
import com.google.inject.binder.AnnotatedBindingBuilder
import com.vdzon.administratie.bankimport.ImportFromBank
import com.vdzon.administratie.abnamrobankimport.ImportFromAbnAmro
import com.vdzon.administratie.mongo.Mongo
import com.vdzon.administratie.rest.administratie.AdministratieResource
import com.vdzon.administratie.rest.afschrift.AfschriftResource
import com.vdzon.administratie.rest.auth.AuthResource
import com.vdzon.administratie.rest.bestelling.BestellingResource
import com.vdzon.administratie.rest.checkandfix.CheckAndFixResource
import com.vdzon.administratie.util.ForbiddenException
import com.vdzon.administratie.rest.contact.ContactResource
import com.vdzon.administratie.rest.data.DataResource
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
import com.vdzon.administratie.rest.version.VersionData

object App {

    @Throws(MongobeeException::class)
    @JvmStatic fun main(args: Array<String>) {

        startMongo()

        // init spark with web statics
        initWebResources()
        initExceptionHandlers()
        initSparkPort(args)
        initJsonResonce()

        val injector = createDependencyInjector()

        initAllRestResources(injector)

        loadApplicationVersion(injector)


    }

    private fun startMongo() = Mongo.start()

    private fun createDependencyInjector() = Guice.createInjector(AppInjector())

    private fun initAllRestResources(injector: Injector) {
        injector.getInstance(DataResource::class.java)
        injector.getInstance(AuthResource::class.java)
        injector.getInstance(VersionResource::class.java)
        injector.getInstance(FactuurResource::class.java)
        injector.getInstance(ContactResource::class.java)
        injector.getInstance(GebruikerResource::class.java)
        injector.getInstance(RekeningResource::class.java)
        injector.getInstance(DeclaratieResource::class.java)
        injector.getInstance(AfschriftResource::class.java)
        injector.getInstance(AdministratieResource::class.java)
        injector.getInstance(BestellingResource::class.java)
        injector.getInstance(RubriceerResource::class.java)
        injector.getInstance(OverzichtResource::class.java)
        injector.getInstance(CheckAndFixResource::class.java)
    }

    private fun initJsonResonce() {
        val f: Filter = Filter() { request: Request, response: Response -> response.type("application/json") }
        Spark.before(f)
    }

    private fun initSparkPort(args: Array<String>) {
        if (args.isNotEmpty()) Spark.port(Integer.parseInt(args[0]))
    }

    private fun initExceptionHandlers() {
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
    }

    private fun initWebResources() {
        if (File("C:\\git\\administratie\\applicatie\\src\\main\\resources\\web").exists()) {
            Spark.externalStaticFileLocation("C:\\git\\administratie\\applicatie\\src\\main\\resources\\web")
        } else {
            Spark.staticFileLocation("/web")
        }
    }

    private fun loadApplicationVersion(injector: Injector) {

        val versionData = injector.getInstance(VersionData::class.java)

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
                    versionData.version = properties.getProperty("Implementation-Version")
                    versionData.buildTime = reformatBuildTime(properties.getProperty("Build-Time"))
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
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
