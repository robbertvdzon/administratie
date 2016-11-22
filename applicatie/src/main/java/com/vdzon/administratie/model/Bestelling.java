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
public class Bestelling {

    @Id
    private String uuid;
    private String bestellingNummer;
    private String gekoppeldFactuurNummer;
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate bestellingDate;
    private Contact contact;
    private List<BestellingRegel> bestellingRegels = new ArrayList<>();
    private BigDecimal bedragExBtw = BigDecimal.ZERO;
    private BigDecimal bedragIncBtw = BigDecimal.ZERO;
    private BigDecimal btw = BigDecimal.ZERO;

    public Bestelling() {
    }

    private Bestelling(Builder builder) {
        uuid = builder.uuid;
        bestellingNummer = builder.bestellingNummer;
        gekoppeldFactuurNummer = builder.gekoppeldFactuurNummer;
        bestellingDate = builder.bestellingDate;
        contact = builder.contact;
        bestellingRegels = builder.bestellingRegels;
        bedragExBtw = builder.bedragExBtw;
        bedragIncBtw = builder.bedragIncBtw;
        btw = builder.btw;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static Builder newBuilder(Bestelling copy) {
        Builder builder = new Builder();
        builder.uuid = copy.uuid;
        builder.bestellingNummer = copy.bestellingNummer;
        builder.gekoppeldFactuurNummer = copy.gekoppeldFactuurNummer;
        builder.bestellingDate = copy.bestellingDate;
        builder.contact = copy.contact;
        builder.bestellingRegels = copy.bestellingRegels;
        builder.bedragExBtw = copy.bedragExBtw;
        builder.bedragIncBtw = copy.bedragIncBtw;
        builder.btw = copy.btw;
        return builder;
    }

    public List<BestellingRegel> getBestellingRegels() {
        return Collections.unmodifiableList(new ArrayList<>(bestellingRegels));
    }

    public String getUuid() {
        return uuid;
    }

    public String getBestellingNummer() {
        return bestellingNummer;
    }

    public String getGekoppeldFactuurNummer() {
        return gekoppeldFactuurNummer;
    }

    public LocalDate getBestellingDate() {
        return bestellingDate;
    }

    public Contact getContact() {
        return contact;
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
    Bestelling(String uuid, String bestellingNummer, String gekoppeldFactuurNummer, LocalDate bestellingDate, Contact contact, List<BestellingRegel> bestellingRegels, BigDecimal bedragExBtw, BigDecimal bedragIncBtw, BigDecimal btw) {
        this.uuid = uuid;
        this.bestellingNummer = bestellingNummer;
        this.gekoppeldFactuurNummer = gekoppeldFactuurNummer;
        this.bestellingDate = bestellingDate;
        this.contact = contact;
        this.bestellingRegels = bestellingRegels;
        this.bedragExBtw = bedragExBtw;
        this.bedragIncBtw = bedragIncBtw;
        this.btw = btw;
        calculate();
    }


    private void calculate() {
        bedragExBtw = BigDecimal.ZERO;
        bedragIncBtw = BigDecimal.ZERO;
        btw = BigDecimal.ZERO;
        for (BestellingRegel bestellingRegel : bestellingRegels) {
            BigDecimal regelBedragEx = bestellingRegel.getStuksPrijs().multiply(bestellingRegel.getAantal());
            BigDecimal regelBedragBtw = regelBedragEx.multiply(bestellingRegel.getBtwPercentage().divide(BigDecimal.valueOf(100)));
            regelBedragBtw = regelBedragBtw.setScale(2, RoundingMode.HALF_UP);
            BigDecimal regelBedragInc = regelBedragEx.add(regelBedragBtw);

            bedragExBtw = bedragExBtw.add(regelBedragEx);
            btw = btw.add(regelBedragBtw);
            bedragIncBtw = bedragIncBtw.add(regelBedragInc);
        }
    }

    public static final class Builder {
        private String uuid;
        private String bestellingNummer;
        private String gekoppeldFactuurNummer;
        private LocalDate bestellingDate;
        private Contact contact;
        private List<BestellingRegel> bestellingRegels;
        private BigDecimal bedragExBtw;
        private BigDecimal bedragIncBtw;
        private BigDecimal btw;

        private Builder() {
        }

        public Builder uuid(String val) {
            uuid = val;
            return this;
        }

        public Builder bestellingNummer(String val) {
            bestellingNummer = val;
            return this;
        }

        public Builder gekoppeldFactuurNummer(String val) {
            gekoppeldFactuurNummer = val;
            return this;
        }

        public Builder bestellingDate(LocalDate val) {
            bestellingDate = val;
            return this;
        }

        public Builder contact(Contact val) {
            contact = val;
            return this;
        }

        public Builder bestellingRegels(List<BestellingRegel> val) {
            bestellingRegels = val;
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

        public Bestelling build() {
            return new Bestelling(this);
        }
    }
}
