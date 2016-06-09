package com.vdzon.administratie.model;

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
    private LocalDate rekeningDate;
    private double bedragExBtw = 0;
    private double bedragIncBtw = 0;
    private double btw = 0;
    private String gekoppeldAfschrift;

    public Rekening() {
    }

    public String getFactuurNummer() {
        return factuurNummer;
    }

    public Rekening(String uuid, String rekeningNummer, String factuurNummer, String naam, String omschrijving, LocalDate rekeningDate, double bedragExBtw, double bedragIncBtw, double btw, String gekoppeldAfschrift) {
        this.uuid = uuid;
        this.rekeningNummer = rekeningNummer;
        this.factuurNummer = factuurNummer;
        this.naam = naam;
        this.omschrijving = omschrijving;
        this.rekeningDate = rekeningDate;
        this.bedragExBtw = bedragExBtw;
        this.bedragIncBtw = bedragIncBtw;
        this.btw = btw;
        this.gekoppeldAfschrift = gekoppeldAfschrift;
    }

    public String getUuid() {
        return uuid;
    }

    public String getRekeningNummer() {
        return rekeningNummer;
    }

    public String getNaam() {
        return naam;
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

    public String getOmschrijving() {
        return omschrijving;
    }

    public String getGekoppeldAfschrift() {
        return gekoppeldAfschrift;
    }
}
