package com.vdzon.administratie.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.vdzon.administratie.model.boekingen.Boeking;
import com.vdzon.administratie.model.boekingen.relaties.BoekingMetAfschrift;
import com.vdzon.administratie.model.boekingen.relaties.BoekingMetFactuur;
import com.vdzon.administratie.model.boekingen.relaties.BoekingMetRekening;

@JsonIgnoreProperties
public class BoekingDto {

    private String uuid;
    private String omschrijving;
    private String factuurNummer;
    private String rekeningNummer;
    private String afschriftNummer;
    private String declaratieNummer;

    public BoekingDto() {
    }

    private BoekingDto(Builder builder) {
        uuid = builder.uuid;
        omschrijving = builder.omschrijving;
        factuurNummer = builder.factuurNummer;
        rekeningNummer = builder.rekeningNummer;
        afschriftNummer = builder.afschriftNummer;
        declaratieNummer = builder.declaratieNummer;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static Builder newBuilder(BoekingDto copy) {
        Builder builder = new Builder();
        builder.uuid = copy.uuid;
        builder.omschrijving = copy.omschrijving;
        builder.factuurNummer = copy.factuurNummer;
        builder.rekeningNummer = copy.rekeningNummer;
        builder.afschriftNummer = copy.afschriftNummer;
        builder.declaratieNummer = copy.declaratieNummer;
        return builder;
    }

    public String getUuid() {
        return uuid;
    }

    public String getOmschrijving() {
        return omschrijving;
    }

    public String getFactuurNummer() {
        return factuurNummer;
    }

    public String getRekeningNummer() {
        return rekeningNummer;
    }

    public String getAfschriftNummer() {
        return afschriftNummer;
    }

    public String getDeclaratieNummer() {
        return declaratieNummer;
    }

    public BoekingDto(Boeking boeking) {
        this.omschrijving = boeking.getOmschrijving();
        this.uuid= boeking.getUuid();
        if (boeking instanceof BoekingMetAfschrift){
            this.afschriftNummer = ((BoekingMetAfschrift) boeking).getAfschriftNummer();
        }
        if (boeking instanceof BoekingMetFactuur){
            this.factuurNummer = ((BoekingMetFactuur) boeking).getFactuurNummer();
        }
        if (boeking instanceof BoekingMetRekening){
            this.rekeningNummer = ((BoekingMetRekening) boeking).getRekeningNummer();
        }
    }

    public static final class Builder {
        private String uuid;
        private String omschrijving;
        private String factuurNummer;
        private String rekeningNummer;
        private String afschriftNummer;
        private String declaratieNummer;

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

        public Builder factuurNummer(String val) {
            factuurNummer = val;
            return this;
        }

        public Builder rekeningNummer(String val) {
            rekeningNummer = val;
            return this;
        }

        public Builder afschriftNummer(String val) {
            afschriftNummer = val;
            return this;
        }

        public Builder declaratieNummer(String val) {
            declaratieNummer = val;
            return this;
        }

        public BoekingDto build() {
            return new BoekingDto(this);
        }
    }
}
