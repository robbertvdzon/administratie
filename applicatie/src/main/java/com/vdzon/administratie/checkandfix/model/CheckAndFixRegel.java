package com.vdzon.administratie.checkandfix.model;

import com.vdzon.administratie.dto.AfschriftDto;
import java.time.LocalDate;

public class CheckAndFixRegel {
    private FixAction rubriceerAction;
    private String omschrijving;
    private String data;
    private String boekingUuid;
    private CheckType checkType;
    private AfschriftDto afschrift;
    private LocalDate date;

    public FixAction getRubriceerAction() {
        return rubriceerAction;
    }

    public String getOmschrijving() {
        return omschrijving;
    }

    public String getData() {
        return data;
    }

    public String getBoekingUuid() {
        return boekingUuid;
    }

    public CheckType getCheckType() {
        return checkType;
    }

    public AfschriftDto getAfschrift() {
        return afschrift;
    }

    public LocalDate getDate() {
        return date;
    }

    private CheckAndFixRegel(Builder builder) {
        rubriceerAction = builder.rubriceerAction;
        omschrijving = builder.omschrijving;
        data = builder.data;
        boekingUuid = builder.boekingUuid;
        checkType = builder.checkType;
        afschrift = builder.afschrift;
        date = builder.date;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static Builder newBuilder(CheckAndFixRegel copy) {
        Builder builder = new Builder();
        builder.rubriceerAction = copy.rubriceerAction;
        builder.omschrijving = copy.omschrijving;
        builder.data = copy.data;
        builder.boekingUuid = copy.boekingUuid;
        builder.checkType = copy.checkType;
        builder.afschrift = copy.afschrift;
        builder.date = copy.date;
        return builder;
    }

    public static final class Builder {
        private FixAction rubriceerAction;
        private String omschrijving;
        private String data;
        private String boekingUuid;
        private CheckType checkType;
        private AfschriftDto afschrift;
        private LocalDate date;

        private Builder() {
        }

        public Builder rubriceerAction(FixAction val) {
            rubriceerAction = val;
            return this;
        }

        public Builder omschrijving(String val) {
            omschrijving = val;
            return this;
        }

        public Builder data(String val) {
            data = val;
            return this;
        }

        public Builder boekingUuid(String val) {
            boekingUuid = val;
            return this;
        }

        public Builder checkType(CheckType val) {
            checkType = val;
            return this;
        }

        public Builder afschrift(AfschriftDto val) {
            afschrift = val;
            return this;
        }

        public Builder date(LocalDate val) {
            date = val;
            return this;
        }

        public CheckAndFixRegel build() {
            return new CheckAndFixRegel(this);
        }
    }
}
