package com.vdzon.administratie.pdfgenerator.overzicht;

import com.vdzon.administratie.model.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class CalculateOverzicht {
    private static DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    
    public static Overzicht calculateOverzicht(Administratie administratie, String beginDateStr, String endDateStr){
        Overzicht overzicht = new Overzicht();

        overzicht.beginDate = LocalDate.parse(beginDateStr, DATE_FORMATTER);
        overzicht.endDate = LocalDate.parse(endDateStr, DATE_FORMATTER);
        overzicht.filteredFacturen = administratie.getFacturen().stream().filter(factuur -> betweenOrAtDates(factuur.getFactuurDate(), overzicht.beginDate, overzicht.endDate)).collect(Collectors.toList());
        overzicht.filteredRekeningen = administratie.getRekeningen().stream().filter(rekening-> betweenOrAtDates(rekening.getRekeningDate(), overzicht.beginDate, overzicht.endDate)).collect(Collectors.toList());
        overzicht.filteredDeclaraties = administratie.getDeclaraties().stream().filter(declaratie-> betweenOrAtDates(declaratie.getDeclaratieDate(), overzicht.beginDate, overzicht.endDate)).collect(Collectors.toList());
        overzicht.filteredAfschriften = administratie.getAfschriften().stream().filter(afschriften-> betweenOrAtDates(afschriften.getBoekdatum(), overzicht.beginDate, overzicht.endDate)).collect(Collectors.toList());


        overzicht.facturenTotaalExBtw = overzicht.filteredFacturen.stream().mapToDouble(factuur -> factuur.getBedragExBtw()).sum();
        overzicht.facturenTotaalIncBtw = overzicht.filteredFacturen.stream().mapToDouble(factuur -> factuur.getBedragIncBtw()).sum();
        overzicht.facturenTotaalBtw = overzicht.filteredFacturen.stream().mapToDouble(factuur -> factuur.getBtw()).sum();

        overzicht.rekeningenTotaalExBtw = overzicht.filteredRekeningen.stream().mapToDouble(rekening -> rekening.getBedragExBtw()).sum();
        overzicht.rekeningenTotaalIncBtw = overzicht.filteredRekeningen.stream().mapToDouble(rekening -> rekening.getBedragIncBtw()).sum();
        overzicht.rekeningenTotaalBtw = overzicht.filteredRekeningen.stream().mapToDouble(rekening -> rekening.getBtw()).sum();

        overzicht.declaratiesTotaalExBtw = overzicht.filteredDeclaraties.stream().mapToDouble(declaratie -> declaratie.getBedragExBtw()).sum();
        overzicht.declaratiesTotaalIncBtw = overzicht.filteredDeclaraties.stream().mapToDouble(declaratie -> declaratie.getBedragIncBtw()).sum();
        overzicht.declaratiesTotaalBtw = overzicht.filteredDeclaraties.stream().mapToDouble(declaratie -> declaratie.getBtw()).sum();

        overzicht.betaaldeFacturen = overzicht.filteredFacturen.stream().filter(factuur->factuur.isBetaald()).mapToDouble(declaratie -> declaratie.getBedragIncBtw()).sum();
        overzicht.onbetaaldeFacturen = overzicht.filteredFacturen.stream().filter(factuur->!factuur.isBetaald()).mapToDouble(declaratie -> declaratie.getBedragIncBtw()).sum();
        overzicht.betaaldeRekeningen = overzicht.rekeningenTotaalIncBtw;
        overzicht.verwachtTotaalOpRekeningBij = overzicht.betaaldeFacturen-overzicht.betaaldeRekeningen;

        overzicht.werkelijkOpBankBij = overzicht.filteredAfschriften.stream().mapToDouble(afschrift -> afschrift.getBedrag()).sum();
        overzicht.priveOpBankBij = overzicht.filteredAfschriften.stream().filter(afschrift->afschrift.getBoekingType()==BoekingType.PRIVE).mapToDouble(afschrift -> afschrift.getBedrag()).sum();
        overzicht.werkelijkOpBankBijVoorAdministratie = overzicht.werkelijkOpBankBij - overzicht.priveOpBankBij;
        overzicht.verschilTussenVerwachtEnWerkelijk = overzicht.verwachtTotaalOpRekeningBij - overzicht.werkelijkOpBankBijVoorAdministratie;

        overzicht.belastbaarInkomenExBtw = overzicht.facturenTotaalExBtw - overzicht.rekeningenTotaalExBtw - overzicht.declaratiesTotaalExBtw;
        overzicht.belastbaarInkomenIncBtw = overzicht.facturenTotaalIncBtw - overzicht.rekeningenTotaalIncBtw - overzicht.declaratiesTotaalIncBtw;
        overzicht.belastbaarInkomenBtw = overzicht.facturenTotaalBtw - overzicht.rekeningenTotaalBtw - overzicht.declaratiesTotaalBtw;
        

        return overzicht;
    }
    
    private static boolean betweenOrAtDates(LocalDate date, LocalDate beginDate, LocalDate endData){
        return date.equals(beginDate) || date.equals(endData) || betweenDates(date, beginDate, endData);
    }

    private static boolean betweenDates(LocalDate date, LocalDate beginDate, LocalDate endData){
        return date.isAfter(beginDate) && date.isBefore(endData);
    }
}
