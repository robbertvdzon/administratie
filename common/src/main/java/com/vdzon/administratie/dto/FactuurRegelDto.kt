package com.vdzon.administratie.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.vdzon.administratie.model.FactuurRegel

import java.math.BigDecimal

@JsonIgnoreProperties
class FactuurRegelDto (
    val uuid: String = "",
    val omschrijving: String = "",
    val aantal: BigDecimal = BigDecimal.ZERO,
    val stuksPrijs: BigDecimal = BigDecimal.ZERO,
    val btwPercentage: BigDecimal = BigDecimal.ZERO){

    fun toFactuurRegel(): FactuurRegel = FactuurRegel(
                omschrijving=omschrijving,
                aantal=aantal,
                stuksPrijs=stuksPrijs,
                btwPercentage=btwPercentage,
                uuid=uuid)

    companion object {
        fun toDto(factuurRegel: FactuurRegel) = FactuurRegelDto(
            uuid = factuurRegel.uuid,
            omschrijving = factuurRegel.omschrijving,
            aantal = factuurRegel.aantal,
            stuksPrijs = factuurRegel.stuksPrijs,
            btwPercentage = factuurRegel.btwPercentage)
    }
}
