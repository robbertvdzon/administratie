package com.vdzon.administratie.checkandfix

import com.vdzon.administratie.checkandfix.model.CheckType
import com.vdzon.administratie.checkandfix.model.FixAction
import com.vdzon.administratie.model.*
import com.vdzon.administratie.model.boekingen.BetaaldeFactuurBoeking
import com.vdzon.administratie.model.boekingen.BetaaldeRekeningBoeking
import com.vdzon.administratie.model.boekingen.Boeking
import junit.framework.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.*

internal class CheckAndFixServiceTest2 {
    @Test
    fun when_alle_facturen_betaald_then_geen_checkregels() {
        val administratie = Administratie.newBuilder()
                .afschriften(Arrays.asList(buildAfschrift("a1", 100.0), buildAfschrift("a2", 101.0)))
                .facturen(Arrays.asList(buildFactuur("f1", 100.0, 0.0), buildFactuur("f2", 100.0, 0.0)))
                .rekeningen(Arrays.asList<Rekening>())
                .boekingen(Arrays.asList(factuurBoeking("b1", "f1", "a1"), factuurBoeking("b2", "f2", "a2")) as List<Boeking>?).build()

        val regels = CheckService.getCheckAndFixRegels(administratie)
        regels.forEach { f -> println(f.omschrijving) };

        assertEquals(1, regels.size)
    }

    @Test
    fun when_factuur_niet_volledig_geboekt_then_return_checkError() {
        val administratie = Administratie.newBuilder()
                .afschriften(Arrays.asList(buildAfschrift("a1", 100.0), buildAfschrift("a2", 100.0), buildAfschrift("a3", -100.0)))
                .facturen(Arrays.asList(buildFactuur("f1", 100.0, 0.0), buildFactuur("f2", 100.0, 0.0)))
                .rekeningen(Arrays.asList(buildRekening("r1", 100.0, 80.0, 20.0)))
                .boekingen(Arrays.asList(factuurBoeking("b1", "f1", "a1"), factuurBoeking("b2", "f2", "a2"), rekeningBoeking("b3", "r1", "a3"))).build()

        val regels = CheckService.getCheckAndFixRegels(administratie)
        regels.forEach { f -> println(f.omschrijving) };
        assertTrue("", regels.size == 0)
    }



    @Test
    fun when_boeking_heeft_nonexisting_factuur_then_return_checkError() {
        val administratie = Administratie.newBuilder()
                .afschriften(Arrays.asList(buildAfschrift("a1", 100.0), buildAfschrift("a2", 100.0)))
                .facturen(Arrays.asList(buildFactuur("f1", 100.0, 0.0)))
                .rekeningen(Arrays.asList<Rekening>())
                .boekingen(Arrays.asList(factuurBoeking("b1", "f1", "a1"), factuurBoeking("b2", "f2", "a2")) as MutableList<Boeking>?).build()

        val regels = CheckService.getCheckAndFixRegels(administratie)
        regels.forEach { f -> println(f.omschrijving) };
        assertTrue("", regels.size == 1)
        assertTrue("", regels[0].rubriceerAction == FixAction.REMOVE_BOEKING)
        assertTrue("", regels[0].checkType == CheckType.FIX_NEEDED)
        assertTrue("", regels[0].boekingUuid.equals("b2"))
    }

    @Test
    fun when_boeking_heeft_nonexisting_factuur_then_boeking_removed_after_fix() {
        val administratie = Administratie.newBuilder()
                .afschriften(Arrays.asList(buildAfschrift("a1", 100.0), buildAfschrift("a2", 100.0)))
                .facturen(Arrays.asList(buildFactuur("f1", 100.0, 0.0)))
                .rekeningen(Arrays.asList<Rekening>())
                .boekingen(Arrays.asList(factuurBoeking("b1", "f1", "a1"), factuurBoeking("b2", "f2", "a2")) as MutableList<Boeking> ).build()

        val fixedAdministratie = FixService.getFixedAdministratie(administratie);
        assertEquals(fixedAdministratie.boekingen.size,1)
        assertEquals(fixedAdministratie.boekingen[0].uuid, "b1")
    }

    @Test
    fun when_boeking_heeft_nonexisting_rekening_then_return_checkError() {
        val administratie = Administratie.newBuilder()
                .afschriften(Arrays.asList(buildAfschrift("a1", 100.0)))
                .facturen(Arrays.asList<Factuur>())
                .rekeningen(Arrays.asList<Rekening>())
                .boekingen(Arrays.asList(rekeningBoeking("b1", "r1", "a1")) as MutableList<Boeking>?).build()

        val regels = CheckService.getCheckAndFixRegels(administratie);
        regels.forEach { f -> println(f.omschrijving) };
        assertTrue("", regels.size == 1)
        assertTrue("", regels[0].rubriceerAction == FixAction.REMOVE_BOEKING)
        assertTrue("", regels[0].checkType == CheckType.FIX_NEEDED)
        assertTrue("", regels[0].boekingUuid.equals("b1"))
    }

    @Test
    fun when_boeking_heeft_nonexisting_afschrift_then_return_checkError() {
        val administratie = Administratie.newBuilder()
                .afschriften(Arrays.asList(buildAfschrift("a1", 100.0)))
                .facturen(Arrays.asList(buildFactuur("f1", 100.0, 0.0), buildFactuur("f2", 0.0, 0.0)))
                .rekeningen(Arrays.asList<Rekening>())
                .boekingen(Arrays.asList(factuurBoeking("b1", "f1", "a1"), factuurBoeking("b2", "f2", "a2")) as MutableList<Boeking>?).build()

        val regels = CheckService.getCheckAndFixRegels(administratie);
        regels.forEach { f -> println(f.omschrijving) };
        assertTrue("", regels.size == 1)
        assertTrue("", regels[0].rubriceerAction == FixAction.REMOVE_BOEKING)
        assertTrue("", regels[0].checkType == CheckType.FIX_NEEDED)
        assertTrue("", regels[0].boekingUuid.equals("b2"))
    }

    @Test
    fun when_rekening_niet_volledig_betaald_then_return_checkError() {
        val administratie = Administratie.newBuilder()
                .afschriften(Arrays.asList(buildAfschrift("a1", 100.0)))
                .facturen(Arrays.asList<Factuur>())
                .rekeningen(Arrays.asList(buildRekening("r1", 100.0, 80.0, 20.0)))
                .boekingen(Arrays.asList<Boeking>())
                .build()

        val regels = CheckService.getCheckAndFixRegels(administratie);
        regels.forEach { f -> println(f.omschrijving) };
        assertTrue("", regels.size == 1)
        assertTrue("", regels[0].rubriceerAction == FixAction.NONE)
        assertTrue("", regels[0].checkType == CheckType.WARNING)
    }

    @Test
    fun when_one_rekening_then_return_geen_errors() {
        val administratie = Administratie.newBuilder()
                .afschriften(Arrays.asList<Afschrift>())
                .facturen(Arrays.asList<Factuur>())
                .rekeningen(Arrays.asList(buildRekening("r1", 0.0, 0.0, 0.0)))
                .boekingen(Arrays.asList<Boeking>())
                .build()

        val regels = CheckService.getCheckAndFixRegels(administratie);
        regels.forEach { f -> println(f.omschrijving) };
        assertTrue("", regels.size == 0)
    }

    @Test
    fun when_one_factuur_then_return_geen_errors() {
        val administratie = Administratie.newBuilder()
                .afschriften(Arrays.asList<Afschrift>())
                .facturen(Arrays.asList(buildFactuur("f1", 0.0, 0.0)))
                .rekeningen(Arrays.asList<Rekening>())
                .boekingen(Arrays.asList<Boeking>())
                .build()

        val regels = CheckService.getCheckAndFixRegels(administratie);
        regels.forEach { f -> println(f.omschrijving) };
        assertTrue("", regels.size == 0)
    }

    @Test
    fun when_dubbele_rekeningen_then_return_een_error() {
        val administratie = Administratie.newBuilder()
                .afschriften(Arrays.asList<Afschrift>())
                .facturen(Arrays.asList<Factuur>())
                .rekeningen(Arrays.asList(buildRekening("r1", 0.0, 0.0, 0.0), buildRekening("r1", 0.0, 0.0, 0.0)))
                .boekingen(Arrays.asList<Boeking>())
                .build()

        val regels = CheckService.getCheckAndFixRegels(administratie);
        regels.forEach { f -> println(f.omschrijving) };
        assertTrue("", regels.size == 1)
        assertTrue("", regels[0].rubriceerAction == FixAction.NONE)
        assertTrue("", regels[0].checkType == CheckType.WARNING)
    }

    @Test
    fun when_dubbele_facturen_then_return_een_error() {
        val administratie = Administratie.newBuilder()
                .afschriften(Arrays.asList<Afschrift>())
                .facturen(Arrays.asList(buildFactuur("f1", 0.0, 0.0), buildFactuur("f1", 0.0, 0.0)))
                .rekeningen(Arrays.asList<Rekening>())
                .boekingen(Arrays.asList<Boeking>())
                .build()

        val regels = CheckService.getCheckAndFixRegels(administratie);
        regels.forEach { f -> println(f.omschrijving) };
        assertTrue("", regels.size == 1)
        assertTrue("", regels[0].rubriceerAction == FixAction.NONE)
        assertTrue("", regels[0].checkType == CheckType.WARNING)
    }

    @Test
    fun when_dubbele_afschriften_then_return_een_error() {
        val administratie = Administratie.newBuilder()
                .afschriften(Arrays.asList(buildAfschrift("a1", 0.0), buildAfschrift("a1", 0.0)))
                .facturen(Arrays.asList<Factuur>())
                .rekeningen(Arrays.asList<Rekening>())
                .boekingen(Arrays.asList<Boeking>())
                .build()

        val regels = CheckService.getCheckAndFixRegels(administratie);
        regels.forEach { f -> println(f.omschrijving) };
        assertTrue("", regels.size == 1)
        assertTrue("", regels[0].rubriceerAction == FixAction.NONE)
        assertTrue("", regels[0].checkType == CheckType.WARNING)
    }


    private fun factuurBoeking(uuid: String, factuurNr: String, afschriftNummer: String): BetaaldeFactuurBoeking =
            BetaaldeFactuurBoeking.newBuilder()
                .uuid(uuid)
                .afschriftNummer(afschriftNummer)
                .factuurNummer(factuurNr)
                .build()


    private fun rekeningBoeking(uuid: String, rekeningNr: String, afschriftNummer: String): BetaaldeRekeningBoeking =
            BetaaldeRekeningBoeking.newBuilder()
                .uuid(uuid)
                .afschriftNummer(afschriftNummer)
                .rekeningNummer(rekeningNr)
                .build()


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