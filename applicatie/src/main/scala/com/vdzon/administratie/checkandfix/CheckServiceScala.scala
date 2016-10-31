package com.vdzon.administratie.checkandfix

import com.vdzon.administratie.checkandfix.actions.check.{BestaanCheck, DubbeleNummersCheck, BedragenCheck}
import com.vdzon.administratie.checkandfix.model.CheckAndFixRegel
import com.vdzon.administratie.model.{Afschrift, Rekening, BoekingenCache, Administratie}

import scala.collection.JavaConversions
import scala.collection.JavaConversions._
import scala.compat.java8.StreamConverters._

object CheckServiceScala {
  val checkOfFacturenVolledigBetaaldZijn = (data: CheckAndFixData) => BedragenCheck.checkOfFacturenVolledigBetaaldZijn(data)
  val checkOfRekeningenVolledigBetaaldZijn = (data: CheckAndFixData) => BedragenCheck.checkOfRekeningenVolledigBetaaldZijn(data)


  val checkOfAfschriftNogBestaat = (data: CheckAndFixData) => BestaanCheck.checkOfAfschriftNogBestaat(data)
  val checkOfFactuurNogBestaat = (data: CheckAndFixData) => BestaanCheck.checkOfFactuurNogBestaat(data)
  val checkOfRekeningNogBestaat = (data: CheckAndFixData) => BestaanCheck.checkOfRekeningNogBestaat(data)

  val checkAfschriftenMetHetzelfdeNummer = (data: CheckAndFixData) => DubbeleNummersCheck.checkAfschriftenMetHetzelfdeNummer(data)
  val checkFacturenMetDezelfdeFactuurNummer = (data: CheckAndFixData) => DubbeleNummersCheck.checkFacturenMetDezelfdeFactuurNummer(data)
  val checkRekeningenMetHetzelfdeRekeningNummer = (data: CheckAndFixData) => DubbeleNummersCheck.checkRekeningenMetHetzelfdeRekeningNummer(data)

  val checkFunctions = List(
    checkOfFacturenVolledigBetaaldZijn,
    checkOfRekeningenVolledigBetaaldZijn,
    checkOfAfschriftNogBestaat,
    checkOfFactuurNogBestaat,
    checkOfRekeningNogBestaat,
    checkAfschriftenMetHetzelfdeNummer,
    checkFacturenMetDezelfdeFactuurNummer,
    checkRekeningenMetHetzelfdeRekeningNummer
  )

  def getCheckAndFixRegels(administratie: Administratie):java.util.List[CheckAndFixRegel] = {
    val checkAndFixData: CheckAndFixData = populateCheckAndFixData(administratie)
    var regels = List[CheckAndFixRegel]()
    checkFunctions.stream.forEach(f => regels = regels ++ f(checkAndFixData))
    return regels
  }

  private def populateCheckAndFixData(administratie: Administratie): CheckAndFixData = {
    new CheckAndFixData(
      JavaConversions.asScalaBuffer(administratie.getAfschriften).toList,
      JavaConversions.asScalaBuffer(administratie.getRekeningen).toList,
      JavaConversions.asScalaBuffer(administratie.getFacturen).toList,
      JavaConversions.asScalaBuffer(administratie.getBoekingen).toList,
      new BoekingenCache(administratie.getBoekingen)
    )
  }

}
