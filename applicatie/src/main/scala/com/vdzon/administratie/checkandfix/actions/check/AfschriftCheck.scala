package com.vdzon.administratie.checkandfix.actions.check

import java.util.Collection
import java.util.stream.Collectors
import collection.JavaConversions._

import com.vdzon.administratie.checkandfix.CheckAndFixData
import com.vdzon.administratie.checkandfix.model.{FixAction, CheckType, CheckAndFixRegel}
import com.vdzon.administratie.model.boekingen.relaties.BoekingMetAfschrift
import com.vdzon.administratie.model.{Afschrift, Factuur}
import scala.compat.java8.StreamConverters._

class AfschriftCheck {
//  @AdministratieCheckRule
//  def checkOfAfschriftNogBestaat(data: CheckAndFixData): Collection[_ <: CheckAndFixRegel] = {
//    return data.alleFacturen
//          .stream.toScala[Stream].map(f=>checkFactuur(f,data)).map(f=>new CheckAndFixRegel(
//      FixAction.NONE,
//      "Factuur is niet volledig betaald of geboekt",
//      "",
//      "",
//      CheckType.WARNING,
//      null,
//      null
//    ));
//  }
////
//  private def checkFactuur(factuur: Factuur, data: CheckAndFixData): Boolean = {
//    val sumVanAfschriften: Double = data.boekingenCache.getBoekingenVanFactuur(factuur.getFactuurNummer).stream.mapToDouble(boeking => getAfschriftBedrag(data, boeking)).sum
//    return sumVanAfschriften != factuur.getBedragIncBtw
//  }
//
//  private def getAfschriftBedrag(data: CheckAndFixData, boeking: BoekingMetAfschrift): Double = {
//    return data.alleAfschriften.stream.filter(afschrift => afschrift.getNummer().equals(boeking.getAfschriftNummer())).findFirst.orElse(Afschrift.builder.bedrag(0).build).getBedrag
//  }

}
