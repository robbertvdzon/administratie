package com.vdzon.administratie.checkandfix.actions.check

import com.vdzon.administratie.checkandfix.CheckAndFixData2
import com.vdzon.administratie.checkandfix.model.CheckAndFixRegel2
import com.vdzon.administratie.checkandfix.model.CheckType
import com.vdzon.administratie.checkandfix.model.FixAction


object DubbeleNummersCheck2 {

    fun checkFacturenMetDezelfdeFactuurNummer(data: CheckAndFixData2): List<CheckAndFixRegel2> = data.alleFacturen
            .groupBy { f -> f.factuurNummer }
            .filter { group -> group.value.size > 1 }
            .map {
                group ->
                CheckAndFixRegel2(
                        rubriceerAction = FixAction.NONE,
                        checkType = CheckType.WARNING,
                        omschrijving = "Factuur " + group.key + " bestaat " + group.value.size + " keer"
                )
            }


    fun checkRekeningenMetHetzelfdeRekeningNummer(data: CheckAndFixData2) = data.alleRekeningen
            .groupBy { r -> r.rekeningNummer }
            .filter { group -> group.value.size > 1 }
            .map {
                group ->
                CheckAndFixRegel2(
                        rubriceerAction = FixAction.NONE,
                        checkType = CheckType.WARNING,
                        omschrijving = "Rekening " + group.key + " bestaat " + group.value.size + " keer")
            }

    fun checkAfschriftenMetHetzelfdeNummer(data: CheckAndFixData2) = data.alleAfschriften
            .groupBy { a -> a.nummer }
            .filter { group -> group.value.size > 1 }
            .map {
                group ->
                CheckAndFixRegel2(
                        rubriceerAction = FixAction.NONE,
                        checkType = CheckType.WARNING,
                        omschrijving = "Afschrift " + group.key + " bestaat " + group.value.size + " keer")
            }


}
