package com.vdzon.administratie.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.vdzon.administratie.extensions.ADMINISTRATIE_DATE_FORMATTER
import com.vdzon.administratie.model.BoekingenCache
import com.vdzon.administratie.model.Factuur
import com.vdzon.administratie.model.FactuurRegel
import com.vdzon.administratie.model.boekingen.Boeking
import com.vdzon.administratie.model.boekingen.relaties.BoekingMetFactuur

import java.math.BigDecimal
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.ArrayList
import java.util.stream.Collectors

@JsonIgnoreProperties(ignoreUnknown = true)
class FactuurDto (
    val uuid: String = "",
    val factuurNummer: String = "",
    val gekoppeldeBestellingNummer: String? = null,
    val factuurDate: String? = null,
    val klant: ContactDto? = null,
    val factuurRegels: List<FactuurRegelDto>? = ArrayList(),
    val bedragExBtw:BigDecimal? = BigDecimal.ZERO,
    val bedragIncBtw:BigDecimal? = BigDecimal.ZERO,
    val btw:BigDecimal? = BigDecimal.ZERO,
    val boekingen: List<BoekingDto>? = ArrayList()
){

    fun toFactuur(): Factuur = Factuur(
                factuurNummer=factuurNummer,
                gekoppeldeBestellingNummer=gekoppeldeBestellingNummer,
                factuurDate=LocalDate.parse(factuurDate, ADMINISTRATIE_DATE_FORMATTER),
                contact= if (klant == null) null else klant.toContact(),
                factuurRegels=toFactuurRegels(),
                uuid=uuid)

    private fun toFactuurRegels(): List<FactuurRegel> {
        return if (factuurRegels == null)
            ArrayList<FactuurRegel>()
        else
            factuurRegels.map{ factuurRegelDto -> factuurRegelDto.toFactuurRegel() }
    }


    companion object {

        fun toDto(factuur: Factuur, boekingenCache: BoekingenCache) = FactuurDto(
            uuid = factuur.uuid,
            factuurNummer = factuur.factuurNummer,
            gekoppeldeBestellingNummer = factuur.gekoppeldeBestellingNummer,
            factuurDate = factuur.factuurDate?.format(ADMINISTRATIE_DATE_FORMATTER),
            klant = if (factuur.contact == null) null else ContactDto.toDto(factuur.contact),
            factuurRegels = toFactuurRegelsDto(factuur.factuurRegels),
            bedragExBtw = factuur.bedragExBtw,
            bedragIncBtw = factuur.bedragIncBtw,
            btw = factuur.btw,
            boekingen = toBoekingenDto(boekingenCache.getBoekingenVanFactuur(factuur.factuurNummer), boekingenCache))

        private fun toFactuurRegelsDto(factuurRegels: List<FactuurRegel>): List<FactuurRegelDto> {
            return factuurRegels.map{ factuurRegel -> FactuurRegelDto.toDto(factuurRegel) }
        }

        private fun toBoekingenDto(boekingen: List<BoekingMetFactuur>, boekingenCache: BoekingenCache): List<BoekingDto> {
            return if (boekingen == null)
                ArrayList()
            else
                boekingen.map{ boeking -> BoekingDto.toDto(boeking as Boeking) }
        }

    }
}
