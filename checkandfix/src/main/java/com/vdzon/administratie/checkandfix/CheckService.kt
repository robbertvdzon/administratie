package com.vdzon.administratie.checkandfix

import com.vdzon.administratie.checkandfix.model.CheckAndFixRegel2
import com.vdzon.administratie.model.Administratie

interface CheckService {
    fun getCheckAndFixRegels(administratie: Administratie): List<CheckAndFixRegel2>
}
