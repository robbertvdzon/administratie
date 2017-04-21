package com.vdzon.administratie.model


import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import org.mongodb.morphia.annotations.Entity
import org.mongodb.morphia.annotations.Id

import java.math.BigDecimal
import java.time.LocalDate

@Entity("afschrift")
class Afschrift (
        @Id
    val uuid: String = "",
        val nummer: String = "",
        val rekening: String = "",
        val omschrijving: String = "",
        val relatienaam: String = "",
        @JsonSerialize(using = LocalDateSerializer::class)
    @JsonDeserialize(using = LocalDateDeserializer::class)
    val boekdatum: LocalDate = LocalDate.now(),
        val bedrag: BigDecimal = BigDecimal.ZERO) {
}
