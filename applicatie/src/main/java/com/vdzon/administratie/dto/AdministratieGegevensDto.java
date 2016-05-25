package com.vdzon.administratie.dto;


import com.vdzon.administratie.model.AdministratieGegevens;

import java.util.UUID;

public class AdministratieGegevensDto {

    private String uuid;
    private String name;
    private String rekeningNummer;
    private String btwNummer;
    private String handelsRegister;
    private String adres;
    private String postcode;
    private String woonplaats;

    public AdministratieGegevensDto() {
    }

    public AdministratieGegevensDto(AdministratieGegevens administratieGegegens) {
        if (administratieGegegens != null) {
            this.uuid = administratieGegegens.getUuid();
            this.name = administratieGegegens.getName();

            this.rekeningNummer = administratieGegegens.getRekeningNummer();
            this.btwNummer = administratieGegegens.getBtwNummer();
            this.handelsRegister = administratieGegegens.getHandelsRegister();
            this.adres = administratieGegegens.getAdres();
            this.postcode = administratieGegegens.getPostcode();
            this.woonplaats = administratieGegegens.getWoonplaats();
        }
        else{
            this.uuid = UUID.randomUUID().toString();
        }
    }

    public AdministratieGegevens toAdministratieGegevens() {
        return new AdministratieGegevens(uuid, name, rekeningNummer, btwNummer, handelsRegister, adres, postcode, woonplaats);
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setName(String name) {
        this.name = name;
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
}
