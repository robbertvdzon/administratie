package com.vdzon.administratie.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.vdzon.administratie.model.boekingen.Boeking
import com.vdzon.administratie.model.boekingen.relaties.BoekingMetAfschrift
import com.vdzon.administratie.model.boekingen.relaties.BoekingMetFactuur
import com.vdzon.administratie.model.boekingen.relaties.BoekingMetRekening

@JsonIgnoreProperties
class BoekingDto(
        val uuid: String = "",
        val omschrijving: String = "",
        val factuurNummer: String? = null,
        val rekeningNummer: String? = null,
        val afschriftNummer: String? = null,
        val declaratieNummer: String? = null) {

    companion object {
        fun toDto(boeking: Boeking) = BoekingDto(
                omschrijving = boeking.omschrijving,
                uuid = boeking.uuid,
                afschriftNummer = if (boeking is BoekingMetAfschrift) boeking.afschriftNummer else null,
                factuurNummer = if (boeking is BoekingMetFactuur) boeking.factuurNummer else null,
                rekeningNummer = if (boeking is BoekingMetRekening) boeking.rekeningNummer else null
        )
    }

}
