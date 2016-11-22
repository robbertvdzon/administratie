package com.vdzon.administratie.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.vdzon.administratie.model.FactuurRegel;

import java.math.BigDecimal;

@JsonIgnoreProperties
public class FactuurRegelDto {

    private String uuid;
    private String omschrijving;
    private BigDecimal aantal;
    private BigDecimal stuksPrijs;
    private BigDecimal btwPercentage;

    public FactuurRegelDto() {
    }

    private FactuurRegelDto(Builder builder) {
        uuid = builder.uuid;
        omschrijving = builder.omschrijving;
        aantal = builder.aantal;
        stuksPrijs = builder.stuksPrijs;
        btwPercentage = builder.btwPercentage;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static Builder newBuilder(FactuurRegelDto copy) {
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

    public FactuurRegelDto(FactuurRegel factuurRegel) {
        this.uuid = factuurRegel.getUuid();
        this.omschrijving = factuurRegel.getOmschrijving();
        this.aantal = factuurRegel.getAantal();
        this.stuksPrijs = factuurRegel.getStuksPrijs();
        this.btwPercentage = factuurRegel.getBtwPercentage();
    }

    public FactuurRegel toFactuurRegel() {
        return FactuurRegel
                .newBuilder()
                .omschrijving(omschrijving)
                .aantal(aantal)
                .stuksPrijs(stuksPrijs)
                .btwPercentage(btwPercentage)
                .uuid(uuid)
                .build();
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

        public FactuurRegelDto build() {
            return new FactuurRegelDto(this);
        }
    }
}
