package com.vdzon.administratie.model;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import java.time.LocalDate;

@Entity("afschrift")
public class Afschrift {

    @Id
    private String uuid;
    private String rekening;
    private String rekeningnaam;
    private String relatienaam;
    private LocalDate boekdatum;
    private double bedragBij = 0;
    private double bedragAf = 0;


    public Afschrift() {
    }

    public Afschrift(String uuid, String rekening, String rekeningnaam, String relatienaam, LocalDate boekdatum, double bedragBij, double bedragAf) {
        this.uuid = uuid;
        this.rekening = rekening;
        this.rekeningnaam = rekeningnaam;
        this.relatienaam = relatienaam;
        this.boekdatum = boekdatum;
        this.bedragBij = bedragBij;
        this.bedragAf = bedragAf;
    }

    public String getUuid() {
        return uuid;
    }

    public String getRekening() {
        return rekening;
    }

    public String getRekeningnaam() {
        return rekeningnaam;
    }

    public String getRelatienaam() {
        return relatienaam;
    }

    public LocalDate getBoekdatum() {
        return boekdatum;
    }

    public double getBedragBij() {
        return bedragBij;
    }

    public double getBedragAf() {
        return bedragAf;
    }
}
