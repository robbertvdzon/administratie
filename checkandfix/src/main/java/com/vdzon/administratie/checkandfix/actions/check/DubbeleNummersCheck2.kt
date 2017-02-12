package com.vdzon.administratie.checkandfix.actions.check


import com.vdzon.administratie.checkandfix.CheckAndFixData
import com.vdzon.administratie.model.Afschrift
import com.vdzon.administratie.model.Factuur
import com.vdzon.administratie.model.Rekening

object DubbeleNummersCheck2 {

    fun checkFacturenMetDezelfdeFactuurNummer(data: CheckAndFixData): List<Factuur>? {
        return null
    }


    fun checkAfschriftenMetHetzelfdeNummer(data: CheckAndFixData): List<Afschrift>? {
        return null
    }

    fun checkRekeningenMetHetzelfdeRekeningNummer(data: CheckAndFixData): List<Rekening>? {
        return null
    }

}
