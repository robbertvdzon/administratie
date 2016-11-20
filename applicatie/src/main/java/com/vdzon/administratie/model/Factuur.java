package com.vdzon.administratie.model;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity("factuur")
public class Factuur {

    @Id
    private String uuid;
    private String factuurNummer;
    private String gekoppeldeBestellingNummer;
    private LocalDate factuurDate;
    private Contact contact;
    private List<FactuurRegel> factuurRegels = new ArrayList<>();
    private double bedragExBtw = 0;
    private double bedragIncBtw = 0;
    private double btw = 0;

    public Factuur() {
    }

    private Factuur(Builder builder) {
        uuid = builder.uuid;
        factuurNummer = builder.factuurNummer;
        gekoppeldeBestellingNummer = builder.gekoppeldeBestellingNummer;
        factuurDate = builder.factuurDate;
        contact = builder.contact;
        factuurRegels = builder.factuurRegels;
        calculate();
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static Builder newBuilder(Factuur copy) {
        Builder builder = new Builder();
        builder.uuid = copy.uuid;
        builder.factuurNummer = copy.factuurNummer;
        builder.gekoppeldeBestellingNummer = copy.gekoppeldeBestellingNummer;
        builder.factuurDate = copy.factuurDate;
        builder.contact = copy.contact;
        builder.factuurRegels = copy.factuurRegels;
        builder.bedragExBtw = copy.bedragExBtw;
        builder.bedragIncBtw = copy.bedragIncBtw;
        builder.btw = copy.btw;
        return builder;
    }

    public String uuid() {
        return uuid;
    }

    public String factuurNummer() {
        return factuurNummer;
    }

    public String gekoppeldeBestellingNummer() {
        return gekoppeldeBestellingNummer;
    }

    public LocalDate factuurDate() {
        return factuurDate;
    }

    public Contact contact() {
        return contact;
    }

    public List<FactuurRegel> factuurRegels() {
        return factuurRegels;
    }

    public double bedragExBtw() {
        return bedragExBtw;
    }

    public double bedragIncBtw() {
        return bedragIncBtw;
    }

    public double btw() {
        return btw;
    }

    /*
         * Use a custom all-arg constructor. This because we want to call calculate at the end of the constructor
         */
    public Factuur(String uuid, String factuurNummer, String gekoppeldeBestellingNummer, LocalDate factuurDate, Contact contact, List<FactuurRegel> factuurRegels) {
        this.uuid = uuid;
        this.factuurNummer = factuurNummer;
        this.gekoppeldeBestellingNummer = gekoppeldeBestellingNummer;
        this.factuurDate = factuurDate;
        this.contact = contact;
        this.factuurRegels = factuurRegels;
        calculate();
    }

    private void calculate() {
        bedragExBtw = 0;
        bedragIncBtw = 0;
        btw = 0;
        if (factuurRegels != null) {
            for (FactuurRegel factuurRegel : factuurRegels) {
                double regelBedragEx = factuurRegel.getStuksPrijs() * factuurRegel.getAantal();
                double regelBedragBtw = round(regelBedragEx * (factuurRegel.getBtwPercentage() / 100), 2);
                double regelBedragInc = regelBedragEx + regelBedragBtw;

                bedragExBtw += regelBedragEx;
                btw += regelBedragBtw;
                bedragIncBtw += regelBedragInc;
            }
        }
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }


    public static final class Builder {
        private String uuid;
        private String factuurNummer;
        private String gekoppeldeBestellingNummer;
        private LocalDate factuurDate;
        private Contact contact;
        private List<FactuurRegel> factuurRegels;
        private double bedragExBtw;
        private double bedragIncBtw;
        private double btw;

        private Builder() {
        }

        public Builder uuid(String val) {
            uuid = val;
            return this;
        }

        public Builder factuurNummer(String val) {
            factuurNummer = val;
            return this;
        }

        public Builder gekoppeldeBestellingNummer(String val) {
            gekoppeldeBestellingNummer = val;
            return this;
        }

        public Builder factuurDate(LocalDate val) {
            factuurDate = val;
            return this;
        }

        public Builder contact(Contact val) {
            contact = val;
            return this;
        }

        public Builder factuurRegels(List<FactuurRegel> val) {
            factuurRegels = val;
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

        public Factuur build() {
            return new Factuur(this);
        }
    }
}
