package com.vdzon.administratie.rubriceren.rubriceerRegels;

import com.vdzon.administratie.model.Afschrift;
import com.vdzon.administratie.model.BoekingType;
import com.vdzon.administratie.model.Gebruiker;
import com.vdzon.administratie.rubriceren.model.RubriceerRegel;

public class RubriceerPriveRegels extends RubriceerHelper {


    @RubriceerRuleCommit
    public void processRegel(RubriceerRegel regel, Gebruiker gebruiker) {
        Afschrift afschrift = regel.getAfschrift().toAfschrift();
        switch (regel.getRubriceerAction()) {
            case PRIVE:
                gebruiker.getDefaultAdministratie().removeAfschrift(afschrift.getNummer());
                gebruiker.getDefaultAdministratie().addAfschrift(
                        afschrift
                                .toBuilder()
                                .boekingType(BoekingType.PRIVE)
                                .factuurNummer("")
                                .rekeningNummer("")
                                .build()
                );
                break;
        }
    }


}
