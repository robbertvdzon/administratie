package com.vdzon.administratie.model.boekingen;

import com.vdzon.administratie.model.boekingen.relaties.BoekingMetRekening;
import lombok.*;
import org.mongodb.morphia.annotations.Id;

@ToString
@EqualsAndHashCode
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor
public class OnbetaaldeRekeningBoeking extends Boeking implements BoekingMetRekening {
    private String rekeningNummer;
    @Id
    private String uuid;

    @Override
    public String getUuid() {
        return uuid;
    }

    @Override
    public String getRekeningNummer() {
        return rekeningNummer;
    }

    @Override
    public String getOmschrijving() {
        return "Onbetaalde rekening";
    }
}
