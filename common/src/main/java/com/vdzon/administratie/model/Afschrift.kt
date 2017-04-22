package com.vdzon.administratie.model


import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import org.litote.kmongo.MongoId

import java.math.BigDecimal
import java.time.LocalDate

class Afschrift(
        @MongoId
        val uuid: String = "",
        val nummer: String = "",
        val rekening: String? = "",
        val omschrijving: String = "",
        val relatienaam: String = "",
        @JsonSerialize(using = LocalDateSerializer::class)
        @JsonDeserialize(using = LocalDateDeserializer::class)
        val boekdatum: LocalDate = LocalDate.now(),
        val bedrag: BigDecimal = BigDecimal.ZERO) {
}
