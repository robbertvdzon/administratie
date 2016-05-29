package com.vdzon.administratie.model;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import java.time.LocalDate;

@Entity("declaratie")
public class Declaratie {

    @Id
    private String uuid;
    private String declaratieNummer;
    private String omschrijving;
    private LocalDate declaratieDate;
    private double bedragExBtw = 0;
    private double bedragIncBtw = 0;
    private double btw = 0;

    public Declaratie() {
    }

    public Declaratie(String uuid, String declaratieNummer, String omschrijving, LocalDate declaratieDate, double bedragExBtw, double bedragIncBtw, double btw) {
        this.uuid = uuid;
        this.declaratieNummer = declaratieNummer;
        this.omschrijving = omschrijving;
        this.declaratieDate = declaratieDate;
        this.bedragExBtw = bedragExBtw;
        this.bedragIncBtw = bedragIncBtw;
        this.btw = btw;
    }

    public String getUuid() {
        return uuid;
    }

    public String getDeclaratieNummer() {
        return declaratieNummer;
    }

    public String getOmschrijving() {
        return omschrijving;
    }

    public LocalDate getDeclaratieDate() {
        return declaratieDate;
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
}
