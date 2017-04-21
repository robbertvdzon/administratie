package com.vdzon.administratie.rubriceren.rubriceerRegels

import com.vdzon.administratie.model.Gebruiker

import java.util.Comparator

open class RubriceerHelper {
    companion object {
        internal fun findNextRekeningNummer(gebruiker: Gebruiker): Int {
            return 1 + (gebruiker.defaultAdministratie.rekeningen.map({ rekening -> Integer.parseInt(rekening.rekeningNummer) }).maxWith(Comparator.naturalOrder<Int>())?:1000)
        }
    }
}
