package com.vdzon.administratie.checkandfix

import com.vdzon.administratie.model.Afschrift
import com.vdzon.administratie.model.BoekingenCache
import com.vdzon.administratie.model.Factuur
import com.vdzon.administratie.model.Rekening
import com.vdzon.administratie.model.boekingen.Boeking

data class CheckAndFixData(
        val alleAfschriften: List<Afschrift>,
        val alleRekeningen: List<Rekening>,
        val alleFacturen: List<Factuur>,
        val alleBoekingen: List<Boeking>,
        val boekingenCache: BoekingenCache)