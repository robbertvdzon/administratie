package com.vdzon.administratie.model;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity("rekening")
public class Rekening {

    @Id
    private String uuid;
    private String rekeningNummer;
    private String naam;
    private String omschrijving;
    private LocalDate factuurDate;
    private double bedragExBtw = 0;
    private double bedragIncBtw = 0;
    private double btw = 0;

    public Rekening() {
    }

    public Rekening(String uuid, String rekeningNummer, String naam, String omschrijving, LocalDate factuurDate, double bedragExBtw, double bedragIncBtw, double btw) {
        this.uuid = uuid;
        this.rekeningNummer = rekeningNummer;
        this.naam = naam;
        this.omschrijving = omschrijving;
        this.factuurDate = factuurDate;
        this.bedragExBtw = bedragExBtw;
        this.bedragIncBtw = bedragIncBtw;
        this.btw = btw;
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

    public LocalDate getFactuurDate() {
        return factuurDate;
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
}
