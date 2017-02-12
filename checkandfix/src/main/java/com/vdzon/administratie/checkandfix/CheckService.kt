package com.vdzon.administratie.checkandfix

import com.vdzon.administratie.checkandfix.actions.check.BedragenCheck2
import com.vdzon.administratie.checkandfix.model.CheckAndFixRegel
import com.vdzon.administratie.model.Administratie
import com.vdzon.administratie.model.BoekingenCache

import java.util.ArrayList
import java.util.Arrays
import java.util.function.Function

/**
 * Created by robbe on 2/12/2017.
 */
object CheckService {

    private val checkOfRekeningenVolledigBetaaldZijn = { data : CheckAndFixData2 -> BedragenCheck2.checkOfRekeningenVolledigBetaaldZijn(data) }

    private val checkFunctions = Arrays.asList<(CheckAndFixData2) -> List<CheckAndFixRegel>>(
            //            checkOfFacturenVolledigBetaaldZijn,
            checkOfRekeningenVolledigBetaaldZijn
            //            checkOfAfschriftNogBestaat,
            //            checkOfFactuurNogBestaat,
            //            checkOfRekeningNogBestaat,
            //            checkAfschriftenMetHetzelfdeNummer,
            //            checkFacturenMetDezelfdeFactuurNummer,
            //            checkRekeningenMetHetzelfdeRekeningNummer
    )

    fun getCheckAndFixRegels(administratie: Administratie): List<CheckAndFixRegel> {
        val checkAndFixData = populateCheckAndFixData(administratie)
        val regels = ArrayList<CheckAndFixRegel>()
        checkFunctions.forEach { f ->
            //                f(checkAndFixData);
            //            CheckAndFixRegel apply = f.apply(checkAndFixData);
            regels.addAll(f(checkAndFixData))
        }
        return regels
    }

    private fun populateCheckAndFixData(administratie: Administratie): CheckAndFixData2 {
        return CheckAndFixData2(
                administratie.afschriften,
                administratie.rekeningen,
                administratie.facturen,
                administratie.boekingen,
                BoekingenCache(administratie.boekingen))

    }

}
