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
    private String gekoppeldeBestellingNummer;
    private String factuurDate;
    private ContactDto klant;
    private boolean betaald;
    private List<FactuurRegelDto> factuurRegels;
    private double bedragExBtw = 0;
    private double bedragIncBtw = 0;
    private double btw = 0;

    private static DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public FactuurDto() {
    }

    public FactuurDto(String uuid, String factuurNummer, String gekoppeldeBestellingNummer, String factuurDate, ContactDto klant, boolean betaald, List<FactuurRegelDto> factuurRegels, double bedragExBtw, double bedragIncBtw, double btw) {
        this.uuid = uuid;
        this.factuurNummer = factuurNummer;
        this.gekoppeldeBestellingNummer = gekoppeldeBestellingNummer;
        this.factuurDate = factuurDate;
        this.klant = klant;
        this.betaald = betaald;
        this.factuurRegels = factuurRegels;
        this.bedragExBtw = bedragExBtw;
        this.bedragIncBtw = bedragIncBtw;
        this.btw = btw;
    }

    public FactuurDto(Factuur factuur){
        this(factuur.getUuid(), factuur.getFactuurNummer(),factuur.getGekoppeldeBestellingNummer(), factuur.getFactuurDate()==null ? null : factuur.getFactuurDate().format(DATE_FORMATTER), factuur.getContact()==null ? null : new ContactDto(factuur.getContact()),factuur.isBetaald(),toFactuurRegelsDto(factuur.getFactuurRegels()), factuur.getBedragExBtw(), factuur.getBedragIncBtw(), factuur.getBtw());
    }

    private static List<FactuurRegelDto> toFactuurRegelsDto(List<FactuurRegel> factuurRegels) {
        return factuurRegels == null ? new ArrayList<>() : factuurRegels
                .stream()
                .map(factuurRegel -> new FactuurRegelDto(factuurRegel))
                .collect(Collectors.toList());
    }

    public Factuur toFactuur() {
        return new Factuur(factuurNummer, gekoppeldeBestellingNummer, LocalDate.parse(factuurDate,DATE_FORMATTER), klant == null ? null : klant.toContact(), betaald, toFactuurRegels(), uuid);
    }

    private List<FactuurRegel> toFactuurRegels() {
        return factuurRegels == null ? new ArrayList<>() : factuurRegels
                .stream()
                .map(factuurRegelDto -> factuurRegelDto.toFactuurRegel())
                .collect(Collectors.toList());
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getFactuurNummer() {
        return factuurNummer;
    }

    public void setFactuurNummer(String factuurNummer) {
        this.factuurNummer = factuurNummer;
    }

    public String getFactuurDate() {
        return factuurDate;
    }

    public void setFactuurDate(String factuurDate) {
        this.factuurDate = factuurDate;
    }

    public ContactDto getKlant() {
        return klant;
    }

    public void setKlant(ContactDto klant) {
        this.klant = klant;
    }

    public boolean isBetaald() {
        return betaald;
    }

    public void setBetaald(boolean betaald) {
        this.betaald = betaald;
    }

    public List<FactuurRegelDto> getFactuurRegels() {
        return factuurRegels;
    }

    public void setFactuurRegels(List<FactuurRegelDto> factuurRegels) {
        this.factuurRegels = factuurRegels;
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

    public String getGekoppeldeBestellingNummer() {
        return gekoppeldeBestellingNummer;
    }

    public void setGekoppeldeBestellingNummer(String gekoppeldeBestellingNummer) {
        this.gekoppeldeBestellingNummer = gekoppeldeBestellingNummer;
    }
}
