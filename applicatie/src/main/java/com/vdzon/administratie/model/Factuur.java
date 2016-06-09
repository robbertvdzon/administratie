package com.vdzon.administratie.model;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity("factuur")
public class Factuur {

    @Id
    private String uuid;
    private String factuurNummer;
    private String gekoppeldeBestellingNummer;
    private LocalDate factuurDate;
    private Contact contact;
    private boolean betaald;
    private List<FactuurRegel> factuurRegels = new ArrayList<>();
    private double bedragExBtw = 0;
    private double bedragIncBtw = 0;
    private double btw = 0;
    private String gekoppeldAfschrift;

    public Factuur() {
    }

    public Factuur(String factuurNummer, String gekoppeldeBestellingNummer, LocalDate factuurDate, Contact contact, boolean betaald, List<FactuurRegel> factuurRegels, String uuid, String gekoppeldAfschrift) {
        this.factuurNummer = factuurNummer;
        this.gekoppeldeBestellingNummer = gekoppeldeBestellingNummer;
        this.factuurDate = factuurDate;
        this.contact = contact;
        this.betaald = betaald;
        this.factuurRegels = factuurRegels;
        this.uuid = uuid;
        this.gekoppeldAfschrift = gekoppeldAfschrift;
        calculate();
    }

    public String getFactuurNummer() {
        return factuurNummer;
    }

//    public void setFactuurNummer(String factuurNummer) {
//        this.factuurNummer = factuurNummer;
//    }

    public LocalDate getFactuurDate() {
        return factuurDate;
    }

//    public void setFactuurDate(LocalDate factuurDate) {
//        this.factuurDate = factuurDate;
//    }

    public Contact getContact() {
        return contact;
    }

//    public void setContact(Contact contact) {
//        this.contact = contact;
//    }

    public boolean isBetaald() {
        return betaald;
    }

//    public void setBetaald(boolean betaald) {
//        this.betaald = betaald;
//    }

    public String getUuid() {
        return uuid;
    }

    public List<FactuurRegel> getFactuurRegels() {
        return Collections.unmodifiableList(new ArrayList<>(factuurRegels));
    }

//    public void removeAllFactuurRegels() {
//        factuurRegels.clear();
//        calculate();
//    }
//
//    public void addFactuurRegel(FactuurRegel factuurRegel) {
//        factuurRegels.add(factuurRegel);
//        calculate();
//    }
//
//    public void addFactuurRegels(List<FactuurRegel> factuurRegels) {
//        factuurRegels.addAll(factuurRegels);
//        calculate();
//    }

    public double getBedragExBtw() {
        return bedragExBtw;
    }

    public double getBedragIncBtw() {
        return bedragIncBtw;
    }

    public double getBtw() {
        return btw;
    }

    public String getGekoppeldeBestellingNummer() {
        return gekoppeldeBestellingNummer;
    }

    public String getGekoppeldAfschrift() {
        return gekoppeldAfschrift;
    }


    private void calculate() {
        bedragExBtw = 0;
        bedragIncBtw = 0;
        btw = 0;
        for (FactuurRegel factuurRegel : factuurRegels) {
            bedragExBtw += factuurRegel.getStuksPrijs() * factuurRegel.getAantal();
            btw += bedragExBtw * (factuurRegel.getBtwPercentage() / 100);
            bedragIncBtw += bedragExBtw + btw;
        }
    }


}
