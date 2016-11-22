package com.vdzon.administratie.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate factuurDate;
    private Contact contact;
    private List<FactuurRegel> factuurRegels = new ArrayList<>();
    private BigDecimal bedragExBtw = BigDecimal.ZERO;
    private BigDecimal bedragIncBtw = BigDecimal.ZERO;
    private BigDecimal btw = BigDecimal.ZERO;

    public Factuur() {
    }

    private Factuur(Builder builder) {
        uuid = builder.uuid;
        factuurNummer = builder.factuurNummer;
        gekoppeldeBestellingNummer = builder.gekoppeldeBestellingNummer;
        factuurDate = builder.factuurDate;
        contact = builder.contact;
        factuurRegels = builder.factuurRegels;
        bedragExBtw = builder.bedragExBtw;
        bedragIncBtw = builder.bedragIncBtw;
        btw = builder.btw;
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

    public String getUuid() {
        return uuid;
    }

    public String getFactuurNummer() {
        return factuurNummer;
    }

    public String getGekoppeldeBestellingNummer() {
        return gekoppeldeBestellingNummer;
    }

    public LocalDate getFactuurDate() {
        return factuurDate;
    }

    public Contact getContact() {
        return contact;
    }

    public List<FactuurRegel> getFactuurRegels() {
        return factuurRegels;
    }

    public BigDecimal getBedragExBtw() {
        return bedragExBtw;
    }

    public BigDecimal getBedragIncBtw() {
        return bedragIncBtw;
    }

    public BigDecimal getBtw() {
        return btw;
    }

    /*
         * Use a custom all-arg constructor. This because we want to call calculate at the end of the constructor
         */
    public Factuur(String uuid, String factuurNummer, String gekoppeldeBestellingNummer, LocalDate factuurDate, Contact contact, List<FactuurRegel> factuurRegels, BigDecimal bedragExBtw, BigDecimal bedragIncBtw, BigDecimal btw) {
        this.uuid = uuid;
        this.factuurNummer = factuurNummer;
        this.gekoppeldeBestellingNummer = gekoppeldeBestellingNummer;
        this.factuurDate = factuurDate;
        this.contact = contact;
        this.factuurRegels = factuurRegels;
        this.bedragExBtw = bedragExBtw;
        this.bedragIncBtw = bedragIncBtw;
        this.btw = btw;
        calculate();
    }

    private void calculate() {
        bedragExBtw = BigDecimal.ZERO;
        bedragIncBtw = BigDecimal.ZERO;
        btw = BigDecimal.ZERO;
        if (factuurRegels != null) {
            for (FactuurRegel factuurRegel : factuurRegels) {
                BigDecimal regelBedragEx = factuurRegel.getStuksPrijs().multiply(factuurRegel.getAantal());
                BigDecimal regelBedragBtw = regelBedragEx.multiply(factuurRegel.getBtwPercentage().divide(BigDecimal.valueOf(100)));
                regelBedragBtw = regelBedragBtw.setScale(2, RoundingMode.HALF_UP);
                BigDecimal regelBedragInc = regelBedragEx.add(regelBedragBtw);

                bedragExBtw = bedragExBtw.add(regelBedragEx);
                btw = btw.add(regelBedragBtw);
                bedragIncBtw = bedragIncBtw.add(regelBedragInc);
            }
        }
//
//        bedragIncBtw = bedragIncBtw.setScale(2, RoundingMode.HALF_UP);
//        System.out.println(bedragIncBtw);
    }



    public static final class Builder {
        private String uuid;
        private String factuurNummer;
        private String gekoppeldeBestellingNummer;
        private LocalDate factuurDate;
        private Contact contact;
        private List<FactuurRegel> factuurRegels;
        private BigDecimal bedragExBtw;
        private BigDecimal bedragIncBtw;
        private BigDecimal btw;

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

        public Builder bedragExBtw(BigDecimal val) {
            bedragExBtw = val;
            return this;
        }

        public Builder bedragIncBtw(BigDecimal val) {
            bedragIncBtw = val;
            return this;
        }

        public Builder btw(BigDecimal val) {
            btw = val;
            return this;
        }

        public Factuur build() {
            return new Factuur(this);
        }
    }
}
