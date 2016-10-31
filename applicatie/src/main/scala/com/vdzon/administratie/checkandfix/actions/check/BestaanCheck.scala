package com.vdzon.administratie.checkandfix.actions.check

import com.vdzon.administratie.checkandfix.CheckAndFixData
import com.vdzon.administratie.checkandfix.model.{CheckType, CheckAndFixRegel, FixAction}
import com.vdzon.administratie.model.boekingen.relaties.{BoekingMetFactuur, BoekingMetRekening, BoekingMetAfschrift}

object BestaanCheck {

  def checkOfAfschriftNogBestaat(data: CheckAndFixData) =
    data.alleBoekingen
      .filter(boeking => boeking match {
        case b:BoekingMetAfschrift => true
        case _ => false
      })
      .map(boeking => boeking.asInstanceOf[BoekingMetAfschrift])
      .filter(boeking => !afschriftExists(boeking.getAfschriftNummer, data))
      .map(boeking => CheckAndFixRegel.newBuilder
        .rubriceerAction(FixAction.REMOVE_BOEKING)
        .checkType(CheckType.FIX_NEEDED)
        .boekingUuid(boeking.getUuid)
        .omschrijving("Afschift " + boeking.getAfschriftNummer + " bestaat niet meer terwijl er wel een boeking van bestaat")
        .build)

  def checkOfRekeningNogBestaat(data: CheckAndFixData) =
    data.alleBoekingen
      .filter(boeking => boeking match {
        case b:BoekingMetRekening => true
        case _ => false
      })
      .map(boeking => boeking.asInstanceOf[BoekingMetRekening])
      .filter(boeking => !rekeningExists(boeking.getRekeningNummer, data))
      .map(boeking => CheckAndFixRegel.newBuilder
        .rubriceerAction(FixAction.REMOVE_BOEKING)
        .checkType(CheckType.FIX_NEEDED)
        .boekingUuid(boeking.getUuid)
        .omschrijving("Rekening " + boeking.getRekeningNummer + " bestaat niet meer terwijl er wel een boeking van bestaat")
        .build)

  def checkOfFactuurNogBestaat(data: CheckAndFixData) =
    data.alleBoekingen
      .filter(boeking => boeking match {
        case b:BoekingMetFactuur => true
        case _ => false
      })
      .map(boeking => boeking.asInstanceOf[BoekingMetFactuur])
      .filter(boeking => !factuurExists(boeking.getFactuurNummer, data))
      .map(boeking => CheckAndFixRegel.newBuilder
        .rubriceerAction(FixAction.REMOVE_BOEKING)
        .checkType(CheckType.FIX_NEEDED)
        .boekingUuid(boeking.getUuid)
        .omschrijving("Factuur " + boeking.getFactuurNummer + " bestaat niet meer terwijl er wel een boeking van bestaat")
        .build)

  private def afschriftExists(afschriftNummer: String, data: CheckAndFixData): Boolean = {
    return data.alleAfschriften.filter(afschrift => afschrift.getNummer().equals(afschriftNummer)).size > 0
  }

  private def rekeningExists(rekeningNummer: String, data: CheckAndFixData): Boolean = {
    return data.alleRekeningen.filter(rekening => rekening.getRekeningNummer().equals(rekeningNummer)).size > 0
  }

  private def factuurExists(factuurNummer: String, data: CheckAndFixData): Boolean = {
    return data.alleFacturen.filter(factuur => factuur.getFactuurNummer().equals(factuurNummer)).size > 0
  }
}
