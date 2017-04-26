package com.vdzon.administratie.checkandfix.actions.fix

import com.vdzon.administratie.checkandfix.CheckAndFixRegel
import com.vdzon.administratie.checkandfix.model.FixAction
import com.vdzon.administratie.model.Administratie
import com.vdzon.administratie.model.boekingen.Boeking

object BoekingenFix {

    fun removeBoekingen(regel: CheckAndFixRegel, administratie: Administratie): Administratie {
        if (regel.rubriceerAction != FixAction.REMOVE_BOEKING) return administratie
        val nieuweBoekingen: List<Boeking> = administratie.boekingen.toMutableList().filter { boeking -> boeking.uuid != regel.boekingUuid }
        return administratie.copy(boekingen = nieuweBoekingen.toMutableList())
    }
}
