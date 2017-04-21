package com.vdzon.administratie.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.vdzon.administratie.model.BestellingRegel

import java.math.BigDecimal

@JsonIgnoreProperties
class BestellingRegelDto(
        val uuid: String = "",
        val omschrijving: String = "",
        val aantal: BigDecimal = BigDecimal.ZERO,
        val stuksPrijs: BigDecimal = BigDecimal.ZERO,
        val btwPercentage: BigDecimal = BigDecimal.ZERO
) {

    fun toBestellingRegel(): BestellingRegel = BestellingRegel(
                omschrijving=omschrijving,
            aantal=aantal,
            stuksPrijs=stuksPrijs,
            btwPercentage=btwPercentage,
            uuid=uuid)


    companion object {
        fun toDto(bestellingRegel: BestellingRegel): BestellingRegelDto = BestellingRegelDto(
                uuid = bestellingRegel.uuid,
                omschrijving = bestellingRegel.omschrijving,
                aantal = bestellingRegel.aantal,
                stuksPrijs = bestellingRegel.stuksPrijs,
                btwPercentage = bestellingRegel.btwPercentage
        )

    }
}
