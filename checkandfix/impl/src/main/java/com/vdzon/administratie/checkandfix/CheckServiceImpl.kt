package com.vdzon.administratie.checkandfix

import com.vdzon.administratie.checkandfix.actions.check.BedragenCheck
import com.vdzon.administratie.checkandfix.actions.check.BestaanCheck
import com.vdzon.administratie.checkandfix.actions.check.DubbeleNummersCheck
import com.vdzon.administratie.model.Administratie
import com.vdzon.administratie.model.BoekingenCache
import java.util.*

class CheckServiceImpl : CheckService {

    override fun getCheckAndFixRegels(administratie: Administratie): List<CheckAndFixRegel> {
        val checkAndFixData = populateCheckAndFixData(administratie)
        val regels = ArrayList<CheckAndFixRegel>()
        checkFunctions.forEach { f -> regels.addAll(f(checkAndFixData)) }
        return regels
    }

    private val checkOfRekeningenVolledigBetaaldZijn = { data: CheckAndFixData -> BedragenCheck.checkOfRekeningenVolledigBetaaldZijn(data) }
    private val checkOfFacturenVolledigBetaaldZijn = { data: CheckAndFixData -> BedragenCheck.checkOfFacturenVolledigBetaaldZijn(data) }
    private val checkOfAfschriftNogBestaat = { data: CheckAndFixData -> BestaanCheck.checkOfAfschriftNogBestaat(data) }
    private val checkOfFactuurNogBestaat = { data: CheckAndFixData -> BestaanCheck.checkOfFactuurNogBestaat(data) }
    private val checkOfRekeningNogBestaat = { data: CheckAndFixData -> BestaanCheck.checkOfRekeningNogBestaat(data) }
    private val checkAfschriftenMetHetzelfdeNummer = { data: CheckAndFixData -> DubbeleNummersCheck.checkAfschriftenMetHetzelfdeNummer(data) }
    private val checkFacturenMetDezelfdeFactuurNummer = { data: CheckAndFixData -> DubbeleNummersCheck.checkFacturenMetDezelfdeFactuurNummer(data) }
    private val checkRekeningenMetHetzelfdeRekeningNummer = { data: CheckAndFixData -> DubbeleNummersCheck.checkRekeningenMetHetzelfdeRekeningNummer(data) }

    private val checkFunctions = Arrays.asList<(CheckAndFixData) -> List<CheckAndFixRegel>>(
            checkOfFacturenVolledigBetaaldZijn,
            checkOfRekeningenVolledigBetaaldZijn,
            checkOfAfschriftNogBestaat,
            checkOfFactuurNogBestaat,
            checkOfRekeningNogBestaat,
            checkAfschriftenMetHetzelfdeNummer,
            checkFacturenMetDezelfdeFactuurNummer,
            checkRekeningenMetHetzelfdeRekeningNummer
    )

    private fun populateCheckAndFixData(administratie: Administratie): CheckAndFixData {
        return CheckAndFixData(
                administratie.afschriften,
                administratie.rekeningen,
                administratie.facturen,
                administratie.boekingen,
                BoekingenCache(administratie.boekingen))

    }

}
