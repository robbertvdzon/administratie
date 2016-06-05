package com.vdzon.administratie.model;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import java.time.LocalDate;

@Entity("afschrift")
public class Afschrift {

    @Id
    private String uuid;
    private String rekening;
    private String omschrijving;
    private String relatienaam;
    private LocalDate boekdatum;
    private double bedrag = 0;


    public Afschrift() {
    }

    public Afschrift(String uuid, String rekening, String omschrijving, String relatienaam, LocalDate boekdatum, double bedrag) {
        this.uuid = uuid;
        this.rekening = rekening;
        this.omschrijving = omschrijving;
        this.relatienaam = relatienaam;
        this.boekdatum = boekdatum;
        this.bedrag = bedrag;
    }

    public String getUuid() {
        return uuid;
    }

    public String getRekening() {
        return rekening;
    }

    public String getOmschrijving() {
        return omschrijving;
    }

    public String getRelatienaam() {
        return relatienaam;
    }

    public LocalDate getBoekdatum() {
        return boekdatum;
    }

    public double getBedrag() {
        return bedrag;
    }

}
