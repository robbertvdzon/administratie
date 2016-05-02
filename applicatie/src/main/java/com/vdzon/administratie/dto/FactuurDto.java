package com.vdzon.administratie.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.vdzon.administratie.model.Factuur;
import com.vdzon.administratie.model.FactuurRegel;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FactuurDto {

    private String uuid;
    private String factuurNummer;
    private String factuurDate;
    private KlantDto klant;
    private boolean betaald;
    private List<FactuurRegelDto> factuurRegels;
    private double bedragExBtw = 0;
    private double bedragIncBtw = 0;
    private double btw = 0;

    private static DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public FactuurDto() {
    }

    public FactuurDto(String uuid, String factuurNummer, String factuurDate, KlantDto klant, boolean betaald, List<FactuurRegelDto> factuurRegels, double bedragExBtw, double bedragIncBtw, double btw) {
        this.uuid = uuid;
        this.factuurNummer = factuurNummer;
        this.factuurDate = factuurDate;
        this.klant = klant;
        this.betaald = betaald;
        this.factuurRegels = factuurRegels;
        this.bedragExBtw = bedragExBtw;
        this.bedragIncBtw = bedragIncBtw;
        this.btw = btw;
    }

    public FactuurDto(Factuur factuur){
        this(factuur.getUuid(), factuur.getFactuurNummer(),factuur.getFactuurDate()==null ? null : factuur.getFactuurDate().format(DATE_FORMATTER), factuur.getKlant()==null ? null : new KlantDto(factuur.getKlant()),factuur.isBetaald(),toFactuurRegelsDto(factuur.getFactuurRegels()), factuur.getBedragExBtw(), factuur.getBedragIncBtw(), factuur.getBtw());
    }

    private static List<FactuurRegelDto> toFactuurRegelsDto(List<FactuurRegel> factuurRegels) {
        return factuurRegels == null ? new ArrayList<>() : factuurRegels
                .stream()
                .map(factuurRegel -> new FactuurRegelDto(factuurRegel))
                .collect(Collectors.toList());
    }

    public Factuur toFactuur() {
        return new Factuur(factuurNummer, LocalDate.parse(factuurDate,DATE_FORMATTER), klant == null ? null : klant.toKlant(), betaald, toFactuurRegels(), uuid);
    }

    private List<FactuurRegel> toFactuurRegels() {
        return factuurRegels == null ? new ArrayList<>() : factuurRegels
                .stream()
                .map(factuurRegelDto -> factuurRegelDto.toFactuurRegel())
                .collect(Collectors.toList());
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setFactuurNummer(String factuurNummer) {
        this.factuurNummer = factuurNummer;
    }

    public void setFactuurDate(String factuurDate) {
        this.factuurDate = factuurDate;
    }

    public void setKlant(KlantDto klant) {
        this.klant = klant;
    }

    public void setBetaald(boolean betaald) {
        this.betaald = betaald;
    }

    public void setFactuurRegels(List<FactuurRegelDto> factuurRegels) {
        this.factuurRegels = factuurRegels;
    }

    public void setBedragExBtw(double bedragExBtw) {
        this.bedragExBtw = bedragExBtw;
    }

    public void setBedragIncBtw(double bedragIncBtw) {
        this.bedragIncBtw = bedragIncBtw;
    }

    public void setBtw(double btw) {
        this.btw = btw;
    }
}
