package com.vdzon.administratie.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.vdzon.administratie.model.BoekingenCache
import com.vdzon.administratie.model.Rekening
import com.vdzon.administratie.model.boekingen.Boeking
import com.vdzon.administratie.model.boekingen.relaties.BoekingMetRekening

import java.math.BigDecimal
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.stream.Collectors

@JsonIgnoreProperties
class RekeningDto (
    var uuid: String,
    var rekeningNummer: String,
    var factuurNummer: String,
    var naam: String,
    var omschrijving: String,
    var rekeningDate: String,
    var bedragExBtw:BigDecimal = BigDecimal.ZERO,
    var bedragIncBtw:BigDecimal = BigDecimal.ZERO,
    var btw:BigDecimal = BigDecimal.ZERO,
    var boekingen: List<BoekingDto> = ArrayList(),
    var maandenAfschrijving:Int = 0){

    private val DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy")

    fun toRekening(): Rekening = Rekening(
                uuid=uuid,
                rekeningNummer=rekeningNummer,
                factuurNummer=factuurNummer,
                naam=naam,
                omschrijving=omschrijving,
                rekeningDate=LocalDate.parse(rekeningDate, DATE_FORMATTER),
                bedragExBtw=bedragExBtw,
                bedragIncBtw=bedragIncBtw,
                btw=btw,
                maandenAfschrijving=maandenAfschrijving)

    companion object {
        private val DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy")

        fun toDto(rekening: Rekening, boekingenCache: BoekingenCache) = RekeningDto(
            uuid = rekening.uuid,
            rekeningNummer = rekening.rekeningNummer,
            factuurNummer = rekening.factuurNummer?:"",
            naam = rekening.naam,
            omschrijving = rekening.omschrijving,
            rekeningDate = if (rekening.rekeningDate == null) "" else rekening.rekeningDate.format(DATE_FORMATTER),
            bedragExBtw = rekening.bedragExBtw,
            bedragIncBtw = rekening.bedragIncBtw,
            btw = rekening.btw,
            boekingen = toBoekingenDto(boekingenCache.getBoekingenVanRekening(rekening.rekeningNummer), boekingenCache),
            maandenAfschrijving = rekening.maandenAfschrijving)

        private fun toBoekingenDto(boekingen: List<BoekingMetRekening>, boekingenCache: BoekingenCache): List<BoekingDto> = boekingen.
                map{ boeking -> BoekingDto.toDto(boeking as Boeking) }


    }
}
