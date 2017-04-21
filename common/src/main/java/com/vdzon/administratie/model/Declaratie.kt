package com.vdzon.administratie.model

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import org.mongodb.morphia.annotations.Entity
import org.mongodb.morphia.annotations.Id

import java.math.BigDecimal
import java.time.LocalDate

@Entity("declaratie")
class Declaratie (

    @Id
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
