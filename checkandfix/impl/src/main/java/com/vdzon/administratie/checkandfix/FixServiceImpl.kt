package com.vdzon.administratie.checkandfix

import com.vdzon.administratie.checkandfix.actions.fix.BoekingenFix
import com.vdzon.administratie.model.Administratie
import java.util.*
import kotlin.reflect.jvm.internal.impl.javax.inject.Inject

class FixServiceImpl : FixService {

    @Inject
    lateinit internal var checkService: CheckService

    val fixAfschriftCheck = { regel: CheckAndFixRegel, administratie: Administratie -> BoekingenFix.removeBoekingen(regel, administratie) }
    val fixFunctions = Arrays.asList(fixAfschriftCheck)

    override fun getFixedAdministratie(administratie: Administratie): Administratie {
        val regelsToFix = checkService.getCheckAndFixRegels(administratie)
        var newAdministratie = administratie
        regelsToFix.forEach { regel -> fixFunctions.forEach { f -> newAdministratie = f(regel, newAdministratie) } }
        return newAdministratie
    }
}
