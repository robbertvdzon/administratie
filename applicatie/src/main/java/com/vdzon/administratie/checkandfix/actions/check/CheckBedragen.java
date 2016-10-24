package com.vdzon.administratie.checkandfix.actions.check;

import com.vdzon.administratie.checkandfix.CheckAndFixData;
import com.vdzon.administratie.checkandfix.model.CheckAndFixRegel;
import com.vdzon.administratie.checkandfix.model.CheckType;
import com.vdzon.administratie.checkandfix.model.FixAction;
import com.vdzon.administratie.model.Afschrift;
import com.vdzon.administratie.model.BoekingenCache;
import com.vdzon.administratie.model.Factuur;
import com.vdzon.administratie.model.Rekening;
import com.vdzon.administratie.model.boekingen.relaties.BoekingMetAfschrift;
import com.vdzon.administratie.model.boekingen.relaties.BoekingMetFactuur;

import java.util.Collection;
import java.util.stream.Collectors;

public class CheckBedragen {


    @AdministratieCheckRule
    public Collection<? extends CheckAndFixRegel> vergelijkBedragTussenFacturenEnAfschriften(CheckAndFixData data) {
        return data.alleFacturen
                .stream()
                .filter(factuur -> checkFactuur(factuur, data))
                .map(factuur -> CheckAndFixRegel
                        .builder()
                        .rubriceerAction(FixAction.NONE)
                        .checkType(CheckType.WARNING)
                        .omschrijving("Factuur " + factuur.getFactuurNummer() + " is niet volledig betaald of geboekt")
                        .data("")
                        .date(factuur.getFactuurDate())
                        .build()
                )
                .collect(Collectors.toList());
    }


    @AdministratieCheckRule
    public Collection<? extends CheckAndFixRegel> vergelijkBedragTussenRekeningenEnAfschriften(CheckAndFixData data) {
        return data.alleRekeningen
                .stream()
                .filter(rekening -> checkRekening(rekening, data))
                .map(rekening -> CheckAndFixRegel
                        .builder()
                        .rubriceerAction(FixAction.NONE)
                        .checkType(CheckType.WARNING)
                        .omschrijving("Rekening " + rekening.getRekeningNummer() + " is niet volledig betaald of geboekt")
                        .data("")
                        .date(rekening.getRekeningDate())
                        .build()
                )
                .collect(Collectors.toList());
    }

    private boolean checkFactuur(Factuur factuur, CheckAndFixData data) {
        double sumVanAfschriften = data.boekingenCache.getBoekingenVanFactuur(factuur.getFactuurNummer()).stream().mapToDouble(boeking -> getAfschriftBedrag(data, boeking)).sum();
        return sumVanAfschriften != factuur.getBedragIncBtw();
    }

    private boolean checkRekening(Rekening rekening, CheckAndFixData data) {
        double sumVanAfschriften = data.boekingenCache.getBoekingenVanRekening(rekening.getRekeningNummer()).stream().mapToDouble(boeking -> getAfschriftBedrag(data, boeking)).sum();
        return sumVanAfschriften != -1*rekening.getBedragIncBtw();
    }

    private double getAfschriftBedrag(CheckAndFixData data, BoekingMetAfschrift boeking) {
        return data.alleAfschriften
                .stream()
                .filter(afschrift -> afschrift.getNummer().equals(boeking.getAfschriftNummer()))
                .findFirst()
                .orElse(Afschrift.builder().bedrag(0).build())
                .getBedrag();
    }



}
