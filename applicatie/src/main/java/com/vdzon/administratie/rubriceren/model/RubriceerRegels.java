package com.vdzon.administratie.rubriceren.model;

import java.util.List;

public class RubriceerRegels {
    private List<RubriceerRegel> rubriceerRegelList;

    public RubriceerRegels() {
    }

    public RubriceerRegels(List<RubriceerRegel> rubriceerRegelList) {
        this.rubriceerRegelList = rubriceerRegelList;
    }

    public List<RubriceerRegel> getRubriceerRegelList() {
        return rubriceerRegelList;
    }
}
