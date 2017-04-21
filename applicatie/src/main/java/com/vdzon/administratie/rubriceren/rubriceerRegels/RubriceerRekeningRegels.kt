package com.vdzon.administratie.rubriceren.rubriceerRegels

import com.vdzon.administratie.dto.AfschriftDto
import com.vdzon.administratie.model.Afschrift
import com.vdzon.administratie.model.BoekingenCache
import com.vdzon.administratie.model.Gebruiker
import com.vdzon.administratie.model.Rekening
import com.vdzon.administratie.model.boekingen.BetaaldeRekeningBoeking
import com.vdzon.administratie.model.boekingen.BetalingZonderFactuurBoeking
import com.vdzon.administratie.model.boekingen.Boeking
import com.vdzon.administratie.model.boekingen.relaties.BoekingMetAfschrift
import com.vdzon.administratie.rubriceren.model.RubriceerAction
import com.vdzon.administratie.rubriceren.model.RubriceerRegel

import java.math.BigDecimal
import java.util.UUID

class RubriceerRekeningRegels : RubriceerHelper() {

    //TODO: deze class kan nog steeds mooier

    @RubriceerRule
    fun updateRegels(gebruiker: Gebruiker, regels: MutableList<RubriceerRegel>, afschrift: Afschrift, boekingenCache: BoekingenCache) {
        val boekingenVanAfschrift = boekingenCache.getBoekingenVanAfschrift(afschrift.nummer)
        if (hasNoBoekingen(boekingenVanAfschrift)) {
            if (afschrift.bedrag.toDouble() < 0) {
                var rubriceerAction = RubriceerAction.BETALING_ZONDER_FACTUUR
                val factuurNummer: String? = null
                var rekeningNummer: String? = null
                for (rekening in gebruiker.defaultAdministratie.rekeningen) {
                    if (boekingenCache.getBoekingenVanRekening(rekening.rekeningNummer).isEmpty()
                            &&
                            !rekeningAlreadyUsed(regels, rekening.rekeningNummer)
                            &&
                            rekening.bedragIncBtw.compareTo(afschrift.bedrag.negate()) == 0
                            &&
                            (afschrift.omschrijving.contains(rekening.rekeningNummer) || afschrift.omschrijving == rekening.omschrijving)) {
                        rubriceerAction = RubriceerAction.CONNECT_EXISTING_REKENING
                        rekeningNummer = rekening.rekeningNummer
                    }
                }
                val rubriceerRegel = RubriceerRegel(
                        rubriceerAction=rubriceerAction,
                        rekeningNummer=rekeningNummer,
                        faktuurNummer=factuurNummer,
                        afschrift=AfschriftDto.toDto(afschrift, boekingenCache))

                regels.add(rubriceerRegel)
            }
        }
    }

    private fun rekeningAlreadyUsed(regels: List<RubriceerRegel>, rekeningNummer: String): Boolean {
        return regels.filter{ regel -> rekeningNummer == regel.rekeningNummer }.count() != 0
    }

    private fun hasNoBoekingen(boekingenVanAfschrift: List<BoekingMetAfschrift>?): Boolean {
        return boekingenVanAfschrift == null || boekingenVanAfschrift.isEmpty()
    }

    @RubriceerRuleCommit
    fun processRegel(regel: RubriceerRegel, gebruiker: Gebruiker) {
        val afschrift = regel.afschrift.toAfschrift()
        val boeking: Boeking
        when (regel.rubriceerAction) {
            RubriceerAction.BETALING_ZONDER_FACTUUR -> {
                boeking = BetalingZonderFactuurBoeking(
                        regel.afschrift.nummer,
                        UUID.randomUUID().toString())
                gebruiker.defaultAdministratie.addBoeking(boeking)
            }
            RubriceerAction.CONNECT_EXISTING_REKENING -> {
                boeking = BetaaldeRekeningBoeking(
                        regel.rekeningNummer?:"",
                        regel.afschrift.nummer,
                        UUID.randomUUID().toString())
                gebruiker.defaultAdministratie.addBoeking(boeking)
            }
            RubriceerAction.CREATE_REKENING -> {
                val rekening = Rekening(
                        UUID.randomUUID().toString(),
                        "" + RubriceerHelper.findNextRekeningNummer(gebruiker),
                        "",
                        afschrift.relatienaam,
                        afschrift.omschrijving,
                        afschrift.boekdatum,
                        afschrift.bedrag.negate(),
                        afschrift.bedrag.negate(),
                        BigDecimal.ZERO,
                        0)
                gebruiker.defaultAdministratie.addRekening(rekening)

                boeking = BetaaldeRekeningBoeking(
                        rekening.rekeningNummer,
                        regel.afschrift.nummer,
                        UUID.randomUUID().toString())
                gebruiker.defaultAdministratie.addBoeking(boeking)
            }
        }
    }

}
