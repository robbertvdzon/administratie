package com.vdzon.administratie.model;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import java.time.LocalDate;

@Entity("declaratie")
public class Declaratie {

    @Id
    private String uuid;
    private String declaratieNummer;
    private String omschrijving;
    private LocalDate declaratieDate;
    private double bedragExBtw = 0;
    private double bedragIncBtw = 0;
    private double btw = 0;

    public Declaratie() {
    }

    private Declaratie(Builder builder) {
        uuid = builder.uuid;
        declaratieNummer = builder.declaratieNummer;
        omschrijving = builder.omschrijving;
        declaratieDate = builder.declaratieDate;
        bedragExBtw = builder.bedragExBtw;
        bedragIncBtw = builder.bedragIncBtw;
        btw = builder.btw;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static Builder newBuilder(Declaratie copy) {
        Builder builder = new Builder();
        builder.uuid = copy.uuid;
        builder.declaratieNummer = copy.declaratieNummer;
        builder.omschrijving = copy.omschrijving;
        builder.declaratieDate = copy.declaratieDate;
        builder.bedragExBtw = copy.bedragExBtw;
        builder.bedragIncBtw = copy.bedragIncBtw;
        builder.btw = copy.btw;
        return builder;
    }

    public String getUuid() {
        return uuid;
    }

    public String getDeclaratieNummer() {
        return declaratieNummer;
    }

    public String getOmschrijving() {
        return omschrijving;
    }

    public LocalDate getDeclaratieDate() {
        return declaratieDate;
    }

    public double getBedragExBtw() {
        return bedragExBtw;
    }

    public double getBedragIncBtw() {
        return bedragIncBtw;
    }

    public double getBtw() {
        return btw;
    }

    public static final class Builder {
        private String uuid;
        private String declaratieNummer;
        private String omschrijving;
        private LocalDate declaratieDate;
        private double bedragExBtw;
        private double bedragIncBtw;
        private double btw;

        private Builder() {
        }

        public Builder uuid(String val) {
            uuid = val;
            return this;
        }

        public Builder declaratieNummer(String val) {
            declaratieNummer = val;
            return this;
        }

        public Builder omschrijving(String val) {
            omschrijving = val;
            return this;
        }

        public Builder declaratieDate(LocalDate val) {
            declaratieDate = val;
            return this;
        }

        public Builder bedragExBtw(double val) {
            bedragExBtw = val;
            return this;
        }

        public Builder bedragIncBtw(double val) {
            bedragIncBtw = val;
            return this;
        }

        public Builder btw(double val) {
            btw = val;
            return this;
        }

        public Declaratie build() {
            return new Declaratie(this);
        }
    }
}
