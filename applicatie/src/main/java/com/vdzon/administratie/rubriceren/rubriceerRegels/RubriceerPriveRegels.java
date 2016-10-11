package com.vdzon.administratie.rubriceren.rubriceerRegels;

import com.vdzon.administratie.model.Afschrift;
import com.vdzon.administratie.model.Gebruiker;
import com.vdzon.administratie.model.boekingen.PriveBetalingBoeking;
import com.vdzon.administratie.rubriceren.model.RubriceerRegel;

import java.util.UUID;

public class RubriceerPriveRegels extends RubriceerHelper {


    @RubriceerRuleCommit
    public void processRegel(RubriceerRegel regel, Gebruiker gebruiker) {
        Afschrift afschrift = regel.getAfschrift().toAfschrift();
        switch (regel.getRubriceerAction()) {
            case PRIVE:
                PriveBetalingBoeking priveBetalingBoeking = PriveBetalingBoeking.builder()
                        .uuid(UUID.randomUUID().toString())
                        .afschriftNummer(regel.getAfschrift().getNummer())
                        .build();
                gebruiker.getDefaultAdministratie().addBoeking(priveBetalingBoeking);
                break;
        }
    }


}
