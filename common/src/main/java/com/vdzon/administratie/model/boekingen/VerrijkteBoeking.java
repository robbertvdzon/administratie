package com.vdzon.administratie.model.boekingen;


import com.vdzon.administratie.model.Afschrift;
import com.vdzon.administratie.model.Factuur;
import com.vdzon.administratie.model.Rekening;

import java.math.BigDecimal;
import java.time.LocalDate;

public class VerrijkteBoeking {

    private VerrijkteBoeking(Builder builder) {
        boeking = builder.boeking;
        factuur = builder.factuur;
        rekening = builder.rekening;
        afschrift = builder.afschrift;
        boekingsType = builder.boekingsType;
        boekingsBedrag = builder.boekingsBedrag;
        afschriftBedrag = builder.afschriftBedrag;
        factuurBedrag = builder.factuurBedrag;
        rekeningBedrag = builder.rekeningBedrag;
        afschriftDate = builder.afschriftDate;
        factuurDate = builder.factuurDate;
        rekeningDate = builder.rekeningDate;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static Builder newBuilder(VerrijkteBoeking copy) {
        Builder builder = new Builder();
        builder.boeking = copy.boeking;
        builder.factuur = copy.factuur;
        builder.rekening = copy.rekening;
        builder.afschrift = copy.afschrift;
        builder.boekingsType = copy.boekingsType;
        builder.boekingsBedrag = copy.boekingsBedrag;
        builder.afschriftBedrag = copy.afschriftBedrag;
        builder.factuurBedrag = copy.factuurBedrag;
        builder.rekeningBedrag = copy.rekeningBedrag;
        builder.afschriftDate = copy.afschriftDate;
        builder.factuurDate = copy.factuurDate;
        builder.rekeningDate = copy.rekeningDate;
        return builder;
    }

    public enum BOEKINGSTYPE {UNKNOWN, BETAALDE_FACTUUR, BETAALDE_REKENING, BETALING_ZONDER_FACTUUR, INKOMSTEN_ZONDER_FACTUUR, PRIVE_BETALING};

    private Boeking boeking;
    private Factuur factuur;
    private Rekening rekening;
    private Afschrift afschrift;
    private BOEKINGSTYPE boekingsType;
    private BigDecimal boekingsBedrag;
    private BigDecimal afschriftBedrag;
    private BigDecimal factuurBedrag;
    private BigDecimal rekeningBedrag;
    private LocalDate afschriftDate;
    private LocalDate factuurDate;
    private LocalDate rekeningDate;

    public Boeking getBoeking() {
        return boeking;
    }

    public Factuur getFactuur() {
        return factuur;
    }

    public Rekening getRekening() {
        return rekening;
    }

    public Afschrift getAfschrift() {
        return afschrift;
    }

    public BOEKINGSTYPE getBoekingsType() {
        return boekingsType;
    }

    public BigDecimal getBoekingsBedrag() {
        return boekingsBedrag;
    }

    public BigDecimal getAfschriftBedrag() {
        return afschriftBedrag;
    }

    public BigDecimal getFactuurBedrag() {
        return factuurBedrag;
    }

    public BigDecimal getRekeningBedrag() {
        return rekeningBedrag;
    }

    public LocalDate getAfschriftDate() {
        return afschriftDate;
    }

    public LocalDate getFactuurDate() {
        return factuurDate;
    }

    public LocalDate getRekeningDate() {
        return rekeningDate;
    }

    public static final class Builder {
        private Boeking boeking;
        private Factuur factuur;
        private Rekening rekening;
        private Afschrift afschrift;
        private BOEKINGSTYPE boekingsType;
        private BigDecimal boekingsBedrag;
        private BigDecimal afschriftBedrag;
        private BigDecimal factuurBedrag;
        private BigDecimal rekeningBedrag;
        private LocalDate afschriftDate;
        private LocalDate factuurDate;
        private LocalDate rekeningDate;

        private Builder() {
        }

        public Builder boeking(Boeking val) {
            boeking = val;
            return this;
        }

        public Builder factuur(Factuur val) {
            factuur = val;
            return this;
        }

        public Builder rekening(Rekening val) {
            rekening = val;
            return this;
        }

        public Builder afschrift(Afschrift val) {
            afschrift = val;
            return this;
        }

        public Builder boekingsType(BOEKINGSTYPE val) {
            boekingsType = val;
            return this;
        }

        public Builder boekingsBedrag(BigDecimal val) {
            boekingsBedrag = val;
            return this;
        }

        public Builder afschriftBedrag(BigDecimal val) {
            afschriftBedrag = val;
            return this;
        }

        public Builder factuurBedrag(BigDecimal val) {
            factuurBedrag = val;
            return this;
        }

        public Builder rekeningBedrag(BigDecimal val) {
            rekeningBedrag = val;
            return this;
        }

        public Builder afschriftDate(LocalDate val) {
            afschriftDate = val;
            return this;
        }

        public Builder factuurDate(LocalDate val) {
            factuurDate = val;
            return this;
        }

        public Builder rekeningDate(LocalDate val) {
            rekeningDate = val;
            return this;
        }

        public VerrijkteBoeking build() {
            return new VerrijkteBoeking(this);
        }
    }
}
