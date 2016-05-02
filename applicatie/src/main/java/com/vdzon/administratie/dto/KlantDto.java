package com.vdzon.administratie.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.vdzon.administratie.model.Klant;

@JsonIgnoreProperties
public class KlantDto {

    private String uuid;
    private String klantNummer;
    private String naam;
    private String woonplaats;
    private String adres;
    private String postcode;
    private String land;

    public KlantDto() {
    }

    public KlantDto(String uuid, String klantNummer, String naam, String woonplaats, String adres, String postcode, String land) {
        this.uuid = uuid;
        this.klantNummer = klantNummer;
        this.naam = naam;
        this.woonplaats = woonplaats;
        this.adres = adres;
        this.postcode = postcode;
        this.land = land;
    }

    public KlantDto(Klant klant) {
        this(klant.getUuid(), klant.getKlantNummer(), klant.getNaam(), klant.getWoonplaats(), klant.getAdres(), klant.getPostcode(), klant.getLand());
    }

    public Klant toKlant() {
        return new Klant(uuid, klantNummer, naam, woonplaats, adres, postcode, land);

    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setKlantNummer(String klantNummer) {
        this.klantNummer = klantNummer;
    }

    public void setNaam(String naam) {
        this.naam = naam;
    }

    public void setWoonplaats(String woonplaats) {
        this.woonplaats = woonplaats;
    }

    public void setAdres(String adres) {
        this.adres = adres;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public void setLand(String land) {
        this.land = land;
    }
}
