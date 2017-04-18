package com.vdzon.administratie.checkandfix.actions.check

import com.vdzon.administratie.checkandfix.CheckAndFixData2
import com.vdzon.administratie.checkandfix.model.CheckAndFixRegel2
import com.vdzon.administratie.checkandfix.model.CheckType
import com.vdzon.administratie.checkandfix.model.FixAction
import com.vdzon.administratie.model.boekingen.relaties.BoekingMetAfschrift
import com.vdzon.administratie.model.boekingen.relaties.BoekingMetFactuur
import com.vdzon.administratie.model.boekingen.relaties.BoekingMetRekening


object BestaanCheck2 {
    fun checkOfAfschriftNogBestaat(data: CheckAndFixData2): List<CheckAndFixRegel2> =
            data.alleBoekingen
                    .filter { boeking ->
                        if (boeking is BoekingMetAfschrift) true else false
                    }
                    .map { boeking -> boeking as BoekingMetAfschrift }
                    .filter { boeking -> !afschriftExists(boeking.afschriftNummer, data) }
                    .map {
                        boeking ->
                        CheckAndFixRegel2(
                                rubriceerAction = FixAction.REMOVE_BOEKING,
                                checkType = CheckType.FIX_NEEDED,
                                boekingUuid = boeking.uuid,
                                omschrijving = "Afschift " + boeking.afschriftNummer + " bestaat niet meer terwijl er wel een boeking van bestaat")
                    }


    fun checkOfRekeningNogBestaat(data: CheckAndFixData2): List<CheckAndFixRegel2> =
            data.alleBoekingen
                    .filter { boeking -> if (boeking is BoekingMetRekening) true else false }
                    .map { boeking -> boeking as BoekingMetRekening }
                    .filter { boeking -> !rekeningExists(boeking.rekeningNummer, data) }
                    .map {
                        boeking ->
                        CheckAndFixRegel2(
                                rubriceerAction = FixAction.REMOVE_BOEKING,
                                checkType = CheckType.FIX_NEEDED,
                                boekingUuid = boeking.uuid,
                                omschrijving = "Rekening " + boeking.rekeningNummer + " bestaat niet meer terwijl er wel een boeking van bestaat")
                    }

    fun checkOfFactuurNogBestaat(data: CheckAndFixData2): List<CheckAndFixRegel2> =
            data.alleBoekingen
                    .filter { boeking -> if (boeking is BoekingMetFactuur) true else false }
                    .map { boeking -> boeking as BoekingMetFactuur }
                    .filter { boeking -> !factuurExists(boeking.factuurNummer, data) }
                    .map { boeking ->
                        CheckAndFixRegel2(
                                rubriceerAction = FixAction.REMOVE_BOEKING,
                                checkType = CheckType.FIX_NEEDED,
                                boekingUuid = boeking.uuid,
                                omschrijving = "Factuur " + boeking.factuurNummer + " bestaat niet meer terwijl er wel een boeking van bestaat")
                    }

    private fun afschriftExists(afschriftNummer: String, data: CheckAndFixData2): Boolean =
            data.alleAfschriften.filter { afschrift -> afschrift.nummer.equals(afschriftNummer) }.size > 0

    private fun rekeningExists(rekeningNummer: String, data: CheckAndFixData2): Boolean =
            data.alleRekeningen.filter { rekening -> rekening.rekeningNummer.equals(rekeningNummer) }.size > 0

    private fun factuurExists(factuurNummer: String, data: CheckAndFixData2): Boolean =
            data.alleFacturen.filter { factuur -> factuur.factuurNummer.equals(factuurNummer) }.size > 0


}
