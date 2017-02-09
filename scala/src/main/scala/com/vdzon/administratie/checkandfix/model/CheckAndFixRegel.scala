package com.vdzon.administratie.checkandfix.model

import java.time.LocalDate

import com.vdzon.administratie.dto.AfschriftDto

case class CheckAndFixRegel (val rubriceerAction: FixAction = null,
  val omschrijving: String = null,
  val data: String = null,
  val boekingUuid: String = null,
  val checkType: CheckType = null,
  val afschrift: AfschriftDto = null,
  val date: LocalDate = null);

