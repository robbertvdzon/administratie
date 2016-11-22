package com.vdzon.administratie.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import java.time.LocalDate;

@Entity("rekening")
public class Rekening {

    @Id
    private String uuid;
    private String rekeningNummer;
    private String factuurNummer;
    private String naam;
    private String omschrijving;
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate rekeningDate;
    private double bedragExBtw = 0;
    private double bedragIncBtw = 0;
    private double btw = 0;
    private int maandenAfschrijving = 0;

    public Rekening() {
    }

    private Rekening(Builder builder) {
        uuid = builder.uuid;
        rekeningNummer = builder.rekeningNummer;
        factuurNummer = builder.factuurNummer;
        naam = builder.naam;
        omschrijving = builder.omschrijving;
        rekeningDate = builder.rekeningDate;
        bedragExBtw = builder.bedragExBtw;
        bedragIncBtw = builder.bedragIncBtw;
        btw = builder.btw;
        maandenAfschrijving = builder.maandenAfschrijving;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static Builder newBuilder(Rekening copy) {
        Builder builder = new Builder();
        builder.uuid = copy.uuid;
        builder.rekeningNummer = copy.rekeningNummer;
        builder.factuurNummer = copy.factuurNummer;
        builder.naam = copy.naam;
        builder.omschrijving = copy.omschrijving;
        builder.rekeningDate = copy.rekeningDate;
        builder.bedragExBtw = copy.bedragExBtw;
        builder.bedragIncBtw = copy.bedragIncBtw;
        builder.btw = copy.btw;
        builder.maandenAfschrijving = copy.maandenAfschrijving;
        return builder;
    }

    public String getUuid() {
        return uuid;
    }

    public String getRekeningNummer() {
        return rekeningNummer;
    }

    public String getFactuurNummer() {
        return factuurNummer;
    }

    public String getNaam() {
        return naam;
    }

    public String getOmschrijving() {
        return omschrijving;
    }

    public LocalDate getRekeningDate() {
        return rekeningDate;
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

    public int getMaandenAfschrijving() {
        return maandenAfschrijving;
    }

    public static final class Builder {
        private String uuid;
        private String rekeningNummer;
        private String factuurNummer;
        private String naam;
        private String omschrijving;
        private LocalDate rekeningDate;
        private double bedragExBtw;
        private double bedragIncBtw;
        private double btw;
        private int maandenAfschrijving;

        private Builder() {
        }

        public Builder uuid(String val) {
            uuid = val;
            return this;
        }

        public Builder rekeningNummer(String val) {
            rekeningNummer = val;
            return this;
        }

        public Builder factuurNummer(String val) {
            factuurNummer = val;
            return this;
        }

        public Builder naam(String val) {
            naam = val;
            return this;
        }

        public Builder omschrijving(String val) {
            omschrijving = val;
            return this;
        }

        public Builder rekeningDate(LocalDate val) {
            rekeningDate = val;
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

        public Builder maandenAfschrijving(int val) {
            maandenAfschrijving = val;
            return this;
        }

        public Rekening build() {
            return new Rekening(this);
        }
    }
}
