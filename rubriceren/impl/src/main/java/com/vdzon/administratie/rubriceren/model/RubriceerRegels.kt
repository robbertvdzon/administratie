package com.vdzon.administratie.rubriceren.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties
class RubriceerRegels (var rubriceerRegelList: List<RubriceerRegel>)
