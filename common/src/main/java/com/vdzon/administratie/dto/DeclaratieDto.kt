package com.vdzon.administratie.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.vdzon.administratie.model.Declaratie

import java.math.BigDecimal
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@JsonIgnoreProperties
class DeclaratieDto (
    var uuid: String = "",
    var declaratieNummer: String = "",
    var omschrijving: String  = "",
    var declaratieDate: String  = "",
    var bedragExBtw:BigDecimal = BigDecimal.ZERO,
    var bedragIncBtw:BigDecimal = BigDecimal.ZERO,
    var btw:BigDecimal = BigDecimal.ZERO){

    private val DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy")

    fun toDeclaratie(): Declaratie = Declaratie(

                uuid=uuid,
                declaratieNummer=declaratieNummer,
                omschrijving=omschrijving,
                declaratieDate=LocalDate.parse(declaratieDate, DATE_FORMATTER),
                bedragExBtw=bedragExBtw,
                bedragIncBtw=bedragIncBtw,
                btw=btw)



    companion object {

        private val DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy")


        fun toDto(declaratie: Declaratie) = DeclaratieDto(
            uuid = declaratie.uuid,
            declaratieNummer = declaratie.declaratieNummer,
            omschrijving = declaratie.omschrijving,
            declaratieDate = declaratie.declaratieDate.format(DATE_FORMATTER),
            bedragExBtw = declaratie.bedragExBtw,
            bedragIncBtw = declaratie.bedragIncBtw,
            btw = declaratie.btw)

 }
}
