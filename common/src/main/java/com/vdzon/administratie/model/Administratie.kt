package com.vdzon.administratie.model

import com.vdzon.administratie.model.boekingen.Boeking
import org.litote.kmongo.MongoId
import java.util.*

data class Administratie(@MongoId
                         val uuid: String = "",
                         val administratieGegevens: AdministratieGegevens? = AdministratieGegevens(),
                         val bestellingen: MutableList<Bestelling>? = ArrayList<Bestelling>(),
                         val facturen: MutableList<Factuur> = ArrayList<Factuur>(),
                         val adresboek: MutableList<Contact> = ArrayList<Contact>(),
                         val rekeningen: MutableList<Rekening> = ArrayList<Rekening>(),
                         val afschriften: MutableList<Afschrift> = ArrayList<Afschrift>(),
                         val declaraties: MutableList<Declaratie>? = ArrayList<Declaratie>(),
                         val boekingen: MutableList<Boeking> = ArrayList<Boeking>()) {

    fun addFactuur(factuur: Factuur) {
        facturen.add(factuur)
    }

    fun getFactuur(uuid: String): Factuur? {
        for (factuur in facturen) {
            if (factuurUuidMatchesUuid(uuid, factuur)) {
                return factuur
            }
        }
        return null
    }

    fun getFactuurByFactuurNummer(factuurNummer: String?): Factuur? {
        for (factuur in facturen) {
            if (factuurNummerMatchesFactuurNummer(factuurNummer, factuur)) {
                return factuur
            }
        }
        return null
    }

    fun removeFactuur(uuid: String) {
        var factuurToRemove: Factuur? = null
        for (factuur in facturen) {
            if (factuurUuidMatchesUuid(uuid, factuur)) {
                factuurToRemove = factuur
            }
        }
        if (factuurToRemove != null) {
            facturen.remove(factuurToRemove)
        }
    }

    private fun factuurUuidMatchesUuid(uuid: String?, factuur: Factuur): Boolean {
        return uuid == null && factuur.uuid == null || uuid != null && uuid == factuur.uuid
    }

    private fun factuurNummerMatchesFactuurNummer(factuurNummer: String?, factuur: Factuur): Boolean {
        return factuurNummer == null && factuur.factuurNummer == null || factuurNummer != null && factuurNummer == factuur.factuurNummer
    }

    fun addBestelling(bestelling: Bestelling) {
        bestellingen!!.add(bestelling)
    }

    fun getBestelling(uuid: String?): Bestelling? {
        for (bestelling in bestellingen!!) {
            if (bestellingNummerMatchesUuid(uuid, bestelling)) {
                return bestelling
            }
        }
        return null
    }

    fun getBestellingByBestellingNummer(bestellingNummer: String?): Bestelling? {
        if (bestellingNummer==null) return null
        for (bestelling in bestellingen!!) {
            if (bestellingNummerMatchesBestellingNummer(bestellingNummer, bestelling)) {
                return bestelling
            }
        }
        return null
    }


    fun removeBestelling(uuid: String?) {
        var bestellingToRemove: Bestelling? = null
        for (bestelling in bestellingen!!) {
            if (bestellingNummerMatchesUuid(uuid, bestelling)) {
                bestellingToRemove = bestelling
            }
        }
        if (bestellingToRemove != null) {
            bestellingen.remove(bestellingToRemove)
        }
    }

    private fun bestellingNummerMatchesUuid(uuid: String?, bestelling: Bestelling): Boolean {
        return uuid == null && bestelling.uuid == null || uuid != null && uuid == bestelling.uuid
    }

    private fun bestellingNummerMatchesBestellingNummer(bestellingNummer: String?, bestelling: Bestelling): Boolean {
        return bestellingNummer == null && bestelling.bestellingNummer == null || bestellingNummer != null && bestellingNummer == bestelling.bestellingNummer
    }


    fun addContact(contact: Contact) {
        adresboek.add(contact)
    }

    fun removeContact(uuid: String) {
        val adresboekClone = adresboek
        for (contact in adresboekClone) {
            if (contact.uuid == uuid) {
                this.adresboek.remove(contact)
            }
        }
    }

    fun addRekening(rekening: Rekening) {
        rekeningen.add(rekening)
    }

    fun removeRekening(uuid: String) {
        val rekeningenClone = rekeningen
        for (rekening in rekeningenClone) {
            if (rekening.uuid == uuid) {
                this.rekeningen.remove(rekening)
            }
        }
    }

    fun getRekening(uuid: String): Rekening? {
        for (rekening in rekeningen) {
            if (rekeningUuidMatchesUuid(uuid, rekening)) {
                return rekening
            }
        }
        return null
    }

    private fun rekeningUuidMatchesUuid(uuid: String?, rekening: Rekening): Boolean {
        return uuid == null && rekening.uuid == null || uuid != null && uuid == rekening.uuid
    }

    fun addDeclaratie(declaratie: Declaratie) {
        if (declaraties==null) return
        declaraties.add(declaratie)
    }

    fun removeDeclaratie(uuid: String) {
        if (declaraties==null) return
        val declaratiesClone = declaraties
        for (declaratie in declaratiesClone) {
            if (declaratie.uuid == uuid) {
                this.declaraties.remove(declaratie)
            }
        }
    }

    fun addAfschrift(afschrift: Afschrift) {
        afschriften.add(afschrift)
    }

    fun removeAfschrift(nummer: String) {
        val afschriftenClone = afschriften
        for (afschrift in afschriftenClone) {
            if (afschrift.nummer == nummer) {
                this.afschriften.remove(afschrift)
            }
        }
    }

    fun addBoeking(boeking: Boeking) {
        boekingen.add(boeking)
    }

    fun removeBoeking(uuid: String) {
        val boekingenClone = boekingen
        for (boeking in boekingenClone) {
            if (boeking.uuid == uuid) {
                this.boekingen.remove(boeking)
            }
        }
    }

}
