package com.vdzon.administratie.checkandfix.actions.fix

import com.vdzon.administratie.checkandfix.model.CheckAndFixRegel2
import com.vdzon.administratie.checkandfix.model.FixAction
import com.vdzon.administratie.model.Administratie
import com.vdzon.administratie.model.boekingen.Boeking

object BoekingenFix2 {

    fun removeBoekingen(regel: CheckAndFixRegel2, administratie: Administratie): Administratie {
        if (regel.rubriceerAction != FixAction.REMOVE_BOEKING) return administratie
        val nieuweBoekingen: List<Boeking> = administratie.boekingen.toMutableList().filter { boeking -> boeking.uuid != regel.boekingUuid }
        return Administratie.newBuilder(administratie).boekingen(nieuweBoekingen).build()
    }
}