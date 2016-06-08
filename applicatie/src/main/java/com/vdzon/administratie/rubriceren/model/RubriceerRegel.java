package com.vdzon.administratie.rubriceren.model;

import com.vdzon.administratie.dto.AfschriftDto;
import com.vdzon.administratie.model.Afschrift;

public class RubriceerRegel {
    private RubriceerAction rubriceerAction;
    private String rekeningNummer;
    private String faktuurNummer;
    private AfschriftDto afschrift;

    public RubriceerRegel() {
    }

    public RubriceerRegel(RubriceerAction rubriceerAction, String rekeningNummer, String faktuurNummer, AfschriftDto afschrift) {
        this.rubriceerAction = rubriceerAction;
        this.rekeningNummer = rekeningNummer;
        this.faktuurNummer = faktuurNummer;
        this.afschrift = afschrift;
    }

    public RubriceerAction getRubriceerAction() {
        return rubriceerAction;
    }

    public String getRekeningNummer() {
        return rekeningNummer;
    }

    public String getFaktuurNummer() {
        return faktuurNummer;
    }

    public AfschriftDto getAfschrift() {
        return afschrift;
    }

    @Override
    public String toString() {
        return "RubriceerRegel{" +
                "rubriceerAction=" + rubriceerAction +
                ", rekeningNummer='" + rekeningNummer + '\'' +
                ", faktuurNummer='" + faktuurNummer + '\'' +
                ", afschrift=" + afschrift +
                '}';
    }
}
