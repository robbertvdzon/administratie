package com.vdzon.administratie.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.vdzon.administratie.model.Factuur;
import com.vdzon.administratie.model.FactuurRegel;

import java.util.UUID;

@JsonIgnoreProperties
public class FactuurRegelDto {

    private String uuid;
    private String omschrijving;
    private double aantal;
    private double stuksPrijs;
    private double btwPercentage;

    public FactuurRegelDto() {
    }

    public FactuurRegelDto(String uuid, String omschrijving, double aantal, double stuksPrijs, double btwPercentage) {
        this.uuid = uuid;
        this.omschrijving = omschrijving;
        this.aantal = aantal;
        this.stuksPrijs = stuksPrijs;
        this.btwPercentage = btwPercentage;
    }

    public FactuurRegelDto(FactuurRegel factuurRegel) {
        this(factuurRegel.getUuid(), factuurRegel.getOmschrijving(), factuurRegel.getAantal(), factuurRegel.getStuksPrijs(), factuurRegel.getBtwPercentage());
    }


    public FactuurRegel toFactuurRegel() {
        return new FactuurRegel(omschrijving, aantal, stuksPrijs, btwPercentage, emptyUuid(uuid) ? getNewUuid() : uuid);
    }

    private String getNewUuid(){
        return UUID.randomUUID().toString();
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
