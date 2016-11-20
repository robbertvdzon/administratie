package com.vdzon.administratie.model

import java.time.LocalDate

import org.bson.types.ObjectId
import org.mongodb.morphia.annotations.{Entity, Id}

import scala.annotation.meta.field

@Entity("rekening") case class Rekening(
                                         @(Id @field) uuid: String,
                                         rekeningNummer: String = null,
                                         factuurNummer: String = null,
                                         naam: String = null,
                                         omschrijving: String = null,
                                         rekeningDate: LocalDate = null,
                                         bedragExBtw: Double = 0,
                                         bedragIncBtw: Double = 0,
                                         btw: Double = 0,
                                         maandenAfschrijving: Int = 0
                                       ) {

  def this() = this(null)


}