package com.vdzon.administratie.bankimport.regelParsers

class DefaultRegelParser : RegelParser {

    override fun match(omschrijving: String): Boolean {
        return false
    }


    override fun extractNaam(omschrijving: String): String {
        return "Onbekend"
    }

    override fun extractOmschrijving(omschrijving: String): String {
        return "Onbekend"
    }
}
