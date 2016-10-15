package com.vdzon.administratie.model;

import com.vdzon.administratie.model.boekingen.Boeking;
import com.vdzon.administratie.model.boekingen.relaties.BoekingMetAfschrift;
import com.vdzon.administratie.model.boekingen.relaties.BoekingMetFactuur;
import com.vdzon.administratie.model.boekingen.relaties.BoekingMetRekening;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
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

    }

    public List<BoekingMetFactuur> getBoekingenVanFactuur(String factuurUuid) {
        List<BoekingMetFactuur> boekingMetFactuurs = alleFactuurBoekingen.get(factuurUuid);
        return boekingMetFactuurs == null ? new ArrayList<>() : boekingMetFactuurs;
    }

    public List<BoekingMetRekening> getBoekingenVanRekening(String rekeningUuid) {
        List<BoekingMetRekening> boekingMetRekenings = alleRekeningBoekingen.get(rekeningUuid);
        return boekingMetRekenings == null ? new ArrayList<>() : boekingMetRekenings;
    }

    public List<BoekingMetAfschrift> getBoekingenVanAfschrift(String afschriftUuid) {
        List<BoekingMetAfschrift> boekingMetAfschrifts = alleAfschriftBoekingen.get(afschriftUuid);
        return boekingMetAfschrifts == null ? new ArrayList<>() : boekingMetAfschrifts;
    }

}
