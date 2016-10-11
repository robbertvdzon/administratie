package com.vdzon.administratie.model.boekingen;

import com.vdzon.administratie.model.boekingen.relaties.BoekingMetFactuur;
import lombok.*;
import org.mongodb.morphia.annotations.Id;

@ToString
@EqualsAndHashCode
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor
public class OnbetaaldeFactuurBoeking extends Boeking implements BoekingMetFactuur {
    private String factuurNummer;
    @Id
    private String uuid;

    @Override
    public String getUuid() {
        return uuid;
    }

    @Override
    public String getFactuurNummer() {
        return factuurNummer;
    }

    @Override
    public String getOmschrijving() {
        return "Onbetaalde factuur";
    }
}
