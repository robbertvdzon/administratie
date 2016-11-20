package com.vdzon.administratie.pdfgenerator.overzicht;

import com.vdzon.administratie.model.*;
import com.vdzon.administratie.model.boekingen.VerrijkteBoeking;
import com.vdzon.administratie.model.boekingen.relaties.BoekingMetFactuur;
import com.vdzon.administratie.model.boekingen.relaties.BoekingMetRekening;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class CalculateOverzicht {
    private static DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    
    public static Overzicht calculateOverzicht(Administratie administratie, String beginDateStr, String endDateStr){
        Overzicht overzicht = new Overzicht();
        List<VerrijkteBoeking> verrijkteBoekings = BoekingsVerrijker.verrijk(administratie);
        BoekingenCache boekingenCache = new BoekingenCache(administratie.getBoekingen());
        overzicht.beginDate = LocalDate.parse(beginDateStr, DATE_FORMATTER);
        overzicht.endDate = LocalDate.parse(endDateStr, DATE_FORMATTER);

        overzicht.filteredFacturen = administratie.getFacturen().stream().filter(factuur -> betweenOrAtDates(factuur.factuurDate(), overzicht.beginDate, overzicht.endDate)).collect(Collectors.toList());
        overzicht.filteredRekeningen = administratie.getRekeningen().stream().filter(rekening-> betweenOrAtDates(rekening.rekeningDate(), overzicht.beginDate, overzicht.endDate)).collect(Collectors.toList());
        overzicht.filteredDeclaraties = administratie.getDeclaraties().stream().filter(declaratie-> betweenOrAtDates(declaratie.getDeclaratieDate(), overzicht.beginDate, overzicht.endDate)).collect(Collectors.toList());
        overzicht.filteredAfschriften = administratie.getAfschriften().stream().filter(afschriften-> betweenOrAtDates(afschriften.boekdatum(), overzicht.beginDate, overzicht.endDate)).collect(Collectors.toList());


        overzicht.facturenTotaalExBtw = overzicht.filteredFacturen.stream().mapToDouble(factuur -> factuur.bedragExBtw()).sum();
        overzicht.facturenTotaalIncBtw = overzicht.filteredFacturen.stream().mapToDouble(factuur -> factuur.bedragIncBtw()).sum();
        overzicht.facturenTotaalBtw = overzicht.filteredFacturen.stream().mapToDouble(factuur -> factuur.btw()).sum();

        overzicht.rekeningenTotaalExBtw = overzicht.filteredRekeningen.stream().mapToDouble(rekening -> rekening.bedragExBtw()).sum();
        overzicht.rekeningenTotaalIncBtw = overzicht.filteredRekeningen.stream().mapToDouble(rekening -> rekening.bedragIncBtw()).sum();
        overzicht.rekeningenTotaalBtw = overzicht.filteredRekeningen.stream().mapToDouble(rekening -> rekening.btw()).sum();

        overzicht.declaratiesTotaalExBtw = overzicht.filteredDeclaraties.stream().mapToDouble(declaratie -> declaratie.getBedragExBtw()).sum();
        overzicht.declaratiesTotaalIncBtw = overzicht.filteredDeclaraties.stream().mapToDouble(declaratie -> declaratie.getBedragIncBtw()).sum();
        overzicht.declaratiesTotaalBtw = overzicht.filteredDeclaraties.stream().mapToDouble(declaratie -> declaratie.getBtw()).sum();

        // overzicht van alle ontvangen facturen
        overzicht.ontvangenFactuurBetalingenBetaaldBinnenGeselecteerdePeriode = verrijkteBoekings.stream()
                .filter(boeking -> boeking.getBoekingsType() == VerrijkteBoeking.BOEKINGSTYPE.BETAALDE_FACTUUR)
                .filter(boeking -> betweenOrAtDates(boeking.getFactuurDate(), overzicht.beginDate, overzicht.endDate))
                .filter(boeking -> betweenOrAtDates(boeking.getAfschriftDate(), overzicht.beginDate, overzicht.endDate))
                .mapToDouble(boeking -> boeking.getBoekingsBedrag())
                .sum();

        overzicht.ontvangenFactuurBetalingenVanBuitenGeselecteerdePeriodeBetaaldBinnenGeselecteerdePeriode = verrijkteBoekings.stream()
                .filter(boeking -> boeking.getBoekingsType() == VerrijkteBoeking.BOEKINGSTYPE.BETAALDE_FACTUUR)
                .filter(boeking -> !betweenOrAtDates(boeking.getFactuurDate(), overzicht.beginDate, overzicht.endDate))
                .filter(boeking -> betweenOrAtDates(boeking.getAfschriftDate(), overzicht.beginDate, overzicht.endDate))
                .mapToDouble(boeking -> boeking.getBoekingsBedrag())
                .sum();

        overzicht.ontvangenFacturenBetaaldBuitenGeselecteerdePeriode = verrijkteBoekings.stream()
                .filter(boeking -> boeking.getBoekingsType() == VerrijkteBoeking.BOEKINGSTYPE.BETAALDE_FACTUUR)
                .filter(boeking -> betweenOrAtDates(boeking.getFactuurDate(), overzicht.beginDate, overzicht.endDate))
                .filter(boeking -> !betweenOrAtDates(boeking.getAfschriftDate(), overzicht.beginDate, overzicht.endDate))
                .mapToDouble(boeking -> boeking.getBoekingsBedrag())
                .sum();

        // overzicht van alle betaalde rekeningen
        overzicht.betaaldeRekeningenBetaaldBinnenGeselecteerdePeriode = -1*verrijkteBoekings.stream()
                .filter(boeking -> boeking.getBoekingsType() == VerrijkteBoeking.BOEKINGSTYPE.BETAALDE_REKENING)
                .filter(boeking -> betweenOrAtDates(boeking.getRekeningDate(), overzicht.beginDate, overzicht.endDate))
                .filter(boeking -> betweenOrAtDates(boeking.getAfschriftDate(), overzicht.beginDate, overzicht.endDate))
                .mapToDouble(boeking -> boeking.getBoekingsBedrag())
                .sum();

        overzicht.betaaldeRekeningenVanBuitenGeselecteerdePeriodeBetaaldBinnenGeselecteerdePeriode = -1*verrijkteBoekings.stream()
                .filter(boeking -> boeking.getBoekingsType() == VerrijkteBoeking.BOEKINGSTYPE.BETAALDE_REKENING)
                .filter(boeking -> !betweenOrAtDates(boeking.getRekeningDate(), overzicht.beginDate, overzicht.endDate))
                .filter(boeking -> betweenOrAtDates(boeking.getAfschriftDate(), overzicht.beginDate, overzicht.endDate))
                .mapToDouble(boeking -> boeking.getBoekingsBedrag())
                .sum();

        overzicht.betaaldeRekeningenBetaaldBuitenGeselecteerdePeriode = -1*verrijkteBoekings.stream()
                .filter(boeking -> boeking.getBoekingsType() == VerrijkteBoeking.BOEKINGSTYPE.BETAALDE_REKENING)
                .filter(boeking -> betweenOrAtDates(boeking.getRekeningDate(), overzicht.beginDate, overzicht.endDate))
                .filter(boeking -> !betweenOrAtDates(boeking.getAfschriftDate(), overzicht.beginDate, overzicht.endDate))
                .mapToDouble(boeking -> boeking.getBoekingsBedrag())
                .sum();

        // overzicht van overige betalingen
        overzicht.priveBoekingen = verrijkteBoekings.stream()
                .filter(boeking -> boeking.getBoekingsType() == VerrijkteBoeking.BOEKINGSTYPE.PRIVE_BETALING)
                .filter(boeking -> betweenOrAtDates(boeking.getAfschriftDate(), overzicht.beginDate, overzicht.endDate))
                .mapToDouble(boeking -> boeking.getBoekingsBedrag())
                .sum();

        overzicht.ontvangenInkomstenZonderFactuur = verrijkteBoekings.stream()
                .filter(boeking -> boeking.getBoekingsType() == VerrijkteBoeking.BOEKINGSTYPE.INKOMSTEN_ZONDER_FACTUUR)
                .filter(boeking -> betweenOrAtDates(boeking.getAfschriftDate(), overzicht.beginDate, overzicht.endDate))
                .mapToDouble(boeking -> boeking.getBoekingsBedrag())
                .sum();

        overzicht.betaaldeRekeningenZonderFactuur = -1*verrijkteBoekings.stream()
                .filter(boeking -> boeking.getBoekingsType() == VerrijkteBoeking.BOEKINGSTYPE.BETALING_ZONDER_FACTUUR)
                .filter(boeking -> betweenOrAtDates(boeking.getAfschriftDate(), overzicht.beginDate, overzicht.endDate))
                .mapToDouble(boeking -> boeking.getBoekingsBedrag())
                .sum();

        // zoek facturen en rekeningen zonder boeking
        overzicht.onbetaaldeFacturen = overzicht.filteredFacturen
                .stream()
                .filter(factuur -> factuurZonderBoekingen(factuur, boekingenCache))
                .mapToDouble(factuur -> factuur.bedragIncBtw())
                .sum();

        overzicht.onbetaaldeRekeningen = overzicht.filteredRekeningen
                .stream()
                .filter(rekening -> rekeningZonderBoekingen(rekening, boekingenCache))
                .mapToDouble(rekening-> rekening.bedragIncBtw())
                .sum();


//--


        overzicht.verwachtTotaalOpRekeningBij = 0
                + overzicht.facturenTotaalIncBtw
                - overzicht.rekeningenTotaalIncBtw
                + overzicht.ontvangenFactuurBetalingenVanBuitenGeselecteerdePeriodeBetaaldBinnenGeselecteerdePeriode
                - overzicht.ontvangenFacturenBetaaldBuitenGeselecteerdePeriode
                - overzicht.onbetaaldeFacturen
                - overzicht.betaaldeRekeningenVanBuitenGeselecteerdePeriodeBetaaldBinnenGeselecteerdePeriode
                + overzicht.betaaldeRekeningenBetaaldBuitenGeselecteerdePeriode
                + overzicht.onbetaaldeRekeningen
                + overzicht.priveBoekingen
                + overzicht.ontvangenInkomstenZonderFactuur
                - overzicht.betaaldeRekeningenZonderFactuur;

        overzicht.werkelijkOpBankBij = overzicht.filteredAfschriften.stream().mapToDouble(afschrift -> afschrift.bedrag()).sum();
        overzicht.verschilTussenVerwachtEnWerkelijk = overzicht.verwachtTotaalOpRekeningBij - overzicht.werkelijkOpBankBij;

        overzicht.belastbaarInkomenExBtw = overzicht.facturenTotaalExBtw - overzicht.rekeningenTotaalExBtw - overzicht.declaratiesTotaalExBtw - overzicht.betaaldeRekeningenZonderFactuur;
        overzicht.belastbaarInkomenIncBtw = overzicht.facturenTotaalIncBtw - overzicht.rekeningenTotaalIncBtw - overzicht.declaratiesTotaalIncBtw - overzicht.betaaldeRekeningenZonderFactuur;
        overzicht.belastbaarInkomenBtw = overzicht.facturenTotaalBtw - overzicht.rekeningenTotaalBtw - overzicht.declaratiesTotaalBtw;
        

        return overzicht;
    }

    private static boolean rekeningZonderBoekingen(Rekening rekening, BoekingenCache boekingenCache) {
        List<BoekingMetRekening> boekingenVanRekening = boekingenCache.getBoekingenVanRekening(rekening.rekeningNummer());
        return boekingenVanRekening==null || boekingenVanRekening.size()==0;
    }

    private static boolean factuurZonderBoekingen(Factuur factuur, BoekingenCache boekingenCache) {
        List<BoekingMetFactuur> boekingenVanFactuur = boekingenCache.getBoekingenVanFactuur(factuur.factuurNummer());
        return boekingenVanFactuur==null || boekingenVanFactuur.size()==0;
    }

    private static boolean betweenOrAtDates(LocalDate date, LocalDate beginDate, LocalDate endData){
        return date.equals(beginDate) || date.equals(endData) || betweenDates(date, beginDate, endData);
    }

    private static boolean betweenDates(LocalDate date, LocalDate beginDate, LocalDate endData){
        return date.isAfter(beginDate) && date.isBefore(endData);
    }
}
