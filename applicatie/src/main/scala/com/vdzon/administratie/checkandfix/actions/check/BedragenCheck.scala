package com.vdzon.administratie.checkandfix.actions.check

import java.math.BigDecimal

import com.vdzon.administratie.checkandfix.CheckAndFixData
import com.vdzon.administratie.checkandfix.model.CheckType.WARNING
import com.vdzon.administratie.checkandfix.model.{CheckAndFixRegel, FixAction}
import com.vdzon.administratie.model.{Afschrift, Rekening, Factuur}
import com.vdzon.administratie.model.boekingen.relaties.BoekingMetAfschrift

object BedragenCheck {

  def checkOfFacturenVolledigBetaaldZijn(data: CheckAndFixData) = data
    .alleFacturen
    .filter(f => factuurNietVolledigBetaald(f, data))
    .map(f => buildNietVolledigBetaaldCheckAndFixRegel(f))

  def checkOfRekeningenVolledigBetaaldZijn(data: CheckAndFixData) = data
    .alleRekeningen
    .filter(r => rekeningNietVolledigBetaald(r, data))
    .map(r => buildNietVolledigBetaaldCheckAndFixRegel(r))

  private def buildNietVolledigBetaaldCheckAndFixRegel(f: Factuur): CheckAndFixRegel = {
    CheckAndFixRegel(
      checkType=WARNING,
      data="",
      date=f.getFactuurDate,
      omschrijving="Factuur " + f.getFactuurNummer + " is niet volledig betaald of geboekt",
      rubriceerAction=FixAction.NONE)
  }

  private def buildNietVolledigBetaaldCheckAndFixRegel(r: Rekening): CheckAndFixRegel = {
    CheckAndFixRegel(
      checkType = WARNING,
      data = "",
      date = r.getRekeningDate,
      omschrijving = "Rekening " + r.getRekeningNummer + " is niet volledig betaald of geboekt",
      rubriceerAction = FixAction.NONE)
  }

  private def factuurNietVolledigBetaald(factuur: Factuur, data: CheckAndFixData): Boolean = {
    val sumVanAfschriften: Double = data.boekingenCache.getBoekingenVanFactuur(factuur.getFactuurNummer).stream.mapToDouble(boeking => getAfschriftBedrag(data, boeking)).sum
    return sumVanAfschriften.toDouble != factuur.getBedragIncBtw.doubleValue()
  }

  private def rekeningNietVolledigBetaald(rekening: Rekening, data: CheckAndFixData): Boolean = {
    val sumVanAfschriften: Double = data.boekingenCache.getBoekingenVanRekening(rekening.getRekeningNummer).stream.mapToDouble(boeking => getAfschriftBedrag(data, boeking)).sum
    return sumVanAfschriften != -1*rekening.getBedragIncBtw.doubleValue()
  }

  private def getAfschriftBedrag(data: CheckAndFixData, boeking: BoekingMetAfschrift): Double = data.alleAfschriften
      .find(afschrift => afschrift.getNummer().equals(boeking.getAfschriftNummer()))
      .getOrElse(Afschrift.newBuilder().bedrag(BigDecimal.ZERO).build()).getBedrag.doubleValue()

}
