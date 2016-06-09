package com.vdzon.administratie.checkandfix.model;

import com.vdzon.administratie.dto.AfschriftDto;

public class CheckAndFixRegel {
    private FixAction rubriceerAction;
    private CheckType checkType;
    private AfschriftDto afschrift;

    public CheckAndFixRegel() {
    }

    public CheckAndFixRegel(FixAction rubriceerAction, CheckType checkType, AfschriftDto afschrift) {
        this.rubriceerAction = rubriceerAction;
        this.checkType = checkType;
        this.afschrift = afschrift;
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
}
