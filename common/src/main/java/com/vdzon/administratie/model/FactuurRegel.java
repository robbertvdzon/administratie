package com.vdzon.administratie.model;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import java.math.BigDecimal;

@Entity("factuurRegel")
public class FactuurRegel {

    @Id
    private String uuid;
    private String omschrijving;
    private BigDecimal aantal;
    private BigDecimal stuksPrijs;
    private BigDecimal btwPercentage;

    public FactuurRegel() {
    }

    private FactuurRegel(Builder builder) {
        uuid = builder.uuid;
        omschrijving = builder.omschrijving;
        aantal = builder.aantal;
        stuksPrijs = builder.stuksPrijs;
        btwPercentage = builder.btwPercentage;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static Builder newBuilder(FactuurRegel copy) {
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

    public BigDecimal getAantal() {
        return aantal;
    }

    public BigDecimal getStuksPrijs() {
        return stuksPrijs;
    }

    public BigDecimal getBtwPercentage() {
        return btwPercentage;
    }

    public static final class Builder {
        private String uuid;
        private String omschrijving;
        private BigDecimal aantal;
        private BigDecimal stuksPrijs;
        private BigDecimal btwPercentage;

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

        public Builder aantal(BigDecimal val) {
            aantal = val;
            return this;
        }

        public Builder stuksPrijs(BigDecimal val) {
            stuksPrijs = val;
            return this;
        }

        public Builder btwPercentage(BigDecimal val) {
            btwPercentage = val;
            return this;
        }

        public FactuurRegel build() {
            return new FactuurRegel(this);
        }
    }
}
