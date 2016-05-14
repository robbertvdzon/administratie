package com.vdzon.administratie.model;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

@Entity("contact")
public class Contact {

    @Id
    private String uuid;
    private String klantNummer;
    private String naam;
    private String woonplaats;
    private String adres;
    private String postcode;
    private String land;

    public Contact() {
    }

    public Contact(String uuid, String klantNummer, String naam, String woonplaats, String adres, String postcode, String land) {
        this.uuid = uuid;
        this.klantNummer = klantNummer;
        this.naam = naam;
        this.woonplaats = woonplaats;
        this.adres = adres;
        this.postcode = postcode;
        this.land = land;
    }

    public String getKlantNummer() {
        return klantNummer;
    }

    public String getNaam() {
        return naam;
    }

    public String getWoonplaats() {
        return woonplaats;
    }

    public String getAdres() {
        return adres;
    }

    public String getPostcode() {
        return postcode;
    }

    public String getLand() {
        return land;
    }

    public String getUuid() {
        return uuid;
    }

}
