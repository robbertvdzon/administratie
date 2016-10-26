package com.vdzon.administratie.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.vdzon.administratie.model.Bestelling;
import com.vdzon.administratie.model.BestellingRegel;
import com.vdzon.administratie.model.FactuurRegel;

@JsonIgnoreProperties
public class BestellingRegelDto {

    private String uuid;
    private String omschrijving;
    private double aantal;
    private double stuksPrijs;
    private double btwPercentage;

    private BestellingRegelDto(Builder builder) {
        uuid = builder.uuid;
        omschrijving = builder.omschrijving;
        aantal = builder.aantal;
        stuksPrijs = builder.stuksPrijs;
        btwPercentage = builder.btwPercentage;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static Builder newBuilder(BestellingRegelDto copy) {
        Builder builder = new Builder();
        builder.uuid = copy.uuid;
        builder.omschrijving = copy.omschrijving;
        builder.aantal = copy.aantal;
        builder.stuksPrijs = copy.stuksPrijs;
        builder.btwPercentage = copy.btwPercentage;
        return builder;
    }

    public String getUuid() {
        return uuid;
    }

    public String getOmschrijving() {
        return omschrijving;
    }

    public double getAantal() {
        return aantal;
    }

    public double getStuksPrijs() {
        return stuksPrijs;
    }

    public double getBtwPercentage() {
        return btwPercentage;
    }

    public BestellingRegelDto(BestellingRegel bestellingRegel) {
        this.uuid = bestellingRegel.getUuid();
        this.omschrijving = bestellingRegel.getOmschrijving();
        this.aantal = bestellingRegel.getAantal();
        this.stuksPrijs = bestellingRegel.getStuksPrijs();
        this.btwPercentage = bestellingRegel.getBtwPercentage();
    }

    public BestellingRegel toBestellingRegel() {
        return BestellingRegel.newBuilder().omschrijving(omschrijving).aantal(aantal).stuksPrijs(stuksPrijs).btwPercentage(btwPercentage).uuid(uuid).build();
    }

    public static final class Builder {
        private String uuid;
        private String omschrijving;
        private double aantal;
        private double stuksPrijs;
        private double btwPercentage;

        private Builder() {
        }

        public Builder uuid(String val) {
            uuid = val;
            return this;
        }

        public Builder omschrijving(String val) {
            omschrijving = val;
            return this;
        }

        public Builder aantal(double val) {
            aantal = val;
            return this;
        }

        public Builder stuksPrijs(double val) {
            stuksPrijs = val;
            return this;
        }

        public Builder btwPercentage(double val) {
            btwPercentage = val;
            return this;
        }

        public BestellingRegelDto build() {
            return new BestellingRegelDto(this);
        }
    }
}
