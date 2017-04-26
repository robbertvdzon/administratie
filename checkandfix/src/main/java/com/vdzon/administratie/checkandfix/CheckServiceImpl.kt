package com.vdzon.administratie.checkandfix

import com.vdzon.administratie.checkandfix.actions.check.BedragenCheck2
import com.vdzon.administratie.checkandfix.actions.check.BestaanCheck2
import com.vdzon.administratie.checkandfix.actions.check.DubbeleNummersCheck2
import com.vdzon.administratie.checkandfix.model.CheckAndFixRegel2
import com.vdzon.administratie.model.Administratie
import com.vdzon.administratie.model.BoekingenCache
import java.util.*

class CheckServiceImpl : CheckService{

    override fun getCheckAndFixRegels(administratie: Administratie): List<CheckAndFixRegel2> {
        val checkAndFixData = populateCheckAndFixData(administratie)
        val regels = ArrayList<CheckAndFixRegel2>()
        checkFunctions.forEach { f -> regels.addAll(f(checkAndFixData)) }
        return regels
    }

    private val checkOfRekeningenVolledigBetaaldZijn = { data: CheckAndFixData2 -> BedragenCheck2.checkOfRekeningenVolledigBetaaldZijn(data) }


    private val checkOfFacturenVolledigBetaaldZijn = { data: CheckAndFixData2 -> BedragenCheck2.checkOfFacturenVolledigBetaaldZijn(data) }
    private val checkOfAfschriftNogBestaat = { data: CheckAndFixData2 -> BestaanCheck2.checkOfAfschriftNogBestaat(data) }
    private val checkOfFactuurNogBestaat = { data: CheckAndFixData2 -> BestaanCheck2.checkOfFactuurNogBestaat(data) }

    private val checkOfRekeningNogBestaat = { data: CheckAndFixData2 -> BestaanCheck2.checkOfRekeningNogBestaat(data) }
    private val checkAfschriftenMetHetzelfdeNummer = { data: CheckAndFixData2 -> DubbeleNummersCheck2.checkAfschriftenMetHetzelfdeNummer(data) }
    private val checkFacturenMetDezelfdeFactuurNummer = { data: CheckAndFixData2 -> DubbeleNummersCheck2.checkFacturenMetDezelfdeFactuurNummer(data) }

    private val checkRekeningenMetHetzelfdeRekeningNummer = { data: CheckAndFixData2 -> DubbeleNummersCheck2.checkRekeningenMetHetzelfdeRekeningNummer(data) }

    private val checkFunctions = Arrays.asList<(CheckAndFixData2) -> List<CheckAndFixRegel2>>(
            checkOfFacturenVolledigBetaaldZijn,
            checkOfRekeningenVolledigBetaaldZijn,
            checkOfAfschriftNogBestaat,
            checkOfFactuurNogBestaat,
            checkOfRekeningNogBestaat,
            checkAfschriftenMetHetzelfdeNummer,
            checkFacturenMetDezelfdeFactuurNummer,
            checkRekeningenMetHetzelfdeRekeningNummer
    )

    private fun populateCheckAndFixData(administratie: Administratie): CheckAndFixData2 {
        return CheckAndFixData2(
                administratie.afschriften,
                administratie.rekeningen,
                administratie.facturen,
                administratie.boekingen,
                BoekingenCache(administratie.boekingen))

    }

}
