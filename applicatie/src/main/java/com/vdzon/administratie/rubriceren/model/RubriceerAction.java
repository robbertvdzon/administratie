package com.vdzon.administratie.rubriceren.model;

public enum RubriceerAction {
    NONE(0), CREATE_REKENING(1), CONNECT_EXISTING_FACTUUR(2), CONNECT_EXISTING_REKENING(3);

    int action = 0;

    RubriceerAction(int action) {
        this.action = action;
    }
}
