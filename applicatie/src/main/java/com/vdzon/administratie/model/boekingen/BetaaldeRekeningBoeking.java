package com.vdzon.administratie.model.boekingen;

import com.vdzon.administratie.model.boekingen.relaties.BoekingMetAfschrift;
import com.vdzon.administratie.model.boekingen.relaties.BoekingMetRekening;
import lombok.*;
import org.mongodb.morphia.annotations.Id;

@ToString
@EqualsAndHashCode
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor
public class BetaaldeRekeningBoeking extends Boeking implements BoekingMetRekening, BoekingMetAfschrift {
    private String rekeningNummer;
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
    public String getRekeningNummer() {
        return rekeningNummer;
    }

    @Override
    public String getOmschrijving() {
        return "Betaalde rekening";
    }
}
