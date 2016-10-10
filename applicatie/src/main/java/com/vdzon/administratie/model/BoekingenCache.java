package com.vdzon.administratie.model;

import com.vdzon.administratie.model.boekingen.Boeking;
import com.vdzon.administratie.model.boekingen.relaties.BoekingMetAfschrift;
import com.vdzon.administratie.model.boekingen.relaties.BoekingMetDeclaratie;
import com.vdzon.administratie.model.boekingen.relaties.BoekingMetFactuur;
import com.vdzon.administratie.model.boekingen.relaties.BoekingMetRekening;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;

@ToString
@EqualsAndHashCode
public class BoekingenCache {
    private Map<String, List<BoekingMetFactuur>> alleFactuurBoekingen = new HashMap<>();
    private Map<String, List<BoekingMetRekening>> alleRekeningBoekingen = new HashMap<>();
    private Map<String, List<BoekingMetAfschrift>> alleAfschriftBoekingen = new HashMap<>();
    private Map<String, List<BoekingMetDeclaratie>> alleDeclaratieBoekingen = new HashMap<>();

    public BoekingenCache(List<Boeking> boekingen) {
        alleAfschriftBoekingen = boekingen.stream()
                .filter(boeking -> boeking instanceof BoekingMetAfschrift)
                .map(boeking -> (BoekingMetAfschrift) boeking)
                .collect(groupingBy(BoekingMetAfschrift::getAfschriftNummer));

        alleFactuurBoekingen = boekingen.stream()
                .filter(boeking -> boeking instanceof BoekingMetFactuur)
                .map(boeking -> (BoekingMetFactuur) boeking)
                .collect(groupingBy(BoekingMetFactuur::getFactuurNummer));

        alleRekeningBoekingen = boekingen.stream()
                .filter(boeking -> boeking instanceof BoekingMetRekening)
                .map(boeking -> (BoekingMetRekening) boeking)
                .collect(groupingBy(BoekingMetRekening::getRekeningNummer));

        alleDeclaratieBoekingen = boekingen.stream()
                .filter(boeking -> boeking instanceof BoekingMetDeclaratie)
                .map(boeking -> (BoekingMetDeclaratie) boeking)
                .collect(groupingBy(BoekingMetDeclaratie::getDeclaratieNummer));
    }

    public List<BoekingMetFactuur> getBoekingenVanFactuur(String factuurUuid) {
        return alleFactuurBoekingen.get(factuurUuid);
    }

    public List<BoekingMetRekening> getBoekingenVanRekening(String factuurUuid) {
        return alleRekeningBoekingen.get(factuurUuid);
    }

    public List<BoekingMetDeclaratie> getBoekingenVanDeclaratie(String factuurUuid) {
        return alleDeclaratieBoekingen.get(factuurUuid);
    }

    public List<BoekingMetAfschrift> getBoekingenVanAfschrift(String factuurUuid) {
        return alleAfschriftBoekingen.get(factuurUuid);
    }

}
