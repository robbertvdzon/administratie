package com.vdzon.administratie.model.boekingen;

import com.vdzon.administratie.model.boekingen.relaties.BoekingMetDeclaratie;
import lombok.*;
import org.mongodb.morphia.annotations.Id;

@ToString
@EqualsAndHashCode
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor
public class DeclaratieBoeking extends Boeking implements BoekingMetDeclaratie {
    String declaratieNummer;
    @Id
    private String uuid;

    @Override
    public String getDeclaratieNummer() {
        return declaratieNummer;
    }

    @Override
    public String getOmschrijving() {
        return "Declaratie";
    }
}
