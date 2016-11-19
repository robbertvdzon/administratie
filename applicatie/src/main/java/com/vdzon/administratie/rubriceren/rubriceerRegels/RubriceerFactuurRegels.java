package com.vdzon.administratie.rubriceren.rubriceerRegels;

import com.vdzon.administratie.dto.AfschriftDto;
import com.vdzon.administratie.model.Afschrift;
import com.vdzon.administratie.model.BoekingenCache;
import com.vdzon.administratie.model.Factuur;
import com.vdzon.administratie.model.Gebruiker;
import com.vdzon.administratie.model.boekingen.BetaaldeFactuurBoeking;
import com.vdzon.administratie.model.boekingen.Boeking;
import com.vdzon.administratie.model.boekingen.InkomstenZonderFactuurBoeking;
import com.vdzon.administratie.model.boekingen.relaties.BoekingMetAfschrift;
import com.vdzon.administratie.rubriceren.model.RubriceerAction;
import com.vdzon.administratie.rubriceren.model.RubriceerRegel;

import java.util.List;
import java.util.UUID;

public class RubriceerFactuurRegels extends RubriceerHelper {

    //TODO: deze class kan nog steeds mooier

    @RubriceerRule
    public void updateRegels(Gebruiker gebruiker, List<RubriceerRegel> regels, Afschrift afschrift, BoekingenCache boekingenCache) {
        List<BoekingMetAfschrift> boekingenVanAfschrift = boekingenCache.getBoekingenVanAfschrift(afschrift.nummer());
        if (boekingenVanAfschrift==null || boekingenVanAfschrift.isEmpty()) {
            if (afschrift.bedrag() > 0) {
                RubriceerAction rubriceerAction = RubriceerAction.INKOMSTEN_ZONDER_FACTUUR;
                String factuurNummer = null;
                for (Factuur factuur : gebruiker.getDefaultAdministratie().getFacturen()) {
                    String omschrijvingZonderSpaties = afschrift.omschrijving().replaceAll(" ","");
                    if ((factuur.getBedragIncBtw() == afschrift.bedrag()) && (omschrijvingZonderSpaties.contains(factuur.getFactuurNummer()))) {
                        rubriceerAction = RubriceerAction.CONNECT_EXISTING_FACTUUR;
                        factuurNummer = factuur.getFactuurNummer();
                    }
                }
                RubriceerRegel rubriceerRegel = RubriceerRegel.newBuilder().rubriceerAction(rubriceerAction).rekeningNummer(null).faktuurNummer(factuurNummer).afschrift(new AfschriftDto(afschrift, boekingenCache)).build();
                regels.add(rubriceerRegel);
            }
        }
    }


    @RubriceerRuleCommit
    public void processRegel(RubriceerRegel regel, Gebruiker gebruiker) {
        Afschrift afschrift = regel.getAfschrift().toAfschrift();
        Boeking boeking;
        switch (regel.getRubriceerAction()) {
            case CONNECT_EXISTING_FACTUUR:
                boeking = BetaaldeFactuurBoeking.newBuilder()
                        .uuid(UUID.randomUUID().toString())
                        .afschriftNummer(regel.getAfschrift().getNummer())
                        .factuurNummer(regel.getFaktuurNummer())
                        .build();
                gebruiker.getDefaultAdministratie().addBoeking(boeking);
                break;
            case INKOMSTEN_ZONDER_FACTUUR:
                boeking = InkomstenZonderFactuurBoeking.newBuilder()
                        .uuid(UUID.randomUUID().toString())
                        .afschriftNummer(regel.getAfschrift().getNummer())
                        .build();
                gebruiker.getDefaultAdministratie().addBoeking(boeking);
                break;
        }
    }


}
