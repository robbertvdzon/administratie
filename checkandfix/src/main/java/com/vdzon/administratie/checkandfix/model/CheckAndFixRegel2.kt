package com.vdzon.administratie.checkandfix.model

import com.vdzon.administratie.dto.AfschriftDto
import java.time.LocalDate

class CheckAndFixRegel2(val rubriceerAction: FixAction? = null,
                        val omschrijving: String? = null,
                        val data: String? = null,
                        val boekingUuid: String? = null,
                        val checkType: CheckType? = null,
                        val afschrift: AfschriftDto? = null,
                        val date: LocalDate? = null);
