package com.vdzon.administratie.model

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import org.litote.kmongo.MongoId
import java.io.Serializable

import java.math.BigDecimal
import java.time.LocalDate

class Rekening(
        @MongoId
        val uuid: String = "",
        val rekeningNummer: String = "",
        val factuurNummer: String? = "",
        val naam: String = "",
        val omschrijving: String = "",
        @JsonSerialize(using = LocalDateSerializer::class)
        @JsonDeserialize(using = LocalDateDeserializer::class)
        val rekeningDate: LocalDate = LocalDate.now(),
        val bedragExBtw: BigDecimal = BigDecimal.ZERO,
        val bedragIncBtw: BigDecimal = BigDecimal.ZERO,
        val btw: BigDecimal = BigDecimal.ZERO,
        var maandenAfschrijving: Int = 0) : Serializable {
}
