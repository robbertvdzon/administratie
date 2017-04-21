package com.vdzon.administratie.rubriceren.rubriceerRegels

import com.vdzon.administratie.dto.AfschriftDto
import com.vdzon.administratie.model.Afschrift
import com.vdzon.administratie.model.BoekingenCache
import com.vdzon.administratie.model.Factuur
import com.vdzon.administratie.model.Gebruiker
import com.vdzon.administratie.model.boekingen.BetaaldeFactuurBoeking
import com.vdzon.administratie.model.boekingen.Boeking
import com.vdzon.administratie.model.boekingen.InkomstenZonderFactuurBoeking
import com.vdzon.administratie.model.boekingen.relaties.BoekingMetAfschrift
import com.vdzon.administratie.rubriceren.model.RubriceerAction
import com.vdzon.administratie.rubriceren.model.RubriceerRegel
import java.util.UUID

class RubriceerFactuurRegels : RubriceerHelper() {

    //TODO: deze class kan nog steeds mooier

    @RubriceerRule
    fun updateRegels(gebruiker: Gebruiker, regels: MutableList<RubriceerRegel>, afschrift: Afschrift, boekingenCache: BoekingenCache) {
        val boekingenVanAfschrift = boekingenCache.getBoekingenVanAfschrift(afschrift.nummer)
        if (boekingenVanAfschrift == null || boekingenVanAfschrift.isEmpty()) {
            if (afschrift.bedrag.toDouble() > 0) {
                var rubriceerAction = RubriceerAction.INKOMSTEN_ZONDER_FACTUUR
                var factuurNummer: String? = null
                for (factuur in gebruiker.defaultAdministratie.facturen) {
                    val omschrijvingZonderSpaties = afschrift.omschrijving.replace(" ".toRegex(), "")
                    if (factuur.bedragIncBtw.compareTo(afschrift.bedrag) == 0 && omschrijvingZonderSpaties.contains(factuur.factuurNummer)) {
                        rubriceerAction = RubriceerAction.CONNECT_EXISTING_FACTUUR
                        factuurNummer = factuur.factuurNummer
                    }
                }
                val rubriceerRegel = RubriceerRegel(rubriceerAction=rubriceerAction,rekeningNummer="",faktuurNummer=factuurNummer,
                        afschrift=AfschriftDto.toDto(afschrift, boekingenCache))
                regels.add(rubriceerRegel)
            }
        }
    }


    @RubriceerRuleCommit
    fun processRegel(regel: RubriceerRegel, gebruiker: Gebruiker) {
        val afschrift = regel.afschrift.toAfschrift()
        val boeking: Boeking
        when (regel.rubriceerAction) {
            RubriceerAction.CONNECT_EXISTING_FACTUUR -> {
                boeking = BetaaldeFactuurBoeking(
                        regel.faktuurNummer?:"",
                        regel.afschrift.nummer,
                        UUID.randomUUID().toString()
                )
                gebruiker.defaultAdministratie.addBoeking(boeking)
            }
            RubriceerAction.INKOMSTEN_ZONDER_FACTUUR -> {
                boeking = InkomstenZonderFactuurBoeking(
                        regel.afschrift.nummer,
                        UUID.randomUUID().toString()
                )
                gebruiker.defaultAdministratie.addBoeking(boeking)
            }
        }
    }


}
