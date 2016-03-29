package com.vdzon.administratie.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mongodb.annotations.Immutable;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

@Entity("factuurRegel")
//@Immutable
@JsonIgnoreProperties
public class FactuurRegel {

    @Id
    private String uuid;
    private String omschrijving;
    private double aantal;
    private double stuksPrijs;
    private double btwPercentage;

    // for jackson
    public FactuurRegel() {
    }

    public FactuurRegel(String omschrijving, double aantal, double stuksPrijs, double btwPercentage, String uuid) {
        this.omschrijving = omschrijving;
        this.aantal = aantal;
        this.stuksPrijs = stuksPrijs;
        this.btwPercentage = btwPercentage;
        this.uuid = uuid;
    }

    public String getOmschrijving() {
        return omschrijving;
    }

    public double getAantal() {
        return aantal;
    }

    public double getStuksPrijs() {
        return stuksPrijs;
    }

    public double getBtwPercentage() {
        return btwPercentage;
    }

    public String getUuid() {
        return uuid;
    }

}
