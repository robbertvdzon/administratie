package com.vdzon.administratie.checkandfix

import com.vdzon.administratie.checkandfix.actions.fix.BoekingenFix
import com.vdzon.administratie.checkandfix.model.CheckAndFixRegel
import com.vdzon.administratie.model.Administratie
import scala.collection.JavaConversions._
import scala.compat.java8.StreamConverters._

object FixServiceScala {
  val fixAfschriftCheck = (regel:CheckAndFixRegel,administratie:Administratie) => BoekingenFix.removeBoekingen(regel,administratie)
  val fixFunctions = List(fixAfschriftCheck)

  def getFixedAdministratie(administratie: Administratie): Administratie = {
    val regelsToFix = CheckServiceScala.getCheckAndFixRegels(administratie)
    var newAdministratie = administratie
    regelsToFix.forEach(regel => fixFunctions.forEach(f => newAdministratie = f(regel, newAdministratie)))
    return newAdministratie
  }

}
