package com.vdzon.administratie.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.vdzon.administratie.model.Bestelling
import com.vdzon.administratie.model.BestellingRegel

import java.math.BigDecimal
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.ArrayList
import java.util.stream.Collectors

@JsonIgnoreProperties
class BestellingDto (
        val uuid: String = "",
        val bestellingNummer: String = "",
        val gekoppeldFactuurNummer: String? = null,
        val bestellingDate: String = "",
        val klant: ContactDto? = null,
        val bestellingRegels: List<BestellingRegelDto> = ArrayList(),
        val bedragExBtw: BigDecimal = BigDecimal.ZERO,
        val bedragIncBtw: BigDecimal = BigDecimal.ZERO,
        val btw:BigDecimal = BigDecimal.ZERO) {


    fun toBestelling(): Bestelling {
        return Bestelling(
                bestellingNummer=bestellingNummer,
                gekoppeldFactuurNummer=gekoppeldFactuurNummer,
                bestellingDate=LocalDate.parse(bestellingDate, DATE_FORMATTER),
                contact = if (klant == null) null else klant.toContact(),
                bestellingRegels = toBestellingRegels(),
                uuid = uuid)
    }

    private fun toBestellingRegels(): List<BestellingRegel> {
        return if (bestellingRegels == null)
            ArrayList<BestellingRegel>()
        else
            bestellingRegels!!
                    .map({ bestellingRegelDto -> bestellingRegelDto.toBestellingRegel() })
    }

    companion object {

        private val DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy")

        fun toDto(bestelling: Bestelling)  = BestellingDto(
            uuid = bestelling.uuid,
            bestellingNummer = bestelling.bestellingNummer,
            gekoppeldFactuurNummer = bestelling.gekoppeldFactuurNummer,
            bestellingDate = if (bestelling.bestellingDate == null) "" else bestelling.bestellingDate.format(DATE_FORMATTER),
            klant = if (bestelling.contact == null) null else ContactDto.toDto(bestelling.contact),
            bestellingRegels = toBestellingRegelsDto(bestelling.bestellingRegels),
            bedragExBtw = bestelling.bedragExBtw,
            bedragIncBtw = bestelling.bedragIncBtw,
            btw = bestelling.btw)


        private fun toBestellingRegelsDto(bestellingRegels: List<BestellingRegel>): List<BestellingRegelDto> {
            return bestellingRegels.map{ bestellingRegel -> BestellingRegelDto.toDto(bestellingRegel) }

        }
    }
}
