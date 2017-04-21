package com.vdzon.administratie.bankimport


import com.vdzon.administratie.bankimport.ImportFromAbnAmroHelper.buildAfschrift
import com.vdzon.administratie.bankimport.ImportFromAbnAmroHelper.findNextAfschriftNummer
import com.vdzon.administratie.bankimport.ImportFromAbnAmroHelper.getBestaandAfschriftMetDezeUuid
import com.vdzon.administratie.bankimport.ImportFromAbnAmroHelper.parseAfschriftData
import com.vdzon.administratie.bankimport.regelParsers.DefaultRegelParser
import com.vdzon.administratie.bankimport.regelParsers.RegelParser
import com.vdzon.administratie.bankimport.regelParsers.SepaRegelParser
import com.vdzon.administratie.bankimport.regelParsers.TRTPRegelParser
import com.vdzon.administratie.model.Afschrift
import com.vdzon.administratie.model.Gebruiker

import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.util.ArrayList
import java.util.stream.Collectors
import java.util.stream.Stream


class ImportFromAbnAmro {


    fun parseFile(out: Path, gebruiker: Gebruiker): List<Afschrift> {
        val nextNummerHolder = NextNummerHolder(findNextAfschriftNummer(gebruiker))
        try {
            return Files.lines(out).map({ line:String -> parseLine(line, gebruiker, nextNummerHolder) }).collect(Collectors.toList<Afschrift>())
        } catch (e: IOException) {
            e.printStackTrace()
            return ArrayList()
        }
    }

    private fun parseLine(line: String, gebruiker: Gebruiker, nextNummerHolder: NextNummerHolder): Afschrift? {
        val afschriftData = parseAfschriftData(line, { str:String -> extractNaam(str) } , { str -> extractOmschrijving(str) }) ?: return null

        val bestaandAfschrift = getBestaandAfschriftMetDezeUuid(gebruiker, afschriftData.uuid)
        if (bestaandAfschrift != null) {
            return null
        }
        return buildAfschrift(nextNummerHolder, afschriftData)
    }

    private fun extractNaam(omschrijving: String): String {
        val regel = regelParsers.filter{ parser -> parser.match(omschrijving) }.firstOrNull()?:DefaultRegelParser()
        return regel.extractNaam(omschrijving)
    }

    private fun extractOmschrijving(omschrijving: String): String {
        val regel = regelParsers.filter{ parser -> parser.match(omschrijving) }.firstOrNull()?:DefaultRegelParser()
        return regel.extractOmschrijving(omschrijving)
    }

    private val regelParsers: List<RegelParser>
        get() {
            val parsers = ArrayList<RegelParser>()
            parsers.add(SepaRegelParser())
            parsers.add(TRTPRegelParser())
            return parsers
        }
}