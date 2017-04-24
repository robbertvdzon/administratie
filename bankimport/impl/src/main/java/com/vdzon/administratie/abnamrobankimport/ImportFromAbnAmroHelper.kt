package com.vdzon.administratie.abnamrobankimport


import com.vdzon.administratie.model.Afschrift
import com.vdzon.administratie.dto.BoekingType
import com.vdzon.administratie.model.Gebruiker

import java.math.BigDecimal
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Comparator
import java.util.function.Function

object ImportFromAbnAmroHelper {

    val START_AFSCHRIFT_NUMMER = 1000

    fun buildAfschrift(nextNummerHolder: NextNummerHolder, afschriftData: AfschriftData): Afschrift {
        val bedrag = getBedrag(afschriftData.bedragStr)
        val boekDatum = getBoekDatum(afschriftData.date)
        val nextAfschriftNummer = nextNummerHolder.nextNummer++
        return Afschrift(afschriftData.uuid!!,
                "" + nextAfschriftNummer,
                afschriftData.rekeningNr!!,
                afschriftData.oms!!,
                afschriftData.naam!!,
                boekDatum,
                bedrag)
    }

    fun parseAfschriftData(line: String, extractNaam: (String)->String, extractOmschrijving: (String)->String): AfschriftData? {
        val parts = line.split("\t".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        if (parts.size != 8) {
            return null
        }
        val omschrijving = parts[7]
        val afschriftData = AfschriftData()
        afschriftData.rekeningNr = parts[0]
        afschriftData.date = parts[2]
        afschriftData.bedragStr = parts[6]
        afschriftData.omschrijving = omschrijving
        afschriftData.naam = extractNaam.invoke(omschrijving)
        afschriftData.oms = extractOmschrijving.invoke(omschrijving)
        afschriftData.uuid = afschriftData.rekeningNr!!.trim { it <= ' ' } + afschriftData.date!!.trim { it <= ' ' } + afschriftData.bedragStr + afschriftData.omschrijving!!.trim { it <= ' ' } + afschriftData.naam!!.trim { it <= ' ' } + afschriftData.oms!!.trim { it <= ' ' }
        return afschriftData
    }

    fun findNextAfschriftNummer(gebruiker: Gebruiker): Int {
        return 1 + (gebruiker.defaultAdministratie.afschriften.map({ afschrift -> Integer.parseInt(afschrift.nummer) }).maxWith(java.util.Comparator.naturalOrder<Int>()) ?: START_AFSCHRIFT_NUMMER)
    }

    fun findNextKeyword(s: String): String? {
        val words = s.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        for (word in words) {
            if (word.endsWith(":")) {
                return word
            }
        }
        return null
    }

    fun getBestaandAfschriftMetDezeUuid(gebruiker: Gebruiker, uuid: String?): Afschrift? {
        return gebruiker.defaultAdministratie.afschriften.filter{ afschrift -> afschrift.uuid == uuid }.firstOrNull()
    }

    fun getBoekDatum(date: String?): LocalDate {
        try {
            val formatter = DateTimeFormatter.ofPattern("yyyyMMdd")
            return LocalDate.parse(date, formatter)
        } catch (exc: DateTimeParseException) {
            exc.printStackTrace()
        }

        return LocalDate.now()

    }

    fun getBedrag(bedrag: String?): BigDecimal {
        return BigDecimal.valueOf(bedrag?.replace(",", ".")?.toDouble()?: Double.MIN_VALUE)
    }


}
