package com.vdzon.administratie.model.boekingen;

import com.vdzon.administratie.model.boekingen.relaties.BoekingMetAfschrift;
import lombok.*;
import org.mongodb.morphia.annotations.Id;

@ToString
@EqualsAndHashCode
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor
public class PriveBetalingBoeking extends Boeking implements BoekingMetAfschrift {
    private String afschriftNummer;
    @Id
    private String uuid;

    @Override
    public String getUuid() {
        return uuid;
    }

    @Override
    public String getAfschriftNummer() {
        return afschriftNummer;
    }

    @Override
    public String getOmschrijving() {
        return "Prive betaling";
    }
}
