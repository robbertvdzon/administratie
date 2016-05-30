package com.vdzon.administratie.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.vdzon.administratie.model.Declaratie;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@JsonIgnoreProperties
public class DeclaratieDto {

    private String uuid;
    private String declaratieNummer;
    private String omschrijving;
    private String declaratieDate;
    private double bedragExBtw = 0;
    private double bedragIncBtw = 0;
    private double btw = 0;

    private static DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public DeclaratieDto() {
    }

    public DeclaratieDto(String uuid, String declaratieNummer, String omschrijving, String declaratieDate, double bedragExBtw, double bedragIncBtw, double btw) {
        this.uuid = uuid;
        this.declaratieNummer = declaratieNummer;
        this.omschrijving = omschrijving;
        this.declaratieDate = declaratieDate;
        this.bedragExBtw = bedragExBtw;
        this.bedragIncBtw = bedragIncBtw;
        this.btw = btw;
    }

    public DeclaratieDto(Declaratie declaratie) {
        this(declaratie.getUuid(), declaratie.getDeclaratieNummer(), declaratie.getOmschrijving(), declaratie.getDeclaratieDate()==null ? null : declaratie.getDeclaratieDate().format(DATE_FORMATTER), declaratie.getBedragExBtw(), declaratie.getBedragIncBtw(), declaratie.getBtw());
    }

    public Declaratie toDeclaratie() {
        return new Declaratie(uuid, declaratieNummer, omschrijving, LocalDate.parse(declaratieDate,DATE_FORMATTER), bedragExBtw, bedragIncBtw, btw);
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getDeclaratieNummer() {
        return declaratieNummer;
    }

    public void setDeclaratieNummer(String declaratieNummer) {
        this.declaratieNummer = declaratieNummer;
    }

    public String getOmschrijving() {
        return omschrijving;
    }

    public void setOmschrijving(String omschrijving) {
        this.omschrijving = omschrijving;
    }

    public String getDeclaratieDate() {
        return declaratieDate;
    }

    public void setDeclaratieDate(String declaratieDate) {
        this.declaratieDate = declaratieDate;
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
