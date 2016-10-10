package com.vdzon.administratie.model.boekingen;

import com.vdzon.administratie.model.boekingen.relaties.BoekingMetAfschrift;
import lombok.*;
import org.mongodb.morphia.annotations.Id;

@ToString
@EqualsAndHashCode
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor
public class OnverwerktAfschiftBoeking extends Boeking implements BoekingMetAfschrift {
    String afschriftNummer;
    @Id
    private String uuid;

    @Override
    public String getAfschriftNummer() {
        return afschriftNummer;
    }

    @Override
    public String getOmschrijving() {
        return "Onverwerkte afschrift";
    }
}
