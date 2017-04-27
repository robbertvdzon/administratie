package com.vdzon.administratie

import com.github.mongobee.exception.MongobeeException
import com.google.inject.Guice
import com.google.inject.Injector
import com.vdzon.administratie.mongo.Mongo
import com.vdzon.administratie.rest.Rest

object App {

    @Throws(MongobeeException::class)
    @JvmStatic fun main(args: Array<String>) {

        startMongo()

        val injector: Injector = createDependencyInjector()
        AppUtil.loadApplicationVersion(injector)

        startRest(injector)

    }

    private fun startRest(injector: Injector) {
        val rest: Rest = injector.getInstance(Rest::class.java)
        rest.initRest(injector);
    }


    private fun startMongo() = Mongo.start()
    private fun createDependencyInjector() = Guice.createInjector(AppInjector())

}
