package com.vdzon.administratie.checkandfix.actions.check

import java.util.Collection
import java.util.stream.Collectors

import com.vdzon.administratie.checkandfix.CheckAndFixData
import com.vdzon.administratie.checkandfix.model.CheckType.WARNING
import com.vdzon.administratie.checkandfix.model.{CheckType, CheckAndFixRegel, FixAction}
import com.vdzon.administratie.model.Afschrift.newBuilder
import com.vdzon.administratie.model.boekingen.relaties.{BoekingMetFactuur, BoekingMetRekening, BoekingMetAfschrift}
import com.vdzon.administratie.model.{Factuur, Rekening}

import scala.collection.JavaConversions._
import scala.compat.java8.StreamConverters._

object BestaanCheck {

  def checkOfAfschriftNogBestaat(data: CheckAndFixData): Collection[_ <: CheckAndFixRegel] =
    data.alleBoekingen
      .stream
      .toScala[Stream]
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

  def checkOfRekeningNogBestaat(data: CheckAndFixData): Collection[_ <: CheckAndFixRegel] =
    data.alleBoekingen
      .stream
      .toScala[Stream]
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

  def checkOfFactuurNogBestaat(data: CheckAndFixData): Collection[_ <: CheckAndFixRegel] =
    data.alleBoekingen
      .stream
      .toScala[Stream]
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
    return data.alleAfschriften.stream.filter(afschrift => afschrift.getNummer().equals(afschriftNummer)).count > 0
  }

  private def rekeningExists(rekeningNummer: String, data: CheckAndFixData): Boolean = {
    return data.alleRekeningen.stream.filter(rekening => rekening.getRekeningNummer().equals(rekeningNummer)).count > 0
  }

  private def factuurExists(factuurNummer: String, data: CheckAndFixData): Boolean = {
    return data.alleFacturen.stream.filter(factuur => factuur.getFactuurNummer().equals(factuurNummer)).count > 0
  }
}
