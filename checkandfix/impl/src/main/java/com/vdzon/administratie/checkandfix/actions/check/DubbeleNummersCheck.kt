package com.vdzon.administratie.checkandfix.actions.check

import com.vdzon.administratie.checkandfix.CheckAndFixData
import com.vdzon.administratie.checkandfix.CheckAndFixRegel
import com.vdzon.administratie.checkandfix.model.CheckType
import com.vdzon.administratie.checkandfix.model.FixAction


object DubbeleNummersCheck {

    fun checkFacturenMetDezelfdeFactuurNummer(data: CheckAndFixData): List<CheckAndFixRegel> = data.alleFacturen
            .groupBy { f -> f.factuurNummer }
            .filter { group -> group.value.size > 1 }
            .map {
                group ->
                CheckAndFixRegel(
                        rubriceerAction = FixAction.NONE,
                        checkType = CheckType.WARNING,
                        omschrijving = "Factuur " + group.key + " bestaat " + group.value.size + " keer"
                )
            }


    fun checkRekeningenMetHetzelfdeRekeningNummer(data: CheckAndFixData) = data.alleRekeningen
            .groupBy { r -> r.rekeningNummer }
            .filter { group -> group.value.size > 1 }
            .map {
                group ->
                CheckAndFixRegel(
                        rubriceerAction = FixAction.NONE,
                        checkType = CheckType.WARNING,
                        omschrijving = "Rekening " + group.key + " bestaat " + group.value.size + " keer")
            }

    fun checkAfschriftenMetHetzelfdeNummer(data: CheckAndFixData) = data.alleAfschriften
            .groupBy { a -> a.nummer }
            .filter { group -> group.value.size > 1 }
            .map {
                group ->
                CheckAndFixRegel(
                        rubriceerAction = FixAction.NONE,
                        checkType = CheckType.WARNING,
                        omschrijving = "Afschrift " + group.key + " bestaat " + group.value.size + " keer")
            }


}
