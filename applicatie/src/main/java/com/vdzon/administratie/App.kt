package com.vdzon.administratie

import com.google.inject.Guice
import com.google.inject.Injector
import com.vdzon.administratie.database.AdministratieDatabase
import com.vdzon.administratie.rest.Rest

object App {

    @JvmStatic fun main(args: Array<String>) {
        val injector: Injector = createDependencyInjector()
        AppUtil.loadApplicationVersion(injector)
        startMongo(injector)
        startRest(injector)
    }

    private fun createDependencyInjector() = Guice.createInjector(AppInjector())

    private fun startRest(injector: Injector) {
        val rest: Rest = injector.getInstance(Rest::class.java)
        rest.initRest(injector)
    }

    private fun startMongo(injector: Injector) {
        val database = injector.getInstance(AdministratieDatabase::class.java)
        database.startDatabase()
    }


}
