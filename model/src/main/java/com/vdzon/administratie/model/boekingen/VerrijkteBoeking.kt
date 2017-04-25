package com.vdzon.administratie.model.boekingen


import com.vdzon.administratie.model.Afschrift
import com.vdzon.administratie.model.Factuur
import com.vdzon.administratie.model.Rekening

import java.math.BigDecimal
import java.time.LocalDate

class VerrijkteBoeking (
        val boeking: Boeking,
        val factuur: Factuur?,
        val rekening: Rekening?,
        val afschrift: Afschrift?,
        val boekingsType: BOEKINGSTYPE,
        val boekingsBedrag: BigDecimal,
        val afschriftBedrag: BigDecimal?,
        val factuurBedrag: BigDecimal?,
        val rekeningBedrag: BigDecimal?,
        val afschriftDate: LocalDate?,
        val factuurDate: LocalDate?,
        val rekeningDate: LocalDate?
){

    enum class BOEKINGSTYPE {
        UNKNOWN, BETAALDE_FACTUUR, BETAALDE_REKENING, BETALING_ZONDER_FACTUUR, INKOMSTEN_ZONDER_FACTUUR, PRIVE_BETALING
    }

}
