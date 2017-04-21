package com.vdzon.administratie.model

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import org.mongodb.morphia.annotations.Entity
import org.mongodb.morphia.annotations.Id

import java.math.BigDecimal
import java.time.LocalDate

@Entity("rekening")
class Rekening (
        @Id
    val uuid: String = "",
        val rekeningNummer: String = "",
        val factuurNummer: String = "",
        val naam: String = "",
        val omschrijving: String = "",
        @JsonSerialize(using = LocalDateSerializer::class)
    @JsonDeserialize(using = LocalDateDeserializer::class)
    val rekeningDate: LocalDate = LocalDate.now(),
        val bedragExBtw: BigDecimal = BigDecimal.ZERO,
        val bedragIncBtw: BigDecimal = BigDecimal.ZERO,
        val btw: BigDecimal = BigDecimal.ZERO,
        var maandenAfschrijving: Int = 0){
}
