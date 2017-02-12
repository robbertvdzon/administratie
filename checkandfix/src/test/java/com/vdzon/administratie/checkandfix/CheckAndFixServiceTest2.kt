package com.vdzon.administratie.checkandfix

//import org.junit.jupiter.api.Test

import com.vdzon.administratie.model.*
import com.vdzon.administratie.model.boekingen.BetaaldeFactuurBoeking
import com.vdzon.administratie.model.boekingen.Boeking
import junit.framework.Assert.assertEquals
import org.junit.Test
import java.util.*

internal class CheckAndFixServiceTest2 {
    @Test
    fun when_alle_facturen_betaald_then_geen_checkregels() {
        val administratie = Administratie.newBuilder()
                .afschriften(Arrays.asList(buildAfschrift("a1", 100.0), buildAfschrift("a2", 101.0)))
                .facturen(Arrays.asList(buildFactuur("f1", 100.0, 0.0), buildFactuur("f2", 100.0, 0.0)))
                .rekeningen(ArrayList<Rekening>())
                .boekingen(Arrays.asList(factuurBoeking("b1", "f1", "a1"), factuurBoeking("b2", "f2", "a2")) as List<Boeking>?).build()

        val regels = CheckService.getCheckAndFixRegels(administratie)

        assertEquals(1, regels.size)
    }

    private fun factuurBoeking(uuid: String, factuurNr: String, afschriftNummer: String): BetaaldeFactuurBoeking {
        return BetaaldeFactuurBoeking.newBuilder()
                .uuid(uuid)
                .afschriftNummer(afschriftNummer)
                .factuurNummer(factuurNr)
                .build()
    }

    private fun buildFactuur(nummer: String, ex: Double, perc: Double): Factuur {
        return Factuur.newBuilder()
                .bedragExBtw(java.math.BigDecimal.valueOf(ex))
                .factuurNummer(nummer)
                .factuurRegels(Arrays.asList(FactuurRegel.newBuilder().aantal(java.math.BigDecimal.ONE).stuksPrijs(java.math.BigDecimal.valueOf(ex)).btwPercentage(java.math.BigDecimal.valueOf(perc)).build()))
                .build()
    }

    private fun buildAfschrift(nummer: String, bedrag: Double): Afschrift {
        return Afschrift.newBuilder()
                .nummer(nummer)
                .bedrag(java.math.BigDecimal.valueOf(bedrag))
                .build()
    }

    private fun buildRekening(nummer: String, inc: Double, ex: Double, btw: Double): Rekening {
        return Rekening.newBuilder()
                .bedragExBtw(java.math.BigDecimal.valueOf(ex))
                .bedragIncBtw(java.math.BigDecimal.valueOf(inc))
                .btw(java.math.BigDecimal.valueOf(btw))
                .rekeningNummer(nummer)
                .build()
    }

}