package com.vdzon.administratie.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.vdzon.administratie.model.Contact;

@JsonIgnoreProperties
public class ContactDto {

    private String uuid;
    private String klantNummer;
    private String naam;
    private String woonplaats;
    private String adres;
    private String postcode;
    private String land;

    public ContactDto() {
    }

    public ContactDto(String uuid, String klantNummer, String naam, String woonplaats, String adres, String postcode, String land) {
        this.uuid = uuid;
        this.klantNummer = klantNummer;
        this.naam = naam;
        this.woonplaats = woonplaats;
        this.adres = adres;
        this.postcode = postcode;
        this.land = land;
    }

    public ContactDto(Contact contact) {
        this(contact.getUuid(), contact.getKlantNummer(), contact.getNaam(), contact.getWoonplaats(), contact.getAdres(), contact.getPostcode(), contact.getLand());
    }

    public Contact toContact() {
        return new Contact(uuid, klantNummer, naam, woonplaats, adres, postcode, land);

    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getKlantNummer() {
        return klantNummer;
    }

    public void setKlantNummer(String klantNummer) {
        this.klantNummer = klantNummer;
    }

    public String getNaam() {
        return naam;
    }

    public void setNaam(String naam) {
        this.naam = naam;
    }

    public String getWoonplaats() {
        return woonplaats;
    }

    public void setWoonplaats(String woonplaats) {
        this.woonplaats = woonplaats;
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

    public String getLand() {
        return land;
    }

    public void setLand(String land) {
        this.land = land;
    }
}
