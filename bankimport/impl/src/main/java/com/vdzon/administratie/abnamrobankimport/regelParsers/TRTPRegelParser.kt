package com.vdzon.administratie.abnamrobankimport.regelParsers

class TRTPRegelParser : RegelParser {

    //TODO: deze class kan wat mooier

    override fun match(omschrijving: String): Boolean = omschrijving.startsWith("/TRTP")

    override fun extractNaam(omschrijving: String): String {
        val split = omschrijving.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        for (i in split.indices) {
            if (split[i] == "NAME") {
                return split[i + 1]
            }
        }
        return "Onbekend"
    }

    override fun extractOmschrijving(omschrijving: String): String {
        val split = omschrijving.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        for (i in split.indices) {
            if (split[i] == "REMI") {
                return split[i + 1]
            }
        }
        return omschrijving
    }
}
