package com.vdzon.administratie.checkandfix.model;

import com.vdzon.administratie.dto.AfschriftDto;

public class CheckAndFixRegel {
    private FixAction rubriceerAction;
    private String omschrijving;
    private String data;
    private CheckType checkType;
    private AfschriftDto afschrift;

    public CheckAndFixRegel() {
    }

    public CheckAndFixRegel(FixAction rubriceerAction, CheckType checkType, AfschriftDto afschrift, String omschrijving, String data) {
        this.rubriceerAction = rubriceerAction;
        this.checkType = checkType;
        this.afschrift = afschrift;
        this.omschrijving = omschrijving;
        this.data = data;
    }

    public FixAction getRubriceerAction() {
        return rubriceerAction;
    }

    public AfschriftDto getAfschrift() {
        return afschrift;
    }

    public CheckType getCheckType() {
        return checkType;
    }

    public String getOmschrijving() {
        return omschrijving;
    }

    public String getData() {
        return data;
    }
}
