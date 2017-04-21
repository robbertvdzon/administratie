package com.vdzon.administratie.model

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import org.mongodb.morphia.annotations.Entity
import org.mongodb.morphia.annotations.Id
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDate
import java.util.*

@Entity("bestelling")
data class Bestelling(
        @Id
        val uuid: String = "",
        val bestellingNummer: String = "",
        val gekoppeldFactuurNummer: String? = null,
        @JsonSerialize(using = LocalDateSerializer::class)
        @JsonDeserialize(using = LocalDateDeserializer::class)
        val bestellingDate: LocalDate = LocalDate.now(),
        val contact: Contact? = null,
        val bestellingRegels: List<BestellingRegel> = ArrayList(),
        var bedragExBtw: BigDecimal = BigDecimal.ZERO,
        var bedragIncBtw: BigDecimal = BigDecimal.ZERO,
        var btw: BigDecimal = BigDecimal.ZERO) {

    init {
        calculate()
    }

    private fun calculate() {
        bedragExBtw = BigDecimal.ZERO
        bedragIncBtw = BigDecimal.ZERO
        btw = BigDecimal.ZERO
        for (bestellingRegel in bestellingRegels) {
            val regelBedragEx = bestellingRegel.stuksPrijs.multiply(bestellingRegel.aantal)
            var regelBedragBtw = regelBedragEx.multiply(bestellingRegel.btwPercentage.divide(BigDecimal.valueOf(100)))
            regelBedragBtw = regelBedragBtw.setScale(2, RoundingMode.HALF_UP)
            val regelBedragInc = regelBedragEx.add(regelBedragBtw)

            bedragExBtw = bedragExBtw.add(regelBedragEx)
            btw = btw.add(regelBedragBtw)
            bedragIncBtw = bedragIncBtw.add(regelBedragInc)
        }
    }
}
