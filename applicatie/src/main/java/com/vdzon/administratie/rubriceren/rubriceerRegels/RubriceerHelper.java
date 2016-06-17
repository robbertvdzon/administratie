package com.vdzon.administratie.rubriceren.rubriceerRegels;

import com.vdzon.administratie.model.Gebruiker;

import java.util.Comparator;

public class RubriceerHelper {
    static int findNextRekeningNummer(Gebruiker gebruiker) {
        return 1 + gebruiker.getDefaultAdministratie().getRekeningen().stream().map(rekening -> Integer.parseInt(rekening.getRekeningNummer())).max(Comparator.naturalOrder()).orElse(1000);
    }

}
