package com.vdzon.administratie.abnamrobankimport.regelParsers

class DefaultRegelParser : RegelParser {
    override fun match(omschrijving: String): Boolean = false
    override fun extractNaam(omschrijving: String): String = "Onbekend"
    override fun extractOmschrijving(omschrijving: String) = "Onbekend"
}
