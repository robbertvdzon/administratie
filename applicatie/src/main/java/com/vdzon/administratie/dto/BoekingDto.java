package com.vdzon.administratie.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.vdzon.administratie.model.boekingen.Boeking;
import com.vdzon.administratie.model.boekingen.relaties.BoekingMetAfschrift;
import com.vdzon.administratie.model.boekingen.relaties.BoekingMetDeclaratie;
import com.vdzon.administratie.model.boekingen.relaties.BoekingMetFactuur;
import com.vdzon.administratie.model.boekingen.relaties.BoekingMetRekening;
import lombok.*;

@ToString
@EqualsAndHashCode
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor

@JsonIgnoreProperties
public class BoekingDto {

    private String uuid;
    private String omschrijving;
    private String factuurNummer;
    private String rekeningNummer;
    private String afschriftNummer;
    private String declaratieNummer;

    public BoekingDto(Boeking boeking) {
        this.omschrijving = boeking.getOmschrijving();
        this.uuid= boeking.getUuid();
        if (boeking instanceof BoekingMetAfschrift){
            this.afschriftNummer = ((BoekingMetAfschrift) boeking).getAfschriftNummer();
        }
        if (boeking instanceof BoekingMetFactuur){
            this.factuurNummer = ((BoekingMetFactuur) boeking).getFactuurNummer();
        }
        if (boeking instanceof BoekingMetRekening){
            this.rekeningNummer = ((BoekingMetRekening) boeking).getRekeningNummer();
        }
        if (boeking instanceof BoekingMetDeclaratie){
            this.declaratieNummer = ((BoekingMetDeclaratie) boeking).getDeclaratieNummer();
        }
    }

}
