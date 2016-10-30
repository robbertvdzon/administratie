package com.vdzon.administratie.checkandfix.actions.check

import java.util.Collection

import com.vdzon.administratie.checkandfix.CheckAndFixData
import com.vdzon.administratie.checkandfix.model.{CheckAndFixRegel, CheckType, FixAction}

import scala.collection.JavaConversions._
import scala.compat.java8.StreamConverters._

class DubbeleNummersCheck {

  @AdministratieCheckRule
  def checkFacturenMetDezelfdeFactuurNummer(data: CheckAndFixData): Collection[_ <: CheckAndFixRegel] = {
    val facturenNummersGroup = data.alleFacturen
      .stream
      .toScala[Stream]
      .groupBy(f => f.getFactuurNummer)

    return facturenNummersGroup.toStream
      .filter(_._2.size > 1)
      .map(tuple => CheckAndFixRegel.newBuilder
        .rubriceerAction(FixAction.NONE)
        .checkType(CheckType.WARNING)
        .omschrijving("Factuur " + tuple._1 + " bestaat " + tuple._2.size + " keer")
        .build)
  }

  @AdministratieCheckRule
  def checkRekeningenMetHetzelfdeRekeningNummer(data: CheckAndFixData): Collection[_ <: CheckAndFixRegel] = {
    val rekeningNummersGroup = data.alleRekeningen
      .stream
      .toScala[Stream]
      .groupBy(r => r.getRekeningNummer)

    return rekeningNummersGroup.toStream
      .filter(_._2.size > 1)
      .map(tuple => CheckAndFixRegel.newBuilder
        .rubriceerAction(FixAction.NONE)
        .checkType(CheckType.WARNING)
        .omschrijving("Rekening " + tuple._1 + " bestaat " + tuple._2.size + " keer")
        .build)
  }

  @AdministratieCheckRule
  def checkAfschriftenMetHetzelfdeNummer(data: CheckAndFixData): Collection[_ <: CheckAndFixRegel] = {
    val afschriftenNummersGroup = data.alleAfschriften
      .stream
      .toScala[Stream]
      .groupBy(a => a.getNummer)

    return afschriftenNummersGroup.toStream
      .filter(_._2.size > 1)
      .map(tuple => CheckAndFixRegel.newBuilder
        .rubriceerAction(FixAction.NONE)
        .checkType(CheckType.WARNING)
        .omschrijving("Afschrift " + tuple._1 + " bestaat " + tuple._2.size + " keer")
        .build)
  }

}
