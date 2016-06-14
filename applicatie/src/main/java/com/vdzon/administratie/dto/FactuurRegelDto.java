package com.vdzon.administratie.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.vdzon.administratie.model.FactuurRegel;
import lombok.*;

@ToString
@EqualsAndHashCode
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties
public class FactuurRegelDto {

    private String uuid;
    private String omschrijving;
    private double aantal;
    private double stuksPrijs;
    private double btwPercentage;

    public FactuurRegelDto(FactuurRegel factuurRegel) {
        this.uuid = factuurRegel.getUuid();
        this.omschrijving = factuurRegel.getOmschrijving();
        this.aantal = factuurRegel.getAantal();
        this.stuksPrijs = factuurRegel.getStuksPrijs();
        this.btwPercentage = factuurRegel.getBtwPercentage();
    }

    public FactuurRegel toFactuurRegel() {
        return new FactuurRegel(omschrijving, aantal, stuksPrijs, btwPercentage, uuid);
    }
}
