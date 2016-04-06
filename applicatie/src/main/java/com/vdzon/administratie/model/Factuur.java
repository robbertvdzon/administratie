package com.vdzon.administratie.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity("factuur")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Factuur {

    @Id
    private String uuid;
    private String omschrijving;
    private String factuurNummer;
    private long datum;
    private Klant klant;
    private boolean betaald;
    private List<FactuurRegel> factuurRegels;
    private double bedragExBtw = 0;
    private double bedragIncBtw = 0;
    private double btw = 0;

    // for jackson
    public Factuur() {
    }

    public Factuur(String omschrijving, String factuurNummer, long datum, Klant klant, boolean betaald, List<FactuurRegel> factuurRegels, String uuid) {
        this.omschrijving = omschrijving;
        this.factuurNummer = factuurNummer;
        this.datum = datum;
        this.klant = klant;
        this.betaald = betaald;
        this.factuurRegels = factuurRegels;
        this.uuid = uuid;
        calculate();
    }

    public String getOmschrijving() {
        return omschrijving;
    }

    public void setOmschrijving(String omschrijving) {
        this.omschrijving = omschrijving;
    }

    public String getFactuurNummer() {
        return factuurNummer;
    }

    public void setFactuurNummer(String factuurNummer) {
        this.factuurNummer = factuurNummer;
    }

    public long getDatum() {
        return datum;
    }

    public void setDatum(long datum) {
        this.datum = datum;
    }

    public Klant getKlant() {
        return klant;
    }

    public void setKlant(Klant klant) {
        this.klant = klant;
    }

    public boolean isBetaald() {
        return betaald;
    }

    public void setBetaald(boolean betaald) {
        this.betaald = betaald;
    }

    public String getUuid() {
        return uuid;
    }

    public List<FactuurRegel> getFactuurRegels() {
        return Collections.unmodifiableList(factuurRegels == null ? new ArrayList<FactuurRegel>() : factuurRegels);
    }

    public void removeAllFactuurRegels(){
        factuurRegels.clear();
        calculate();
    }

    public void addFactuurRegel(FactuurRegel factuurRegel){
        factuurRegels.add(factuurRegel);
        calculate();
    }

    public void addFactuurRegels(List<FactuurRegel> factuurRegels){
        factuurRegels.addAll(factuurRegels);
        calculate();
    }

    private void calculate(){
        bedragExBtw = 0;
        bedragIncBtw = 0;
        btw = 0;
        for (FactuurRegel factuurRegel:factuurRegels){
            bedragExBtw += factuurRegel.getStuksPrijs() * factuurRegel.getAantal();
            btw +=  bedragExBtw * (factuurRegel.getBtwPercentage()/100);
            bedragIncBtw += bedragExBtw + btw;

        }
    }

}
