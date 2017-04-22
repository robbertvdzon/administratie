package com.vdzon.administratie.model

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import org.litote.kmongo.MongoId
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDate
import java.util.*

data class Factuur(
        @MongoId
        val uuid: String = "",
        val factuurNummer: String = "",
        val gekoppeldeBestellingNummer: String? = null,
        @JsonSerialize(using = LocalDateSerializer::class)
        @JsonDeserialize(using = LocalDateDeserializer::class)
        val factuurDate: LocalDate? = null,
        val contact: Contact? = null,
        val factuurRegels: List<FactuurRegel> = ArrayList(),
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
        if (factuurRegels != null) {
            for (factuurRegel in factuurRegels!!) {
                val regelBedragEx = factuurRegel.stuksPrijs.multiply(factuurRegel.aantal)
                var regelBedragBtw = regelBedragEx.multiply(factuurRegel.btwPercentage.divide(BigDecimal.valueOf(100)))
                regelBedragBtw = regelBedragBtw.setScale(2, RoundingMode.HALF_UP)
                val regelBedragInc = regelBedragEx.add(regelBedragBtw)

                bedragExBtw = bedragExBtw.add(regelBedragEx)
                btw = btw.add(regelBedragBtw)
                bedragIncBtw = bedragIncBtw.add(regelBedragInc)
            }
        }
        //
        //        bedragIncBtw = bedragIncBtw.setScale(2, RoundingMode.HALF_UP);
        //        System.out.println(bedragIncBtw);
    }

}
