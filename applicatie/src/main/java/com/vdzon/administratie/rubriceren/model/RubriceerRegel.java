package com.vdzon.administratie.rubriceren.model;

public class RubriceerRegel {
    private RubriceerAction rubriceerAction;
    private String afschiftUuid;
    private String rekeningNummer;
    private String faktuurNummer;

    public RubriceerRegel() {
    }

    public RubriceerRegel(RubriceerAction rubriceerAction, String afschiftUuid, String rekeningNummer, String faktuurNummer) {
        this.rubriceerAction = rubriceerAction;
        this.afschiftUuid = afschiftUuid;
        this.rekeningNummer = rekeningNummer;
        this.faktuurNummer = faktuurNummer;
    }

    public RubriceerAction getRubriceerAction() {
        return rubriceerAction;
    }

    public String getAfschiftUuid() {
        return afschiftUuid;
    }

    public String getRekeningNummer() {
        return rekeningNummer;
    }

    public String getFaktuurNummer() {
        return faktuurNummer;
    }

    @Override
    public String toString() {
        return "RubriceerRegel{" +
                "rubriceerAction=" + rubriceerAction +
                ", afschiftUuid='" + afschiftUuid + '\'' +
                ", rekeningNummer='" + rekeningNummer + '\'' +
                ", faktuurNummer='" + faktuurNummer + '\'' +
                '}';
    }
}
