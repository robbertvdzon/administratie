package com.vdzon.administratie.checkandfix.actions.check

import com.vdzon.administratie.checkandfix.CheckAndFixData
import com.vdzon.administratie.checkandfix.CheckAndFixRegel
import com.vdzon.administratie.checkandfix.model.CheckType
import com.vdzon.administratie.checkandfix.model.FixAction
import com.vdzon.administratie.model.boekingen.relaties.BoekingMetAfschrift
import com.vdzon.administratie.model.boekingen.relaties.BoekingMetFactuur
import com.vdzon.administratie.model.boekingen.relaties.BoekingMetRekening


object BestaanCheck {
    fun checkOfAfschriftNogBestaat(data: CheckAndFixData): List<CheckAndFixRegel> =
            data.alleBoekingen
                    .filter { boeking ->
                        if (boeking is BoekingMetAfschrift) true else false
                    }
                    .map { boeking -> boeking as BoekingMetAfschrift }
                    .filter { boeking -> !afschriftExists(boeking.afschriftNummer, data) }
                    .map {
                        boeking ->
                        CheckAndFixRegel(
                                rubriceerAction = FixAction.REMOVE_BOEKING,
                                checkType = CheckType.FIX_NEEDED,
                                boekingUuid = boeking.uuid,
                                omschrijving = "Afschift " + boeking.afschriftNummer + " bestaat niet meer terwijl er wel een boeking van bestaat")
                    }


    fun checkOfRekeningNogBestaat(data: CheckAndFixData): List<CheckAndFixRegel> =
            data.alleBoekingen
                    .filter { boeking -> if (boeking is BoekingMetRekening) true else false }
                    .map { boeking -> boeking as BoekingMetRekening }
                    .filter { boeking -> !rekeningExists(boeking.rekeningNummer, data) }
                    .map {
                        boeking ->
                        CheckAndFixRegel(
                                rubriceerAction = FixAction.REMOVE_BOEKING,
                                checkType = CheckType.FIX_NEEDED,
                                boekingUuid = boeking.uuid,
                                omschrijving = "Rekening " + boeking.rekeningNummer + " bestaat niet meer terwijl er wel een boeking van bestaat")
                    }

    fun checkOfFactuurNogBestaat(data: CheckAndFixData): List<CheckAndFixRegel> =
            data.alleBoekingen
                    .filter { boeking -> if (boeking is BoekingMetFactuur) true else false }
                    .map { boeking -> boeking as BoekingMetFactuur }
                    .filter { boeking -> !factuurExists(boeking.factuurNummer, data) }
                    .map { boeking ->
                        CheckAndFixRegel(
                                rubriceerAction = FixAction.REMOVE_BOEKING,
                                checkType = CheckType.FIX_NEEDED,
                                boekingUuid = boeking.uuid,
                                omschrijving = "Factuur " + boeking.factuurNummer + " bestaat niet meer terwijl er wel een boeking van bestaat")
                    }

    private fun afschriftExists(afschriftNummer: String, data: CheckAndFixData): Boolean =
            data.alleAfschriften.filter { afschrift -> afschrift.nummer.equals(afschriftNummer) }.size > 0

    private fun rekeningExists(rekeningNummer: String, data: CheckAndFixData): Boolean =
            data.alleRekeningen.filter { rekening -> rekening.rekeningNummer.equals(rekeningNummer) }.size > 0

    private fun factuurExists(factuurNummer: String, data: CheckAndFixData): Boolean =
            data.alleFacturen.filter { factuur -> factuur.factuurNummer.equals(factuurNummer) }.size > 0


}
