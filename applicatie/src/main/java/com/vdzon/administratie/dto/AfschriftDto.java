package com.vdzon.administratie.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.vdzon.administratie.model.Afschrift;
import com.vdzon.administratie.model.BoekingType;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@JsonIgnoreProperties
public class AfschriftDto {

    private String uuid;
    private String nummer;
    private String rekening;
    private String omschrijving;
    private String relatienaam;
    private String boekdatum;
    private double bedrag = 0;
    private BoekingType boekingType;
    private String factuurNummer;
    private String rekeningNummer;

    private static DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public AfschriftDto() {
    }

    public AfschriftDto(String uuid, String nummer, String rekening, String omschrijving, String relatienaam, String boekdatum, double bedrag, BoekingType boekingType, String factuurNummer, String rekeningNummer) {
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

    public AfschriftDto(Afschrift afschrift) {
        this(afschrift.getUuid(), afschrift.getNummer(),  afschrift.getRekening(), afschrift.getOmschrijving(), afschrift.getRelatienaam(), afschrift.getBoekdatum()==null ? null : afschrift.getBoekdatum().format(DATE_FORMATTER), afschrift.getBedrag(), afschrift.getBoekingType(), afschrift.getFactuurNummer(), afschrift.getRekeningNummer());
    }

    public Afschrift toAfschrift() {
        return Afschrift.builder()
                .uuid(uuid)
                .nummer(nummer)
                .rekening(rekening)
                .omschrijving(omschrijving)
                .relatienaam(relatienaam)
                .boekdatum(LocalDate.parse(boekdatum,DATE_FORMATTER))
                .bedrag(bedrag)
                .boekingType(boekingType)
                .factuurNummer(factuurNummer)
                .rekeningNummer(rekeningNummer)
                .build();
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

    public BoekingType getBoekingType() {
        return boekingType;
    }

    public void setBoekingType(BoekingType boekingType) {
        this.boekingType = boekingType;
    }

    public String getFactuurNummer() {
        return factuurNummer;
    }

    public void setFactuurNummer(String factuurNummer) {
        this.factuurNummer = factuurNummer;
    }

    public String getRekeningNummer() {
        return rekeningNummer;
    }

    public void setRekeningNummer(String rekeningNummer) {
        this.rekeningNummer = rekeningNummer;
    }

    public String getNummer() {
        return nummer;
    }

    public void setNummer(String nummer) {
        this.nummer = nummer;
    }
}
