package com.vdzon.administratie.checkandfix;

import com.vdzon.administratie.checkandfix.model.CheckAndFixRegel;
import com.vdzon.administratie.model.*;
import com.vdzon.administratie.model.boekingen.BetaaldeFactuurBoeking;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by robbe on 2/12/2017.
 */
class CheckAndFixServiceTest2 {
    @Test
    public void when_alle_facturen_betaald_then_geen_checkregels() {
        Administratie administratie = Administratie.newBuilder()
                .afschriften(Arrays.asList(buildAfschrift("a1", 100), buildAfschrift("a2", 101)))
                .facturen(Arrays.asList(buildFactuur("f1", 100, 0), buildFactuur("f2", 100, 0)))
                .rekeningen(new ArrayList<Rekening>())
                .boekingen(Arrays.asList(factuurBoeking("b1", "f1", "a1"), factuurBoeking("b2", "f2", "a2"))).build();

        List<CheckAndFixRegel> regels = CheckService.getCheckAndFixRegels(administratie);

        assertTrue(regels.size() == 1);
    }

    private BetaaldeFactuurBoeking factuurBoeking(String uuid, String factuurNr, String afschriftNummer) {
        return BetaaldeFactuurBoeking.newBuilder()
                                     .uuid(uuid)
                                     .afschriftNummer(afschriftNummer)
                                     .factuurNummer(factuurNr)
                                     .build();
    }

    private Factuur buildFactuur(String nummer, double ex, double perc) {
        return Factuur.newBuilder()
               .bedragExBtw(java.math.BigDecimal.valueOf(ex))
               .factuurNummer(nummer)
               .factuurRegels(Arrays.asList(FactuurRegel.newBuilder().aantal(java.math.BigDecimal.ONE).stuksPrijs(java.math.BigDecimal.valueOf(ex)).btwPercentage(java.math.BigDecimal.valueOf(perc)).build()))
               .build();
    }

    private Afschrift buildAfschrift(String nummer, double bedrag) {
        return Afschrift.newBuilder()
                 .nummer(nummer)
                 .bedrag(java.math.BigDecimal.valueOf(bedrag))
                 .build();
    }

    private Rekening buildRekening(String nummer, double inc, double ex, double btw) {
        return Rekening.newBuilder()
                .bedragExBtw(java.math.BigDecimal.valueOf(ex))
                .bedragIncBtw(java.math.BigDecimal.valueOf(inc))
                .btw(java.math.BigDecimal.valueOf(btw))
                .rekeningNummer(nummer)
                .build();
    }

}