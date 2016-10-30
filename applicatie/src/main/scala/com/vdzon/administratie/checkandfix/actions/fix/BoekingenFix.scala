package com.vdzon.administratie.checkandfix.actions.fix

import com.vdzon.administratie.checkandfix.model.{CheckAndFixRegel, FixAction}
import com.vdzon.administratie.model.Administratie

import scala.collection.JavaConversions._
import scala.compat.java8.StreamConverters._

object BoekingenFix {

  def fixAfschrift(regel: CheckAndFixRegel, administratie: Administratie): Administratie = {
  if (regel.getRubriceerAction ne FixAction.REMOVE_BOEKING) return administratie
  val nieuweBoekingen = administratie.getBoekingen
    .stream
    .toScala[Stream]
      .filter(boeking => boeking.getUuid.ne(regel.getBoekingUuid))

  return Administratie.newBuilder(administratie).boekingen(nieuweBoekingen).build
}


}
