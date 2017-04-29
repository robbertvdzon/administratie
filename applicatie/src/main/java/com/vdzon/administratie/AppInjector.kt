package com.vdzon.administratie

import com.google.inject.AbstractModule
import com.vdzon.administratie.bankimport.ImportFromBank
import com.vdzon.administratie.abnamrobankimport.ImportFromAbnAmro
import com.vdzon.administratie.authenticatie.AuthenticationService
import com.vdzon.administratie.authenticatie.AuthenticationServiceImpl
import com.vdzon.administratie.checkandfix.CheckService
import com.vdzon.administratie.checkandfix.CheckServiceImpl
import com.vdzon.administratie.checkandfix.FixService
import com.vdzon.administratie.checkandfix.FixServiceImpl
import com.vdzon.administratie.database.UserDao
import com.vdzon.administratie.crud.UserDaoImpl
import com.vdzon.administratie.database.AdministratieDatabase
import com.vdzon.administratie.database.MongoDatabase
import com.vdzon.administratie.pdfgenerator.factuur.GenerateFactuur
import com.vdzon.administratie.pdfgenerator.factuur.GenerateFactuurImpl
import com.vdzon.administratie.pdfgenerator.factuur.GenerateOverzicht
import com.vdzon.administratie.pdfgenerator.overzicht.GenerateOverzichtImpl
import com.vdzon.administratie.rest.Rest
import com.vdzon.administratie.rest.RestImpl
import com.vdzon.administratie.rest.rubriceren.RubriceerService
import com.vdzon.administratie.rest.rubriceren.RubriceerServiceImpl
import com.vdzon.administratie.rest.version.VersionData

class AppInjector : AbstractModule() {

    override fun configure() {
        bind(AdministratieDatabase::class.java).to(MongoDatabase::class.java).asEagerSingleton()
        bind(Rest::class.java).to(RestImpl::class.java).asEagerSingleton()
        bind(FixService::class.java).to(FixServiceImpl::class.java).asEagerSingleton()
        bind(CheckService::class.java).to(CheckServiceImpl::class.java).asEagerSingleton()
        bind(ImportFromBank::class.java).to(ImportFromAbnAmro::class.java).asEagerSingleton()
        bind(GenerateFactuur::class.java).to(GenerateFactuurImpl::class.java).asEagerSingleton()
        bind(GenerateOverzicht::class.java).to(GenerateOverzichtImpl::class.java).asEagerSingleton()
        bind(UserDao::class.java).to(UserDaoImpl::class.java).asEagerSingleton()
        bind(RubriceerService::class.java).to(RubriceerServiceImpl::class.java).asEagerSingleton()
        bind(VersionData::class.java).to(VersionDataImpl::class.java).asEagerSingleton()
        bind(AuthenticationService::class.java).to(AuthenticationServiceImpl::class.java).asEagerSingleton()
    }

}