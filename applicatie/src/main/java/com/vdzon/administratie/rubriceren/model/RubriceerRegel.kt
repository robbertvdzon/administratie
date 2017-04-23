package com.vdzon.administratie.rubriceren.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.vdzon.administratie.dto.AfschriftDto

@JsonIgnoreProperties
class RubriceerRegel (
    var rubriceerAction: RubriceerAction,
    var rekeningNummer: String?,
    var faktuurNummer: String?,
    var afschrift: AfschriftDto)

