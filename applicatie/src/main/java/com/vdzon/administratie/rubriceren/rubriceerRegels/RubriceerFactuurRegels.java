package com.vdzon.administratie.rubriceren.rubriceerRegels;

import com.vdzon.administratie.dto.AfschriftDto;
import com.vdzon.administratie.model.Afschrift;
import com.vdzon.administratie.model.BoekingType;
import com.vdzon.administratie.model.Factuur;
import com.vdzon.administratie.model.Gebruiker;
import com.vdzon.administratie.rubriceren.model.RubriceerAction;
import com.vdzon.administratie.rubriceren.model.RubriceerRegel;

import java.util.List;

public class RubriceerFactuurRegels extends RubriceerHelper {

    //TODO: deze class kan nog steeds mooier

    @RubriceerRule
    public void updateRegels(Gebruiker gebruiker, List<RubriceerRegel> regels, Afschrift afschrift) {
        if (afschrift.getBoekingType() == null || afschrift.getBoekingType() == BoekingType.NONE) {
            if (afschrift.getBedrag() > 0) {
                RubriceerAction rubriceerAction = RubriceerAction.NONE;
                String factuurNummer = null;
                for (Factuur factuur : gebruiker.getDefaultAdministratie().getFacturen()) {
                    if ((factuur.getBedragIncBtw() == afschrift.getBedrag()) && (afschrift.getOmschrijving().contains(factuur.getFactuurNummer()))) {
                        rubriceerAction = RubriceerAction.CONNECT_EXISTING_FACTUUR;
                        factuurNummer = factuur.getFactuurNummer();
                    }
                }
                RubriceerRegel rubriceerRegel = RubriceerRegel.builder().rubriceerAction(rubriceerAction).rekeningNummer(null).faktuurNummer(factuurNummer).afschrift(new AfschriftDto(afschrift)).build();
                regels.add(rubriceerRegel);
            }
        }
    }


    @RubriceerRuleCommit
    public void processRegel(RubriceerRegel regel, Gebruiker gebruiker) {
        Afschrift afschrift = regel.getAfschrift().toAfschrift();
        switch (regel.getRubriceerAction()) {
            case CONNECT_EXISTING_FACTUUR:
                for (Factuur factuur : gebruiker.getDefaultAdministratie().getFacturen()) {
                    if (regel.getFaktuurNummer().equals(factuur.getFactuurNummer())) {
                        Factuur newFactuur = factuur.toBuilder().betaald(true).gekoppeldAfschrift(afschrift.getNummer()).build();
                        gebruiker.getDefaultAdministratie().removeFactuur(factuur.getUuid());
                        gebruiker.getDefaultAdministratie().addFactuur(newFactuur);
                        gebruiker.getDefaultAdministratie().removeAfschrift(afschrift.getNummer());
                        gebruiker.getDefaultAdministratie().addAfschrift(
                                afschrift
                                        .toBuilder()
                                        .boekingType(BoekingType.FACTUUR)
                                        .factuurNummer(factuur.getFactuurNummer())
                                        .rekeningNummer("")
                                        .build()
                        );
                    }
                }
                break;
        }
    }


}
