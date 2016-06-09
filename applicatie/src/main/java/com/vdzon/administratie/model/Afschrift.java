package com.vdzon.administratie.model;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import java.time.LocalDate;

@Entity("afschrift")
public class Afschrift {

    @Id
    private String uuid;
    private String nummer;
    private String rekening;
    private String omschrijving;
    private String relatienaam;
    private LocalDate boekdatum;
    private double bedrag = 0;
    private BoekingType boekingType;
    private String factuurNummer;
    private String rekeningNummer;

    public Afschrift() {
    }

    public Afschrift(String uuid, String nummer, String rekening, String omschrijving, String relatienaam, LocalDate boekdatum, double bedrag, BoekingType boekingType, String factuurNummer, String rekeningNummer) {
        this.uuid = uuid;
        this.nummer = nummer;
        this.rekening = rekening;
        this.omschrijving = omschrijving;
        this.relatienaam = relatienaam;
        this.boekdatum = boekdatum;
        this.bedrag = bedrag;
        this.boekingType = boekingType;
        this.factuurNummer = factuurNummer;
        this.rekeningNummer = rekeningNummer;
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

    public BoekingType getBoekingType() {
        return boekingType;
    }

    public String getFactuurNummer() {
        return factuurNummer;
    }

    public String getRekeningNummer() {
        return rekeningNummer;
    }

    public String getNummer() {
        return nummer;
    }
}
