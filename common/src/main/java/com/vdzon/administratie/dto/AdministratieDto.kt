package com.vdzon.administratie.dto


import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.vdzon.administratie.model.*
import java.util.*

@JsonIgnoreProperties
class AdministratieDto(
        val uuid: String,
        val administratieGegevens: AdministratieGegevensDto,
        val facturen: List<FactuurDto>,
        val adresboek: List<ContactDto>,
        val rekeningen: List<RekeningDto>,
        val afschriften: List<AfschriftDto>,
        val declaraties: List<DeclaratieDto>,
        val bestellingen: List<BestellingDto>) {

    fun toAdministratie(): Administratie {
        return Administratie(
                uuid = uuid
                , bestellingen = toBestellingen()
                , facturen = toFacturen()
                , adresboek = toAdressen()
                , rekeningen = toRekeningen()
                , afschriften = toAfschriften()
                , declaraties = toDeclaraties()
                , administratieGegevens = (administratieGegevens.toAdministratieGegevens()))
    }

    private fun toBestellingen() = bestellingen.map { bestelling -> bestelling.toBestelling() }.toMutableList()
    private fun toFacturen() = facturen.map { factuur -> factuur.toFactuur() }.toMutableList()
    private fun toAdressen() = adresboek.map { klant -> klant.toContact() }.toMutableList()
    private fun toRekeningen() = rekeningen.map { rekening -> rekening.toRekening() }.toMutableList()
    private fun toDeclaraties() = declaraties.map { declaratie -> declaratie.toDeclaratie() }.toMutableList()
    private fun toAfschriften() = afschriften.map { afschrift -> afschrift.toAfschrift() }.toMutableList()

    companion object {
        fun toDto(administratie: Administratie): AdministratieDto {
            val boekingenCache = BoekingenCache(administratie.boekingen)
            return AdministratieDto(
                    uuid = administratie.uuid,
                    administratieGegevens = AdministratieGegevensDto.toDto(administratie.administratieGegevens),
                    adresboek = toAdressenDto(administratie.adresboek),
                    afschriften = toAfschriftenDto(administratie.afschriften, boekingenCache),
                    bestellingen = toBestellingenDto(administratie.bestellingen?: ArrayList()),
                    declaraties = toDeclaratiesDto(administratie.declaraties?: ArrayList(), boekingenCache),
                    facturen = toFacturenDto(administratie.facturen, boekingenCache),
                    rekeningen = toRekeningenDto(administratie.rekeningen, boekingenCache))
        }

        private fun toBestellingenDto(bestellingen: List<Bestelling>): List<BestellingDto> {
            return bestellingen
                    .map { bestelling -> BestellingDto.toDto(bestelling) }
                    .sortedBy { it.bestellingNummer }
        }

        private fun toRekeningenDto(rekeningen: List<Rekening>, boekingenCache: BoekingenCache): List<RekeningDto> {
            return rekeningen
                    .map { rekening -> RekeningDto.toDto(rekening, boekingenCache) }
                    .sortedBy { it.rekeningNummer }
        }

        private fun toAfschriftenDto(afschriften: List<Afschrift>, boekingenCache: BoekingenCache): List<AfschriftDto> {
            return afschriften
                    .map { afschrift -> AfschriftDto.toDto(afschrift, boekingenCache) }
                    .sortedBy { it.nummer }
        }

        private fun toDeclaratiesDto(declaraties: List<Declaratie>, boekingenCache: BoekingenCache): List<DeclaratieDto> {
            return declaraties
                    .map { declaratie -> DeclaratieDto.toDto(declaratie) }
                    .sortedBy { it.declaratieNummer }
        }

        private fun toAdressenDto(klanten: List<Contact>): List<ContactDto> {
            return klanten
                    .map { klant -> ContactDto.toDto(klant) }
                    .sortedBy { it.klantNummer }
        }

        private fun toFacturenDto(facturen: List<Factuur>, boekingenCache: BoekingenCache): List<FactuurDto> {
            return facturen
                    .map { factuur -> FactuurDto.toDto(factuur, boekingenCache) }
                    .sortedBy { it.factuurNummer }
        }
    }

}

