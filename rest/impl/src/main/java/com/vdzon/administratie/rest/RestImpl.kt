package com.vdzon.administratie.rest

import com.google.inject.Injector
import com.vdzon.administratie.rest.administratie.AdministratieResource
import com.vdzon.administratie.rest.afschrift.AfschriftResource
import com.vdzon.administratie.rest.auth.AuthResource
import com.vdzon.administratie.rest.bestelling.BestellingResource
import com.vdzon.administratie.rest.checkandfix.CheckAndFixResource
import com.vdzon.administratie.rest.contact.ContactResource
import com.vdzon.administratie.rest.data.DataResource
import com.vdzon.administratie.rest.declaratie.DeclaratieResource
import com.vdzon.administratie.rest.factuur.FactuurResource
import com.vdzon.administratie.rest.gebruiker.GebruikerResource
import com.vdzon.administratie.rest.overzicht.OverzichtResource
import com.vdzon.administratie.rest.rekening.RekeningResource
import com.vdzon.administratie.rest.rubriceren.RubriceerResource
import com.vdzon.administratie.rest.version.VersionResource
import com.vdzon.administratie.util.ForbiddenException
import spark.Filter
import spark.Request
import spark.Response
import spark.Spark
import javax.inject.Inject


class RestImpl : Rest {

    override fun initRest(injector: Injector) {
        initWebResources()
        initExceptionHandlers()
        initJsonResonce()

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

    private fun initJsonResonce() = Spark.before(Filter() { request: Request, response: Response -> response.type("application/json") })

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
//        if (File("C:\\git\\administratie\\applicatie\\src\\main\\resources\\web").exists()) {
//            Spark.externalStaticFileLocation("C:\\git\\administratie\\applicatie\\src\\main\\resources\\web")
//        } else {
            Spark.staticFileLocation("/web")
//        }
    }


}