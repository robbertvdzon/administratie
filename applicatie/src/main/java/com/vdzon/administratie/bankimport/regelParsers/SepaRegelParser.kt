package com.vdzon.administratie.bankimport.regelParsers

import com.vdzon.administratie.bankimport.ImportFromAbnAmroHelper.findNextKeyword

class SepaRegelParser : RegelParser {

    //TODO: deze class kan wat mooier

    override fun match(omschrijving: String): Boolean {
        return omschrijving.startsWith("SEPA")
    }

    override fun extractNaam(omschrijving: String): String {
        val pos = omschrijving.indexOf("Naam:")
        val posStart = pos + "Naam:".length
        if (pos > 0) {
            val nextKeyword = findNextKeyword(omschrijving.substring(posStart))
            if (nextKeyword == null) {
                return omschrijving.substring(posStart)
            } else {
                val posEnd = posStart + omschrijving.substring(posStart).indexOf(nextKeyword)
                return omschrijving.substring(posStart, posEnd - 1)
            }
        }
        return "Onbekend"
    }

    override fun extractOmschrijving(omschrijving: String): String {
        val pos = omschrijving.indexOf("Omschrijving:")
        val posStart = pos + "Omschrijving:".length
        if (pos > 0) {
            val nextKeyword = findNextKeyword(omschrijving.substring(posStart))
            if (nextKeyword == null) {
                return omschrijving.substring(posStart)
            } else {
                val posEnd = posStart + omschrijving.substring(posStart).indexOf(nextKeyword)
                return omschrijving.substring(posStart, posEnd - 1)
            }
        }
        return omschrijving
    }
}