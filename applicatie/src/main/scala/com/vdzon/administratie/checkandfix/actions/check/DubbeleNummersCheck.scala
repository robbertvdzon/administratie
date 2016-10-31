package com.vdzon.administratie.checkandfix.actions.check

import com.vdzon.administratie.checkandfix.CheckAndFixData
import com.vdzon.administratie.checkandfix.model.{CheckAndFixRegel, CheckType, FixAction}

object DubbeleNummersCheck {

  def checkFacturenMetDezelfdeFactuurNummer(data: CheckAndFixData) = data.alleFacturen
      .groupBy(f => f.getFactuurNummer)
      .filter(_._2.size > 1)
      .map(tuple => CheckAndFixRegel.newBuilder
        .rubriceerAction(FixAction.NONE)
        .checkType(CheckType.WARNING)
        .omschrijving("Factuur " + tuple._1 + " bestaat " + tuple._2.size + " keer")
        .build)

  def checkRekeningenMetHetzelfdeRekeningNummer(data: CheckAndFixData) = data.alleRekeningen
      .groupBy(r => r.getRekeningNummer)
      .filter(_._2.size > 1)
      .map(tuple => CheckAndFixRegel.newBuilder
        .rubriceerAction(FixAction.NONE)
        .checkType(CheckType.WARNING)
        .omschrijving("Rekening " + tuple._1 + " bestaat " + tuple._2.size + " keer")
        .build)

  def checkAfschriftenMetHetzelfdeNummer(data: CheckAndFixData) = data.alleAfschriften
      .groupBy(a => a.getNummer)
      .filter(_._2.size > 1)
      .map(tuple => CheckAndFixRegel.newBuilder
        .rubriceerAction(FixAction.NONE)
        .checkType(CheckType.WARNING)
        .omschrijving("Afschrift " + tuple._1 + " bestaat " + tuple._2.size + " keer")
        .build)

}
