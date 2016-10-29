package com.vdzon.administratie.checkandfix.actions.check

import java.util.Collection

import com.vdzon.administratie.checkandfix.CheckAndFixData
import com.vdzon.administratie.checkandfix.model.CheckType.WARNING
import com.vdzon.administratie.checkandfix.model.{CheckAndFixRegel, FixAction}
import com.vdzon.administratie.model.Afschrift.newBuilder
import com.vdzon.administratie.model.{Rekening, Factuur}
import com.vdzon.administratie.model.boekingen.relaties.BoekingMetAfschrift

import scala.collection.JavaConversions._
import scala.compat.java8.StreamConverters._

class BedragenCheck {
  @AdministratieCheckRule
  def checkOfFacturenVolledigBetaaldZijn(data: CheckAndFixData): Collection[_ <: CheckAndFixRegel] = data
    .alleFacturen
    .stream
    .toScala[Stream]
    .filter(f => factuurNietVolledigBetaald(f, data))
    .map(f => buildNietVolledigBetaaldCheckAndFixRegel(f))

  @AdministratieCheckRule
  def checkOfRekeningenVolledigBetaaldZijn(data: CheckAndFixData): Collection[_ <: CheckAndFixRegel] = data
    .alleRekeningen
    .stream
    .toScala[Stream]
    .filter(r => rekeningNietVolledigBetaald(r, data))
    .map(r => buildNietVolledigBetaaldCheckAndFixRegel(r))

  private def buildNietVolledigBetaaldCheckAndFixRegel(f: Factuur): CheckAndFixRegel = {
    CheckAndFixRegel
      .newBuilder()
      .checkType(WARNING)
      .data("")
      .date(f.getFactuurDate)
      .omschrijving("Factuur " + f.getFactuurNummer + " is niet volledig betaald of geboekt")
      .rubriceerAction(FixAction.NONE).build()
  }

  private def buildNietVolledigBetaaldCheckAndFixRegel(r: Rekening): CheckAndFixRegel = {
    CheckAndFixRegel
      .newBuilder()
      .checkType(WARNING)
      .data("")
      .date(r.getRekeningDate)
      .omschrijving("Rekening " + r.getRekeningNummer + " is niet volledig betaald of geboekt")
      .rubriceerAction(FixAction.NONE).build()
  }

  private def factuurNietVolledigBetaald(factuur: Factuur, data: CheckAndFixData): Boolean = {
    val sumVanAfschriften: Double = data.boekingenCache.getBoekingenVanFactuur(factuur.getFactuurNummer).stream.mapToDouble(boeking => getAfschriftBedrag(data, boeking)).sum
    return sumVanAfschriften != factuur.getBedragIncBtw
  }

  private def rekeningNietVolledigBetaald(rekening: Rekening, data: CheckAndFixData): Boolean = {
    val sumVanAfschriften: Double = data.boekingenCache.getBoekingenVanRekening(rekening.getRekeningNummer).stream.mapToDouble(boeking => getAfschriftBedrag(data, boeking)).sum
    return sumVanAfschriften != -1*rekening.getBedragIncBtw
  }

  private def getAfschriftBedrag(data: CheckAndFixData, boeking: BoekingMetAfschrift): Double = {
    return data.alleAfschriften.stream.filter(afschrift => afschrift.getNummer().equals(boeking.getAfschriftNummer())).findFirst.orElse(newBuilder.bedrag(0).build).getBedrag
  }

}
