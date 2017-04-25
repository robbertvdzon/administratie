package com.vdzon.administratie.model

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import org.litote.kmongo.MongoId

import java.math.BigDecimal
import java.time.LocalDate

class Declaratie (

    @MongoId
    val uuid: String,
    val declaratieNummer: String,
    val omschrijving: String,
    @JsonSerialize(using = LocalDateSerializer::class)
    @JsonDeserialize(using = LocalDateDeserializer::class)
    val declaratieDate: LocalDate,
    var bedragExBtw:BigDecimal = BigDecimal.ZERO,
    var bedragIncBtw:BigDecimal = BigDecimal.ZERO,
    var btw:BigDecimal = BigDecimal.ZERO) {
    }
