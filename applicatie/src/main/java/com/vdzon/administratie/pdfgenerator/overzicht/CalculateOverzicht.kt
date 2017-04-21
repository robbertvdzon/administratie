package com.vdzon.administratie.pdfgenerator.overzicht

import com.vdzon.administratie.model.*
import com.vdzon.administratie.model.boekingen.VerrijkteBoeking
import com.vdzon.administratie.model.boekingen.relaties.BoekingMetAfschrift
import com.vdzon.administratie.model.boekingen.relaties.BoekingMetFactuur
import com.vdzon.administratie.model.boekingen.relaties.BoekingMetRekening

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.stream.Collectors
import java.util.stream.Stream

object CalculateOverzicht {
    private val DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy")

    fun calculateOverzicht(administratie: Administratie, beginDateStr: String, endDateStr: String): Overzicht {
        val overzicht = Overzicht()
        val verrijkteBoekings = BoekingsVerrijker.verrijk(administratie)
        val boekingenCache = BoekingenCache(administratie.boekingen)
        overzicht.beginDate = LocalDate.parse(beginDateStr, DATE_FORMATTER)
        overzicht.endDate = LocalDate.parse(endDateStr, DATE_FORMATTER)

        overzicht.filteredFacturen = administratie.facturen.filter{ factuur -> betweenOrAtDates(factuur.factuurDate, overzicht.beginDate, overzicht.endDate) }
        overzicht.filteredRekeningen = administratie.rekeningen.filter({ rekening -> betweenOrAtDates(rekening.rekeningDate, overzicht.beginDate, overzicht.endDate) })
        overzicht.filteredDeclaraties = administratie.declaraties.filter({ declaratie -> betweenOrAtDates(declaratie.declaratieDate, overzicht.beginDate, overzicht.endDate) })
        overzicht.filteredAfschriften = administratie.afschriften.filter({ afschriften -> betweenOrAtDates(afschriften.boekdatum, overzicht.beginDate, overzicht.endDate) })


        overzicht.facturenTotaalExBtw = overzicht.filteredFacturen!!.map({ factuur -> factuur.bedragExBtw.toDouble() }).sum()
        overzicht.facturenTotaalIncBtw = overzicht.filteredFacturen!!.map({ factuur -> factuur.bedragIncBtw.toDouble() }).sum()
        overzicht.facturenTotaalBtw = overzicht.filteredFacturen!!.map({ factuur -> factuur.btw.toDouble() }).sum()

        overzicht.rekeningenTotaalExBtw = overzicht.filteredRekeningen!!.map({ rekening -> rekening.bedragExBtw.toDouble() }).sum()
        overzicht.rekeningenTotaalIncBtw = overzicht.filteredRekeningen!!.map({ rekening -> rekening.bedragIncBtw.toDouble() }).sum()
        overzicht.rekeningenTotaalBtw = overzicht.filteredRekeningen!!.map({ rekening -> rekening.btw.toDouble() }).sum()

        overzicht.declaratiesTotaalExBtw = overzicht.filteredDeclaraties!!.map({ declaratie -> declaratie.bedragExBtw.toDouble() }).sum()
        overzicht.declaratiesTotaalIncBtw = overzicht.filteredDeclaraties!!.map({ declaratie -> declaratie.bedragIncBtw.toDouble() }).sum()
        overzicht.declaratiesTotaalBtw = overzicht.filteredDeclaraties!!.map({ declaratie -> declaratie.btw.toDouble() }).sum()

        // overzicht van alle ontvangen facturen
        overzicht.ontvangenFactuurBetalingenBetaaldBinnenGeselecteerdePeriode = verrijkteBoekings
                .filter({ boeking -> boeking.boekingsType === VerrijkteBoeking.BOEKINGSTYPE.BETAALDE_FACTUUR })
                .filter({ boeking -> betweenOrAtDates(boeking.factuurDate, overzicht.beginDate, overzicht.endDate) })
                .filter({ boeking -> betweenOrAtDates(boeking.afschriftDate, overzicht.beginDate, overzicht.endDate) })
                .map({ boeking -> boeking.boekingsBedrag.toDouble() })
                .sum()

        overzicht.ontvangenFactuurBetalingenVanBuitenGeselecteerdePeriodeBetaaldBinnenGeselecteerdePeriode = verrijkteBoekings
                .filter({ boeking -> boeking.boekingsType === VerrijkteBoeking.BOEKINGSTYPE.BETAALDE_FACTUUR })
                .filter({ boeking -> !betweenOrAtDates(boeking.factuurDate, overzicht.beginDate, overzicht.endDate) })
                .filter({ boeking -> betweenOrAtDates(boeking.afschriftDate, overzicht.beginDate, overzicht.endDate) })
                .map({ boeking -> boeking.boekingsBedrag.toDouble() })
                .sum()

        overzicht.ontvangenFacturenBetaaldBuitenGeselecteerdePeriode = verrijkteBoekings
                .filter({ boeking -> boeking.boekingsType === VerrijkteBoeking.BOEKINGSTYPE.BETAALDE_FACTUUR })
                .filter({ boeking -> betweenOrAtDates(boeking.factuurDate, overzicht.beginDate, overzicht.endDate) })
                .filter({ boeking -> !betweenOrAtDates(boeking.afschriftDate, overzicht.beginDate, overzicht.endDate) })
                .map({ boeking -> boeking.boekingsBedrag.toDouble() })
                .sum()

        // overzicht van alle betaalde rekeningen
        overzicht.betaaldeRekeningenBetaaldBinnenGeselecteerdePeriode = -1 * verrijkteBoekings
                .filter({ boeking -> boeking.boekingsType === VerrijkteBoeking.BOEKINGSTYPE.BETAALDE_REKENING })
                .filter({ boeking -> betweenOrAtDates(boeking.rekeningDate, overzicht.beginDate, overzicht.endDate) })
                .filter({ boeking -> betweenOrAtDates(boeking.afschriftDate, overzicht.beginDate, overzicht.endDate) })
                .map({ boeking -> boeking.boekingsBedrag.toDouble() })
                .sum()

        overzicht.betaaldeRekeningenVanBuitenGeselecteerdePeriodeBetaaldBinnenGeselecteerdePeriode = -1 * verrijkteBoekings
                .filter({ boeking -> boeking.boekingsType === VerrijkteBoeking.BOEKINGSTYPE.BETAALDE_REKENING })
                .filter({ boeking -> !betweenOrAtDates(boeking.rekeningDate, overzicht.beginDate, overzicht.endDate) })
                .filter({ boeking -> betweenOrAtDates(boeking.afschriftDate, overzicht.beginDate, overzicht.endDate) })
                .map({ boeking -> boeking.boekingsBedrag.toDouble() })
                .sum()

        overzicht.betaaldeRekeningenBetaaldBuitenGeselecteerdePeriode = -1 * verrijkteBoekings
                .filter({ boeking -> boeking.boekingsType === VerrijkteBoeking.BOEKINGSTYPE.BETAALDE_REKENING })
                .filter({ boeking -> betweenOrAtDates(boeking.rekeningDate, overzicht.beginDate, overzicht.endDate) })
                .filter({ boeking -> !betweenOrAtDates(boeking.afschriftDate, overzicht.beginDate, overzicht.endDate) })
                .map({ boeking -> boeking.boekingsBedrag.toDouble() })
                .sum()

        // overzicht van overige betalingen
        overzicht.priveBoekingen = verrijkteBoekings
                .filter({ boeking -> boeking.boekingsType === VerrijkteBoeking.BOEKINGSTYPE.PRIVE_BETALING })
                .filter({ boeking -> betweenOrAtDates(boeking.afschriftDate, overzicht.beginDate, overzicht.endDate) })
                .map({ boeking -> boeking.boekingsBedrag.toDouble() })
                .sum()

        overzicht.ontvangenInkomstenZonderFactuur = verrijkteBoekings
                .filter({ boeking -> boeking.boekingsType === VerrijkteBoeking.BOEKINGSTYPE.INKOMSTEN_ZONDER_FACTUUR })
                .filter({ boeking -> betweenOrAtDates(boeking.afschriftDate, overzicht.beginDate, overzicht.endDate) })
                .map({ boeking -> boeking.boekingsBedrag.toDouble() })
                .sum()

        overzicht.betaaldeRekeningenZonderFactuur = -1 * verrijkteBoekings
                .filter({ boeking -> boeking.boekingsType === VerrijkteBoeking.BOEKINGSTYPE.BETALING_ZONDER_FACTUUR })
                .filter({ boeking -> betweenOrAtDates(boeking.afschriftDate, overzicht.beginDate, overzicht.endDate) })
                .map({ boeking -> boeking.boekingsBedrag.toDouble() })
                .sum()

        // zoek facturen en rekeningen zonder boeking
        overzicht.onbetaaldeFacturen = overzicht.filteredFacturen!!

                .filter({ factuur -> factuurZonderBoekingen(factuur, boekingenCache) })
                .map({ factuur -> factuur.bedragIncBtw.toDouble() })
                .sum()

        overzicht.onbetaaldeRekeningen = overzicht.filteredRekeningen!!

                .filter({ rekening -> rekeningZonderBoekingen(rekening, boekingenCache) })
                .map({ rekening -> rekening.bedragIncBtw.toDouble() })
                .sum()


        //--


        overzicht.verwachtTotaalOpRekeningBij = 0 + overzicht.facturenTotaalIncBtw - overzicht.rekeningenTotaalIncBtw + overzicht.ontvangenFactuurBetalingenVanBuitenGeselecteerdePeriodeBetaaldBinnenGeselecteerdePeriode
        -overzicht.ontvangenFacturenBetaaldBuitenGeselecteerdePeriode
        -overzicht.onbetaaldeFacturen
        -overzicht.betaaldeRekeningenVanBuitenGeselecteerdePeriodeBetaaldBinnenGeselecteerdePeriode
        +overzicht.betaaldeRekeningenBetaaldBuitenGeselecteerdePeriode
        +overzicht.onbetaaldeRekeningen
        +overzicht.priveBoekingen
        +overzicht.ontvangenInkomstenZonderFactuur - overzicht.betaaldeRekeningenZonderFactuur

        overzicht.werkelijkOpBankBij = overzicht.filteredAfschriften!!.map({ afschrift -> afschrift.bedrag.toDouble() }).sum()
        overzicht.verschilTussenVerwachtEnWerkelijk = overzicht.verwachtTotaalOpRekeningBij - overzicht.werkelijkOpBankBij

        overzicht.belastbaarInkomenExBtw = overzicht.facturenTotaalExBtw - overzicht.rekeningenTotaalExBtw - overzicht.declaratiesTotaalExBtw - overzicht.betaaldeRekeningenZonderFactuur
        overzicht.belastbaarInkomenIncBtw = overzicht.facturenTotaalIncBtw - overzicht.rekeningenTotaalIncBtw - overzicht.declaratiesTotaalIncBtw - overzicht.betaaldeRekeningenZonderFactuur
        overzicht.belastbaarInkomenBtw = overzicht.facturenTotaalBtw - overzicht.rekeningenTotaalBtw - overzicht.declaratiesTotaalBtw


        return overzicht
    }

    private fun rekeningZonderBoekingen(rekening: Rekening, boekingenCache: BoekingenCache): Boolean {
        val boekingenVanRekening = boekingenCache.getBoekingenVanRekening(rekening.rekeningNummer)
        return boekingenVanRekening == null || boekingenVanRekening.size == 0
    }

    private fun factuurZonderBoekingen(factuur: Factuur, boekingenCache: BoekingenCache): Boolean {
        val boekingenVanFactuur = boekingenCache.getBoekingenVanFactuur(factuur.factuurNummer)
        return boekingenVanFactuur == null || boekingenVanFactuur.size == 0
    }

    private fun betweenOrAtDates(date: LocalDate?, beginDate: LocalDate?, endData: LocalDate?): Boolean {
        return date == beginDate || date == endData || betweenDates(date, beginDate, endData)
    }

    private fun betweenDates(date: LocalDate?, beginDate: LocalDate?, endData: LocalDate?): Boolean {
        return date!!.isAfter(beginDate) && date!!.isBefore(endData)
    }
}
