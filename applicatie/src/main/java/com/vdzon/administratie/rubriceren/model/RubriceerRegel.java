package com.vdzon.administratie.rubriceren.model;

import com.vdzon.administratie.dto.AfschriftDto;

public class RubriceerRegel {
    private RubriceerAction rubriceerAction;
    private String rekeningNummer;
    private String faktuurNummer;
    private AfschriftDto afschrift;

    public RubriceerRegel() {
    }

    private RubriceerRegel(Builder builder) {
        rubriceerAction = builder.rubriceerAction;
        rekeningNummer = builder.rekeningNummer;
        faktuurNummer = builder.faktuurNummer;
        afschrift = builder.afschrift;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static Builder newBuilder(RubriceerRegel copy) {
        Builder builder = new Builder();
        builder.rubriceerAction = copy.rubriceerAction;
        builder.rekeningNummer = copy.rekeningNummer;
        builder.faktuurNummer = copy.faktuurNummer;
        builder.afschrift = copy.afschrift;
        return builder;
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

    public static final class Builder {
        private RubriceerAction rubriceerAction;
        private String rekeningNummer;
        private String faktuurNummer;
        private AfschriftDto afschrift;

        private Builder() {
        }

        public Builder rubriceerAction(RubriceerAction val) {
            rubriceerAction = val;
            return this;
        }

        public Builder rekeningNummer(String val) {
            rekeningNummer = val;
            return this;
        }

        public Builder faktuurNummer(String val) {
            faktuurNummer = val;
            return this;
        }

        public Builder afschrift(AfschriftDto val) {
            afschrift = val;
            return this;
        }

        public RubriceerRegel build() {
            return new RubriceerRegel(this);
        }
    }
}
