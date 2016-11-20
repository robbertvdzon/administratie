package com.vdzon.administratie.model

import java.time.LocalDate

import org.mongodb.morphia.annotations.{Entity, Id}

import scala.IllegalArgumentException
import scala.annotation.meta.field

@Entity("factuur") case class Factuur(
                                       @(Id@field) val uuid: String = null,
                                       val factuurNummer: String = null,
                                       val gekoppeldeBestellingNummer: String = null,
                                       val factuurDate: LocalDate = null,
                                       val contact: Contact = null,
                                       val factuurRegels: java.util.List[FactuurRegel] = null
                                       ) {

  var bedragExBtwVal:Double = 0  // deze 3 values worden berekend in de calculate methode
  var bedragIncBtwVal:Double = 0
  var btwVal:Double = 0
  calculate()

  def bedragExBtw() = bedragExBtwVal

  def bedragIncBtw() = bedragIncBtwVal

  def btw() = btwVal

  def this() = this(uuid = null)


  def calculate(): Unit = {
    if (factuurRegels != null) {
      import scala.collection.JavaConversions._
      for (factuurRegel <- factuurRegels) {
        val regelBedragEx: Double = factuurRegel.getStuksPrijs * factuurRegel.getAantal
        val regelBedragBtw: Double = round(regelBedragEx * (factuurRegel.getBtwPercentage / 100), 2)
        val regelBedragInc: Double = regelBedragEx + regelBedragBtw
        bedragExBtwVal += regelBedragEx
        btwVal += regelBedragBtw
        bedragIncBtwVal += regelBedragInc
      }
    }
  }

  def round(value: Double, places: Int): Double = {
    var value2 = value;
    if (places < 0) throw new IllegalArgumentException
    var factor: Long = Math.pow(10, places).toLong
    value2 = value2 * factor
    val tmp: Long = value2.round
    return tmp.toDouble / factor
  }


}
