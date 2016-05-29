package com.vdzon.administratie.model;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity("factuur")
public class Bestelling {

    @Id
    private String uuid;
    private String bestellingNummer;
    private LocalDate bestellingDate;
    private Contact contact;
    private List<BestellingRegel> bestellingRegels = new ArrayList<>();
    private double bedragExBtw = 0;
    private double bedragIncBtw = 0;
    private double btw = 0;

    public Bestelling() {
    }

    public Bestelling(String bestellingNummer, LocalDate bestellingDate, Contact contact, List<BestellingRegel> bestellingRegels, String uuid) {
        this.bestellingNummer = bestellingNummer;
        this.bestellingDate = bestellingDate;
        this.contact = contact;
        this.bestellingRegels = bestellingRegels;
        this.uuid = uuid;
        calculate();
    }

    public String getBestellingNummer() {
        return bestellingNummer;
    }

    public void setBestellingNummer(String bestellingNummer) {
        this.bestellingNummer = bestellingNummer;
    }

    public LocalDate getBestellingDate() {
        return bestellingDate;
    }

    public void setBestellingDate(LocalDate bestellingDate) {
        this.bestellingDate = bestellingDate;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public String getUuid() {
        return uuid;
    }

    public List<BestellingRegel> getBestellingRegels() {
        return Collections.unmodifiableList(new ArrayList<>(bestellingRegels));
    }

    public void removeAllFactuurRegels() {
        bestellingRegels.clear();
        calculate();
    }

    public void addBestellingRegel(BestellingRegel bestellingRegel) {
        bestellingRegels.add(bestellingRegel);
        calculate();
    }

    public void addFactuurRegels(List<BestellingRegel> bestellingRegels) {
        bestellingRegels.addAll(bestellingRegels);
        calculate();
    }

    public double getBedragExBtw() {
        return bedragExBtw;
    }

    public double getBedragIncBtw() {
        return bedragIncBtw;
    }

    public double getBtw() {
        return btw;
    }

    private void calculate() {
        bedragExBtw = 0;
        bedragIncBtw = 0;
        btw = 0;
        for (BestellingRegel bestellingRegel : bestellingRegels) {
            bedragExBtw += bestellingRegel.getStuksPrijs() * bestellingRegel.getAantal();
            btw += bedragExBtw * (bestellingRegel.getBtwPercentage() / 100);
            bedragIncBtw += bedragExBtw + btw;
        }
    }

}
