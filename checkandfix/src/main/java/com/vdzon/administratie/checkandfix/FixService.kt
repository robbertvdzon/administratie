package com.vdzon.administratie.checkandfix

import com.vdzon.administratie.checkandfix.actions.fix.BoekingenFix2
import com.vdzon.administratie.checkandfix.model.CheckAndFixRegel2
import com.vdzon.administratie.model.Administratie
import java.util.*

/**
 * Created by robbe on 2/12/2017.
 */
object FixService {

    val fixAfschriftCheck = { regel: CheckAndFixRegel2, administratie: Administratie -> BoekingenFix2.removeBoekingen(regel,administratie)}
    val fixFunctions = Arrays.asList(fixAfschriftCheck)

    fun getFixedAdministratie(administratie: Administratie): Administratie {
        val regelsToFix = CheckService.getCheckAndFixRegels(administratie)
        var newAdministratie = administratie
        regelsToFix.forEach{regel -> fixFunctions.forEach{f -> newAdministratie = f(regel, newAdministratie)}}
        return newAdministratie
    }
}
