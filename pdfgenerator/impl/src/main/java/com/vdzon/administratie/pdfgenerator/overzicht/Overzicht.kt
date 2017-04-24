package com.vdzon.administratie.pdfgenerator.overzicht

import com.vdzon.administratie.model.Afschrift
import com.vdzon.administratie.model.Declaratie
import com.vdzon.administratie.model.Factuur
import com.vdzon.administratie.model.Rekening

import java.time.LocalDate
import java.util.stream.Collectors

class Overzicht {

    var facturenTotaalExBtw = 0.0
    var facturenTotaalIncBtw = 0.0
    var facturenTotaalBtw = 0.0

    var rekeningenTotaalExBtw = 0.0
    var rekeningenTotaalIncBtw = 0.0
    var rekeningenTotaalBtw = 0.0

    var declaratiesTotaalExBtw = 0.0
    var declaratiesTotaalIncBtw = 0.0
    var declaratiesTotaalBtw = 0.0

    // onderstaande velden zijn berekend aan de hand van de afschriften *************************
    var ontvangenFactuurBetalingenBetaaldBinnenGeselecteerdePeriode = 0.0
    var ontvangenFactuurBetalingenVanBuitenGeselecteerdePeriodeBetaaldBinnenGeselecteerdePeriode = 0.0
    var ontvangenFacturenBetaaldBuitenGeselecteerdePeriode = 0.0
    var onbetaaldeFacturen = 0.0

    var betaaldeRekeningenBetaaldBinnenGeselecteerdePeriode = 0.0
    var betaaldeRekeningenVanBuitenGeselecteerdePeriodeBetaaldBinnenGeselecteerdePeriode = 0.0
    var betaaldeRekeningenBetaaldBuitenGeselecteerdePeriode = 0.0
    var onbetaaldeRekeningen = 0.0

    var priveBoekingen = 0.0
    var ontvangenInkomstenZonderFactuur = 0.0
    var betaaldeRekeningenZonderFactuur = 0.0

    // controle bedragen **************************************
    var verwachtTotaalOpRekeningBij = 0.0

    var werkelijkOpBankBij = 0.0
    //    public double werkelijkOpBankBijVoorAdministratie = 0;
    var verschilTussenVerwachtEnWerkelijk = 0.0


    // voor btw aangifte **************************************
    var belastbaarInkomenExBtw = 0.0
    var belastbaarInkomenIncBtw = 0.0
    var belastbaarInkomenBtw = 0.0

    var beginDate: LocalDate? = null
    var endDate: LocalDate? = null
    var filteredFacturen: List<Factuur>? = null
    var filteredRekeningen: List<Rekening>? = null
    var filteredDeclaraties: List<Declaratie>? = null
    var filteredAfschriften: List<Afschrift>? = null

    //    public List<Factuur> alleFacturen = null;

}
