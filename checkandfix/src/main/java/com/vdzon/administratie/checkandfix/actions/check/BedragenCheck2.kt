package com.vdzon.administratie.checkandfix.actions.check


import com.vdzon.administratie.checkandfix.CheckAndFixData2
import com.vdzon.administratie.checkandfix.model.CheckAndFixRegel2
import com.vdzon.administratie.checkandfix.model.CheckType
import com.vdzon.administratie.checkandfix.model.FixAction
import com.vdzon.administratie.model.Afschrift
import com.vdzon.administratie.model.Factuur
import com.vdzon.administratie.model.Rekening
import com.vdzon.administratie.model.boekingen.relaties.BoekingMetAfschrift
import java.math.BigDecimal

object BedragenCheck2 {


    fun rekeningNietVolledigBetaald(rekening: Rekening, data: CheckAndFixData2): Boolean {
        val sumVanAfschriften: Double = data.boekingenCache.getBoekingenVanRekening(rekening.rekeningNummer).map { getAfschriftBedrag(data, it) }.sum()
        return sumVanAfschriften != -1 * rekening.bedragIncBtw.toDouble()
    }

    fun checkOfRekeningenVolledigBetaaldZijn(data: CheckAndFixData2): List<CheckAndFixRegel2> =
            data
                    .alleRekeningen
                    .filter { (rekeningNietVolledigBetaald(it, data)) }
                    .map { factuur -> buildNietVolledigBetaaldCheckAndFixRegel(factuur) };


    fun buildNietVolledigBetaaldCheckAndFixRegel(f: Factuur): CheckAndFixRegel2 =
            CheckAndFixRegel2(
                    checkType = CheckType.WARNING,
                    data = "",
                    date = f.factuurDate,
                    omschrijving = "Factuur " + f.factuurNummer + " is niet volledig betaald of geboekt",
                    rubriceerAction = FixAction.NONE)

    fun buildNietVolledigBetaaldCheckAndFixRegel(r: Rekening): CheckAndFixRegel2 =
            CheckAndFixRegel2(
                    checkType = CheckType.WARNING,
                    data = "",
                    date = r.rekeningDate,
                    omschrijving = "Rekening " + r.rekeningNummer + " is niet volledig betaald of geboekt",
                    rubriceerAction = FixAction.NONE)


    fun getAfschriftBedrag(data: CheckAndFixData2, boeking: BoekingMetAfschrift): Double {
        val afschrift = data.alleAfschriften
                .find { it.nummer.equals(boeking.afschriftNummer) }
        if (afschrift != null) {
            return afschrift.bedrag.toDouble()
        } else {
            return BigDecimal.ZERO.toDouble()
        }
    }

    fun factuurNietVolledigBetaald(factuur: Factuur, data: CheckAndFixData2): Boolean  {
        val sumVanAfschriften: Double = data.boekingenCache.getBoekingenVanFactuur(factuur.factuurNummer).map{boeking -> getAfschriftBedrag(data, boeking)}.sum()
        return sumVanAfschriften.toDouble() != factuur.bedragIncBtw.toDouble()
    }

    fun checkOfFacturenVolledigBetaaldZijn(data: CheckAndFixData2): List<CheckAndFixRegel2> = data
            .alleFacturen
            .filter{f -> factuurNietVolledigBetaald(f, data)}
            .map{f -> buildNietVolledigBetaaldCheckAndFixRegel(f)}

}



