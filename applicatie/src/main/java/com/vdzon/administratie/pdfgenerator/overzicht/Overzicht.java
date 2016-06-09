package com.vdzon.administratie.pdfgenerator.overzicht;

import com.vdzon.administratie.model.Afschrift;
import com.vdzon.administratie.model.Declaratie;
import com.vdzon.administratie.model.Factuur;
import com.vdzon.administratie.model.Rekening;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class Overzicht {

    public double facturenTotaalExBtw = 0;
    public double facturenTotaalIncBtw = 0;
    public double facturenTotaalBtw = 0;

    public double rekeningenTotaalExBtw = 0;
    public double rekeningenTotaalIncBtw = 0;
    public double rekeningenTotaalBtw = 0;

    public double declaratiesTotaalExBtw = 0;
    public double declaratiesTotaalIncBtw = 0;
    public double declaratiesTotaalBtw = 0;

    public double betaaldeFacturen = 0;
    public double onbetaaldeFacturen = 0;
    public double betaaldeRekeningen = 0;
    public double verwachtTotaalOpRekeningBij = 0;

    public double werkelijkOpBankBij = 0;
    public double priveOpBankBij = 0;
    public double werkelijkOpBankBijVoorAdministratie = 0;
    public double verschilTussenVerwachtEnWerkelijk = 0;

    public double belastbaarInkomenExBtw = 0;
    public double belastbaarInkomenIncBtw = 0;
    public double belastbaarInkomenBtw = 0;

    public LocalDate beginDate = null;
    public LocalDate endDate = null;
    public List<Factuur> filteredFacturen = null;
    public List<Rekening> filteredRekeningen = null;
    public List<Declaratie> filteredDeclaraties = null;
    public List<Afschrift> filteredAfschriften = null;

}
