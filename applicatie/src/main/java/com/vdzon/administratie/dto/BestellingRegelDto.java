package com.vdzon.administratie.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.vdzon.administratie.model.Bestelling;
import com.vdzon.administratie.model.BestellingRegel;
import com.vdzon.administratie.model.FactuurRegel;

@JsonIgnoreProperties
public class BestellingRegelDto {

    private String uuid;
    private String omschrijving;
    private double aantal;
    private double stuksPrijs;
    private double btwPercentage;

    public BestellingRegelDto() {
    }

    public BestellingRegelDto(String uuid, String omschrijving, double aantal, double stuksPrijs, double btwPercentage) {
        this.uuid = uuid;
        this.omschrijving = omschrijving;
        this.aantal = aantal;
        this.stuksPrijs = stuksPrijs;
        this.btwPercentage = btwPercentage;
    }

    public BestellingRegelDto(BestellingRegel bestellingRegel) {
        this(bestellingRegel.getUuid(), bestellingRegel.getOmschrijving(), bestellingRegel.getAantal(), bestellingRegel.getStuksPrijs(), bestellingRegel.getBtwPercentage());
    }


    public BestellingRegel toBestellingRegel() {
        return new BestellingRegel(omschrijving, aantal, stuksPrijs, btwPercentage, uuid);
    }

    private boolean emptyUuid(String uuid) {
        return uuid == null || uuid.length() == 0;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setOmschrijving(String omschrijving) {
        this.omschrijving = omschrijving;
    }

    public void setAantal(double aantal) {
        this.aantal = aantal;
    }

    public void setStuksPrijs(double stuksPrijs) {
        this.stuksPrijs = stuksPrijs;
    }

    public void setBtwPercentage(double btwPercentage) {
        this.btwPercentage = btwPercentage;
    }
}
