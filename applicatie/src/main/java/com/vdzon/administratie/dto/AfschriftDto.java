package com.vdzon.administratie.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.vdzon.administratie.model.Afschrift;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@JsonIgnoreProperties
public class AfschriftDto {

    private String uuid;
    private String rekening;
    private String omschrijving;
    private String relatienaam;
    private String boekdatum;
    private double bedrag = 0;

    private static DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public AfschriftDto() {
    }

    public AfschriftDto(String uuid, String rekening, String omschrijving, String relatienaam, String boekdatum, double bedrag) {
        this.uuid = uuid;
        this.rekening = rekening;
        this.omschrijving = omschrijving;
        this.relatienaam = relatienaam;
        this.boekdatum = boekdatum;
        this.bedrag = bedrag;
    }

    public AfschriftDto(Afschrift afschrift) {
        this(afschrift.getUuid(), afschrift.getRekening(), afschrift.getOmschrijving(), afschrift.getRelatienaam(), afschrift.getBoekdatum()==null ? null : afschrift.getBoekdatum().format(DATE_FORMATTER), afschrift.getBedrag());
    }

    public Afschrift toAfschrift() {
        return new Afschrift(uuid, rekening, omschrijving, relatienaam, LocalDate.parse(boekdatum,DATE_FORMATTER), bedrag);
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

    public String getOmschrijving() {
        return omschrijving;
    }

    public void setOmschrijving(String omschrijving) {
        this.omschrijving = omschrijving;
    }

    public String getRelatienaam() {
        return relatienaam;
    }

    public void setRelatienaam(String relatienaam) {
        this.relatienaam = relatienaam;
    }

    public String getBoekdatum() {
        return boekdatum;
    }

    public void setBoekdatum(String boekdatum) {
        this.boekdatum = boekdatum;
    }

    public double getBedrag() {
        return bedrag;
    }

    public void setBedrag(double bedrag) {
        this.bedrag = bedrag;
    }

}
