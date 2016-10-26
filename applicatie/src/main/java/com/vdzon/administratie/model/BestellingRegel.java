package com.vdzon.administratie.model;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

@Entity("bestellingRegel")
public class BestellingRegel {

    @Id
    private String uuid;
    private String omschrijving;
    private double aantal;
    private double stuksPrijs;
    private double btwPercentage;

    public BestellingRegel() {
    }

    private BestellingRegel(Builder builder) {
        uuid = builder.uuid;
        omschrijving = builder.omschrijving;
        aantal = builder.aantal;
        stuksPrijs = builder.stuksPrijs;
        btwPercentage = builder.btwPercentage;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static Builder newBuilder(BestellingRegel copy) {
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

        public BestellingRegel build() {
            return new BestellingRegel(this);
        }
    }
}
