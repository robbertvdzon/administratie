package com.vdzon.administratie.rubriceren.model

import com.vdzon.administratie.dto.AfschriftDto

class RubriceerRegel (
    val rubriceerAction: RubriceerAction,
    val rekeningNummer: String?,
    val faktuurNummer: String?,
    val afschrift: AfschriftDto)

