package com.vdzon.administratie.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.vdzon.administratie.model.Bestelling;
import com.vdzon.administratie.model.BestellingRegel;
import com.vdzon.administratie.model.FactuurRegel;
import lombok.*;

@ToString
@EqualsAndHashCode
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties
public class BestellingRegelDto {

    private String uuid;
    private String omschrijving;
    private double aantal;
    private double stuksPrijs;
    private double btwPercentage;

    public BestellingRegelDto(BestellingRegel bestellingRegel) {
        this.uuid = bestellingRegel.getUuid();
        this.omschrijving = bestellingRegel.getOmschrijving();
        this.aantal = bestellingRegel.getAantal();
        this.stuksPrijs = bestellingRegel.getStuksPrijs();
        this.btwPercentage = bestellingRegel.getBtwPercentage();
    }

    public BestellingRegel toBestellingRegel() {
        return BestellingRegel.builder().omschrijving(omschrijving).aantal(aantal).stuksPrijs(stuksPrijs).btwPercentage(btwPercentage).uuid(uuid).build();
    }
}
