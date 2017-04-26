package com.vdzon.administratie

import com.google.inject.AbstractModule
import com.vdzon.administratie.bankimport.ImportFromBank
import com.vdzon.administratie.abnamrobankimport.ImportFromAbnAmro
import com.vdzon.administratie.checkandfix.CheckService
import com.vdzon.administratie.checkandfix.CheckServiceImpl
import com.vdzon.administratie.checkandfix.FixService
import com.vdzon.administratie.checkandfix.FixServiceImpl
import com.vdzon.administratie.crud.UserCrud
import com.vdzon.administratie.crud.UserCrudImpl
import com.vdzon.administratie.pdfgenerator.factuur.GenerateFactuur
import com.vdzon.administratie.pdfgenerator.factuur.GenerateFactuurImpl
import com.vdzon.administratie.pdfgenerator.factuur.GenerateOverzicht
import com.vdzon.administratie.pdfgenerator.overzicht.GenerateOverzichtImpl
import com.vdzon.administratie.rest.rubriceren.RubriceerService
import com.vdzon.administratie.rest.rubriceren.RubriceerServiceImpl
import com.vdzon.administratie.rest.version.VersionData

class AppInjector : AbstractModule() {

    override fun configure() {
        bind(FixService::class.java).to(FixServiceImpl::class.java)
        bind(CheckService::class.java).to(CheckServiceImpl::class.java)
        bind(ImportFromBank::class.java).to(ImportFromAbnAmro::class.java)
        bind(GenerateFactuur::class.java).to(GenerateFactuurImpl::class.java)
        bind(GenerateOverzicht::class.java).to(GenerateOverzichtImpl::class.java)
        bind(UserCrud::class.java).to(UserCrudImpl::class.java)
        bind(RubriceerService::class.java).to(RubriceerServiceImpl::class.java)

        bind(VersionData::class.java).to(VersionDataImpl::class.java).asEagerSingleton()
    }

}