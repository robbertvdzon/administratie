package com.vdzon.administratie.bankimport.regelParsers

interface RegelParser {

    fun match(omschrijving: String): Boolean

    fun extractNaam(omschrijving: String): String

    fun extractOmschrijving(omschrijving: String): String
}
