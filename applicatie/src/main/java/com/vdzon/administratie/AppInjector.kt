package com.vdzon.administratie

import com.google.inject.AbstractModule
import com.vdzon.administratie.bankimport.ImportFromBank
import com.vdzon.administratie.abnamrobankimport.ImportFromAbnAmro
import com.vdzon.administratie.crud.UserCrud
import com.vdzon.administratie.crud.UserCrudImpl
import com.vdzon.administratie.pdfgenerator.factuur.GenerateFactuur
import com.vdzon.administratie.pdfgenerator.factuur.GenerateFactuurImpl
import com.vdzon.administratie.pdfgenerator.factuur.GenerateOverzicht
import com.vdzon.administratie.pdfgenerator.overzicht.GenerateOverzichtImpl

class AppInjector : AbstractModule() {

    override fun configure() {
        bind(ImportFromBank::class.java).to(ImportFromAbnAmro::class.java)
        bind(GenerateFactuur::class.java).to(GenerateFactuurImpl::class.java)
        bind(GenerateOverzicht::class.java).to(GenerateOverzichtImpl::class.java)
        bind(UserCrud::class.java).to(UserCrudImpl::class.java)

    }

}