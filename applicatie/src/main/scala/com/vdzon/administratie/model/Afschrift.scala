package com.vdzon.administratie.model

import java.time.LocalDate

import org.mongodb.morphia.annotations.{Entity, Id}

import scala.annotation.meta.field

@Entity("afschrift") case class Afschrift(
                                           @(Id @field) val uuid: String = null,
                                           val nummer: String = null,
                                           val rekening: String = null,
                                           val omschrijving: String = null,
                                           val relatienaam: String = null,
                                           val boekdatum: LocalDate = null,
                                           val bedrag: Double = .0) {

  def this() = this(uuid = null)

}
