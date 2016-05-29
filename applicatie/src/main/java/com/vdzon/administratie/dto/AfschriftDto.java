package com.vdzon.administratie.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.vdzon.administratie.model.Afschrift;
import com.vdzon.administratie.model.Declaratie;

import java.time.LocalDate;

@JsonIgnoreProperties
public class AfschriftDto {

    private String uuid;
    private String rekening;
    private String rekeningnaam;
    private String relatienaam;
    private LocalDate boekdatum;
    private double bedragBij = 0;
    private double bedragAf = 0;

    public AfschriftDto() {
    }

    public AfschriftDto(String uuid, String rekening, String rekeningnaam, String relatienaam, LocalDate boekdatum, double bedragBij, double bedragAf) {
        this.uuid = uuid;
        this.rekening = rekening;
        this.rekeningnaam = rekeningnaam;
        this.relatienaam = relatienaam;
        this.boekdatum = boekdatum;
        this.bedragBij = bedragBij;
        this.bedragAf = bedragAf;
    }

    public AfschriftDto(Afschrift afschrift) {
        this(afschrift.getUuid(), afschrift.getRekening(), afschrift.getRekeningnaam(), afschrift.getRelatienaam(), afschrift.getBoekdatum(), afschrift.getBedragBij(), afschrift.getBedragAf());
    }

    public Afschrift toAfschrift() {
        return new Afschrift(uuid, rekening, rekeningnaam, relatienaam, boekdatum, bedragBij, bedragAf);
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getRekening() {
        return rekening;
    }

    public void setRekening(String rekening) {
        this.rekening = rekening;
    }

    public String getRekeningnaam() {
        return rekeningnaam;
    }

    public void setRekeningnaam(String rekeningnaam) {
        this.rekeningnaam = rekeningnaam;
    }

    public String getRelatienaam() {
        return relatienaam;
    }

    public void setRelatienaam(String relatienaam) {
        this.relatienaam = relatienaam;
    }

    public LocalDate getBoekdatum() {
        return boekdatum;
    }

    public void setBoekdatum(LocalDate boekdatum) {
        this.boekdatum = boekdatum;
    }

    public double getBedragBij() {
        return bedragBij;
    }

    public void setBedragBij(double bedragBij) {
        this.bedragBij = bedragBij;
    }

    public double getBedragAf() {
        return bedragAf;
    }

    public void setBedragAf(double bedragAf) {
        this.bedragAf = bedragAf;
    }
}
