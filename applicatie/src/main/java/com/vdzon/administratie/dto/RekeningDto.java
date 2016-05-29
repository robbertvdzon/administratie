package com.vdzon.administratie.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.vdzon.administratie.model.Contact;
import com.vdzon.administratie.model.Rekening;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@JsonIgnoreProperties
public class RekeningDto {

    private String uuid;
    private String rekeningNummer;
    private String naam;
    private String omschrijving;
    private String rekeningDate;
    private double bedragExBtw = 0;
    private double bedragIncBtw = 0;
    private double btw = 0;

    private static DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public RekeningDto() {
    }

    public RekeningDto(String uuid, String rekeningNummer, String naam, String omschrijving, String rekeningDate, double bedragExBtw, double bedragIncBtw, double btw) {
        this.uuid = uuid;
        this.rekeningNummer = rekeningNummer;
        this.naam = naam;
        this.omschrijving = omschrijving;
        this.rekeningDate = rekeningDate;
        this.bedragExBtw = bedragExBtw;
        this.bedragIncBtw = bedragIncBtw;
        this.btw = btw;
    }

    public RekeningDto(Rekening rekening) {
        this(rekening.getUuid(), rekening.getRekeningNummer(), rekening.getNaam(), rekening.getOmschrijving(), rekening.getRekeningDate()==null ? null : rekening.getRekeningDate().format(DATE_FORMATTER),rekening.getBedragExBtw(),rekening.getBedragIncBtw(),rekening.getBtw() );
    }

    public Rekening toRekening() {
        return new Rekening(uuid, rekeningNummer, naam, omschrijving, LocalDate.parse(rekeningDate,DATE_FORMATTER), bedragExBtw, bedragIncBtw, btw);
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getRekeningNummer() {
        return rekeningNummer;
    }

    public void setRekeningNummer(String rekeningNummer) {
        this.rekeningNummer = rekeningNummer;
    }

    public String getNaam() {
        return naam;
    }

    public void setNaam(String naam) {
        this.naam = naam;
    }

    public String getOmschrijving() {
        return omschrijving;
    }

    public void setOmschrijving(String omschrijving) {
        this.omschrijving = omschrijving;
    }

    public String getRekeningDate() {
        return rekeningDate;
    }

    public void setRekeningDate(String rekeningDate) {
        this.rekeningDate = rekeningDate;
    }

    public double getBedragExBtw() {
        return bedragExBtw;
    }

    public void setBedragExBtw(double bedragExBtw) {
        this.bedragExBtw = bedragExBtw;
    }

    public double getBedragIncBtw() {
        return bedragIncBtw;
    }

    public void setBedragIncBtw(double bedragIncBtw) {
        this.bedragIncBtw = bedragIncBtw;
    }

    public double getBtw() {
        return btw;
    }

    public void setBtw(double btw) {
        this.btw = btw;
    }
}
