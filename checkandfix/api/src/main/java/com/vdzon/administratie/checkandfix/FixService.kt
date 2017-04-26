package com.vdzon.administratie.checkandfix

import com.vdzon.administratie.model.Administratie

interface FixService {
    fun getFixedAdministratie(administratie: Administratie): Administratie
}
