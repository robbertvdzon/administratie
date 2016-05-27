package com.vdzon.administratie.model;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity("administratieGegevens")
public class AdministratieGegevens {

    @Id
    private String uuid;
    private String name;
    private String rekeningNummer;
    private String btwNummer;
    private String handelsRegister;
    private String adres;
    private String postcode;
    private String woonplaats;
    private String logoUrl;

    public AdministratieGegevens() {
    }

    public AdministratieGegevens(String uuid, String name, String rekeningNummer, String btwNummer, String handelsRegister, String adres, String postcode, String woonplaats, String logoUrl) {
        this.uuid = uuid;
        this.name = name;
        this.rekeningNummer = rekeningNummer;
        this.btwNummer = btwNummer;
        this.handelsRegister = handelsRegister;
        this.adres = adres;
        this.postcode = postcode;
        this.woonplaats = woonplaats;
        this.logoUrl = logoUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUuid() {
        return uuid;
    }

    public String getRekeningNummer() {
        return rekeningNummer;
    }

    public void setRekeningNummer(String rekeningNummer) {
        this.rekeningNummer = rekeningNummer;
    }

    public String getBtwNummer() {
        return btwNummer;
    }

    public void setBtwNummer(String btwNummer) {
        this.btwNummer = btwNummer;
    }

    public String getHandelsRegister() {
        return handelsRegister;
    }

    public void setHandelsRegister(String handelsRegister) {
        this.handelsRegister = handelsRegister;
    }

    public String getAdres() {
        return adres;
    }

    public void setAdres(String adres) {
        this.adres = adres;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getWoonplaats() {
        return woonplaats;
    }

    public void setWoonplaats(String woonplaats) {
        this.woonplaats = woonplaats;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    private boolean factuurNummerMatchesUuid(String uuid, Factuur factuur) {
        return uuid == null && factuur.getUuid() == null || uuid != null && uuid.equals(factuur.getUuid());
    }

}
