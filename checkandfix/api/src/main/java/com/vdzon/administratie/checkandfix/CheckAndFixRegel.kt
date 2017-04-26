package com.vdzon.administratie.checkandfix

import com.vdzon.administratie.checkandfix.model.CheckType
import com.vdzon.administratie.checkandfix.model.FixAction
import com.vdzon.administratie.dto.AfschriftDto
import java.time.LocalDate

class CheckAndFixRegel(val rubriceerAction: FixAction? = null,
                       val omschrijving: String? = null,
                       val data: String? = null,
                       val boekingUuid: String? = null,
                       val checkType: CheckType? = null,
                       val afschrift: AfschriftDto? = null,
                       val date: LocalDate? = null);
