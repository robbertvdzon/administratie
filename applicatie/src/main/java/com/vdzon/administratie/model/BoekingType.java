package com.vdzon.administratie.model;

public enum BoekingType {
    REKENING(0), FACTUUR(1), PRIVE(2), NONE(3);

    private int type;

    BoekingType(int type) {
        this.type = type;
    }

}
