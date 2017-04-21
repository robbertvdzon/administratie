package com.vdzon.administratie.pdfgenerator.overzicht

import com.vdzon.administratie.model.*
import com.vdzon.administratie.model.boekingen.*
import com.vdzon.administratie.model.boekingen.relaties.BoekingMetAfschrift
import com.vdzon.administratie.model.boekingen.relaties.BoekingMetFactuur
import com.vdzon.administratie.model.boekingen.relaties.BoekingMetRekening

import java.math.BigDecimal
import java.time.LocalDate
import java.util.ArrayList
import java.util.stream.Collectors

object BoekingsVerrijker {
    fun verrijk(administratie: Administratie): List<VerrijkteBoeking> {
        return administratie.boekingen
                .map{ boeking -> verrijkBoeking(boeking, administratie) }
    }

    private fun verrijkBoeking(boeking: Boeking, administratie: Administratie): VerrijkteBoeking {
        if (boeking is InkomstenZonderFactuurBoeking) {
            return verrijk(boeking, administratie)
        }
        if (boeking is BetalingZonderFactuurBoeking) {
            return verrijk(boeking, administratie)
        }
        if (boeking is PriveBetalingBoeking) {
            return verrijk(boeking, administratie)
        }
        if (boeking is BetaaldeFactuurBoeking) {
            return verrijk(boeking, administratie)
        }
        if (boeking is BetaaldeRekeningBoeking) {
            return verrijk(boeking, administratie)
        }

        throw RuntimeException("Boeking is van onbekend type : " + boeking.javaClass.canonicalName)
    }


    private fun verrijk(boeking: InkomstenZonderFactuurBoeking, administratie: Administratie): VerrijkteBoeking {
        val afschift = getAfschift(boeking, administratie)
        val boekingsType = VerrijkteBoeking.BOEKINGSTYPE.INKOMSTEN_ZONDER_FACTUUR
        val boekingsBedrag = afschift?.bedrag?: BigDecimal.ZERO

        return VerrijkteBoeking(
                boeking,
                null,
                null,
                afschift,
                boekingsType,
                boekingsBedrag,
                afschift?.bedrag,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                afschift?.boekdatum,
                null,
                null)
    }

    private fun verrijk(boeking: BetalingZonderFactuurBoeking, administratie: Administratie): VerrijkteBoeking {
        val afschift = getAfschift(boeking, administratie)
        val boekingsType = VerrijkteBoeking.BOEKINGSTYPE.BETALING_ZONDER_FACTUUR
        val boekingsBedrag = afschift?.bedrag?: BigDecimal.ZERO

        return VerrijkteBoeking(
                boeking,
                null,
                null,
                afschift,
                boekingsType,
                boekingsBedrag,
                afschift?.bedrag,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                afschift?.boekdatum,
                null,
                null)
    }

    private fun verrijk(boeking: PriveBetalingBoeking, administratie: Administratie): VerrijkteBoeking {
        val afschift = getAfschift(boeking, administratie)
        val boekingsType = VerrijkteBoeking.BOEKINGSTYPE.PRIVE_BETALING
        val boekingsBedrag = afschift?.bedrag?: BigDecimal.ZERO

        return VerrijkteBoeking(
                boeking,
                null,
                null,
                afschift,
                boekingsType,
                boekingsBedrag,
                afschift?.bedrag,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                afschift?.boekdatum,
                null,
                null)
    }

    private fun verrijk(boeking: BetaaldeFactuurBoeking, administratie: Administratie): VerrijkteBoeking {
        val afschift = getAfschift(boeking, administratie)
        var factuur: Factuur? = getFactuur(boeking, administratie)
        if (factuur == null) {
            factuur = Factuur(
                    "",
                    "",
                    "",
                    afschift?.boekdatum,
                    null,
                    ArrayList<FactuurRegel>(),
                    BigDecimal.ZERO,
                    BigDecimal.ZERO,
                    BigDecimal.ZERO)
        }
        val boekingsType = VerrijkteBoeking.BOEKINGSTYPE.BETAALDE_FACTUUR
        val boekingsBedrag = afschift?.bedrag?: BigDecimal.ZERO
        val factuurBedrag = factuur.bedragIncBtw
        val factuurDate = factuur.factuurDate

        return VerrijkteBoeking(
                boeking,
                factuur,
                null,
                afschift,
                boekingsType,
                boekingsBedrag,
                afschift?.bedrag,
                factuurBedrag,
                BigDecimal.ZERO,
                afschift?.boekdatum,
                factuurDate,
                null)

    }

    private fun verrijk(boeking: BetaaldeRekeningBoeking, administratie: Administratie): VerrijkteBoeking {
        val afschift = getAfschift(boeking, administratie)
        val rekening = getRekening(boeking, administratie)
        val boekingsType = VerrijkteBoeking.BOEKINGSTYPE.BETAALDE_REKENING
        val boekingsBedrag = afschift?.bedrag?: BigDecimal.ZERO

        return VerrijkteBoeking(
                boeking,
                null,
                rekening,
                afschift,
                boekingsType,
                boekingsBedrag,
                afschift?.bedrag,
                BigDecimal.ZERO,
                rekening?.bedragIncBtw,
                afschift?.boekdatum,
                null,
                rekening?.rekeningDate)
    }

    private fun getRekening(boeking: BoekingMetRekening, administratie: Administratie): Rekening? {
        return administratie.rekeningen
                .filter{ rekening -> rekening.rekeningNummer == boeking.rekeningNummer }
                .firstOrNull()
    }

    private fun getFactuur(boeking: BoekingMetFactuur, administratie: Administratie): Factuur? {
        return administratie.facturen
                .filter{ factuur -> factuur.factuurNummer == boeking.factuurNummer }
                .firstOrNull()
    }

    private fun getAfschift(boeking: BoekingMetAfschrift, administratie: Administratie): Afschrift? {
        return administratie.afschriften
                .filter{ afschrift -> afschrift.nummer == boeking.afschriftNummer }
                .firstOrNull()
    }
}
