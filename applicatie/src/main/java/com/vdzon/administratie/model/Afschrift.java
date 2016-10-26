package com.vdzon.administratie.model;


import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import java.time.LocalDate;

@Entity("afschrift")
public class Afschrift {

    @Id
    private String uuid;
    private String nummer;
    private String rekening;
    private String omschrijving;
    private String relatienaam;
    private LocalDate boekdatum;
    private double bedrag;

    public Afschrift() {
    }

    private Afschrift(Builder builder) {
        uuid = builder.uuid;
        nummer = builder.nummer;
        rekening = builder.rekening;
        omschrijving = builder.omschrijving;
        relatienaam = builder.relatienaam;
        boekdatum = builder.boekdatum;
        bedrag = builder.bedrag;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static Builder newBuilder(Afschrift copy) {
        Builder builder = new Builder();
        builder.uuid = copy.uuid;
        builder.nummer = copy.nummer;
        builder.rekening = copy.rekening;
        builder.omschrijving = copy.omschrijving;
        builder.relatienaam = copy.relatienaam;
        builder.boekdatum = copy.boekdatum;
        builder.bedrag = copy.bedrag;
        return builder;
    }

    public String getUuid() {
        return uuid;
    }

    public String getNummer() {
        return nummer;
    }

    public String getRekening() {
        return rekening;
    }

    public String getOmschrijving() {
        return omschrijving;
    }

    public String getRelatienaam() {
        return relatienaam;
    }

    public LocalDate getBoekdatum() {
        return boekdatum;
    }

    public double getBedrag() {
        return bedrag;
    }

    public static final class Builder {
        private String uuid;
        private String nummer;
        private String rekening;
        private String omschrijving;
        private String relatienaam;
        private LocalDate boekdatum;
        private double bedrag;

        private Builder() {
        }

        public Builder uuid(String val) {
            uuid = val;
            return this;
        }

        public Builder nummer(String val) {
            nummer = val;
            return this;
        }

        public Builder rekening(String val) {
            rekening = val;
            return this;
        }

        public Builder omschrijving(String val) {
            omschrijving = val;
            return this;
        }

        public Builder relatienaam(String val) {
            relatienaam = val;
            return this;
        }

        public Builder boekdatum(LocalDate val) {
            boekdatum = val;
            return this;
        }

        public Builder bedrag(double val) {
            bedrag = val;
            return this;
        }

        public Afschrift build() {
            return new Afschrift(this);
        }
    }
}
