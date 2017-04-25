package com.vdzon.administratie.dto

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.vdzon.administratie.extensions.ADMINISTRATIE_DATE_FORMATTER
import com.vdzon.administratie.extensions.normalizedCopy
import com.vdzon.administratie.model.Afschrift
import com.vdzon.administratie.model.BoekingenCache
import com.vdzon.administratie.model.boekingen.Boeking
import com.vdzon.administratie.model.boekingen.relaties.BoekingMetAfschrift
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*

@JsonIgnoreProperties
class AfschriftDto(
        var uuid: String = "",
        var nummer: String = "",
        var rekening: String = "",
        var omschrijving: String = "",
        var relatienaam: String = "",
        var boekdatum: String = "",
        var bedrag: BigDecimal = BigDecimal.ZERO,
        var boekingen: List<BoekingDto>? = ArrayList()) {


    fun toAfschrift(): Afschrift = Afschrift(
                uuid=uuid,
                nummer=nummer,
                rekening=rekening,
                omschrijving=omschrijving,
                relatienaam=relatienaam,
                boekdatum=LocalDate.parse(boekdatum, ADMINISTRATIE_DATE_FORMATTER),
                bedrag=bedrag.normalizedCopy()
    )

    companion object {

        fun toDto(afschrift: Afschrift, boekingenCache: BoekingenCache): AfschriftDto = AfschriftDto(
                uuid = afschrift.uuid,
                nummer = afschrift.nummer,
                rekening = afschrift.rekening?:"",
                omschrijving = afschrift.omschrijving,
                relatienaam = afschrift.relatienaam,
                boekdatum = if (afschrift.boekdatum == null) "" else afschrift.boekdatum.format(ADMINISTRATIE_DATE_FORMATTER),
                bedrag = afschrift.bedrag.normalizedCopy(),
                boekingen = toBoekingenDto(boekingenCache.getBoekingenVanAfschrift(afschrift.nummer), boekingenCache))

        private fun toBoekingenDto(boekingen: List<BoekingMetAfschrift>?, boekingenCache: BoekingenCache): List<BoekingDto>? =
                if (boekingen == null) null
                else boekingen.map { boeking -> BoekingDto.toDto(boeking as Boeking) }

    }
}
