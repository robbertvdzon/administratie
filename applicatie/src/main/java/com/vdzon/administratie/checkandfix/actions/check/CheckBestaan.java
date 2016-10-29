package com.vdzon.administratie.checkandfix.actions.check;

import com.vdzon.administratie.checkandfix.CheckAndFixData;
import com.vdzon.administratie.checkandfix.model.CheckAndFixRegel;
import com.vdzon.administratie.checkandfix.model.CheckType;
import com.vdzon.administratie.checkandfix.model.FixAction;
import com.vdzon.administratie.dto.BoekingType;
import com.vdzon.administratie.model.Afschrift;
import com.vdzon.administratie.model.boekingen.relaties.BoekingMetAfschrift;
import com.vdzon.administratie.model.boekingen.relaties.BoekingMetFactuur;
import com.vdzon.administratie.model.boekingen.relaties.BoekingMetRekening;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

public class CheckBestaan {


//    @AdministratieCheckRule
//    public Collection<? extends CheckAndFixRegel> checkOfAfschriftNogBestaat(CheckAndFixData data) {
//        return data.alleBoekingen
//                .stream()
//                .filter(boeking -> boeking instanceof BoekingMetAfschrift)
//                .map(boeking -> (BoekingMetAfschrift)boeking)
//                .filter(boeking -> !afschriftExists(boeking.getAfschriftNummer(), data))
//                .map(boeking -> CheckAndFixRegel
//                        .newBuilder()
//                        .rubriceerAction(FixAction.REMOVE_BOEKING)
//                        .checkType(CheckType.FIX_NEEDED)
//                        .boekingUuid(boeking.getUuid())
//                        .omschrijving("Afschift " + boeking.getAfschriftNummer() + " bestaat niet meer terwijl er wel een boeking van bestaat")
//                        .build()
//                )
//                .collect(Collectors.toList());
//    }

    @AdministratieCheckRule
    public Collection<? extends CheckAndFixRegel> checkRekeningNogBestaat(CheckAndFixData data) {
        return data.alleBoekingen
                .stream()
                .filter(boeking -> boeking instanceof BoekingMetRekening)
                .map(boeking -> (BoekingMetRekening)boeking)
                .filter(boeking -> !rekeningExists(boeking.getRekeningNummer(), data))
                .map(boeking -> CheckAndFixRegel
                        .newBuilder()
                        .rubriceerAction(FixAction.REMOVE_BOEKING)
                        .checkType(CheckType.FIX_NEEDED)
                        .boekingUuid(boeking.getUuid())
                        .omschrijving("Rekening " + boeking.getRekeningNummer() + " bestaat niet meer terwijl er wel een boeking van bestaat")
                        .build()
                )
                .collect(Collectors.toList());
    }

    @AdministratieCheckRule
    public Collection<? extends CheckAndFixRegel> checkFactuurNogBestaat(CheckAndFixData data) {
        return data.alleBoekingen
                .stream()
                .filter(boeking -> boeking instanceof BoekingMetFactuur)
                .map(boeking -> (BoekingMetFactuur)boeking)
                .filter(boeking -> !factuurExists(boeking.getFactuurNummer(), data))
                .map(boeking -> CheckAndFixRegel
                        .newBuilder()
                        .rubriceerAction(FixAction.REMOVE_BOEKING)
                        .checkType(CheckType.FIX_NEEDED)
                        .boekingUuid(boeking.getUuid())
                        .omschrijving("Factuur " + boeking.getFactuurNummer() + " bestaat niet meer terwijl er wel een boeking van bestaat")
                        .build()
                )
                .collect(Collectors.toList());
    }

    private boolean afschriftExists(String afschriftNummer, CheckAndFixData data) {
        return data.alleAfschriften
                .stream()
                .filter(afschrift -> afschrift.getNummer().equals(afschriftNummer))
                .count()>0;
    }

    private boolean rekeningExists(String rekeningNummer, CheckAndFixData data) {
        return data.alleRekeningen
                .stream()
                .filter(rekening -> rekening.getRekeningNummer().equals(rekeningNummer))
                .count()>0;
    }

    private boolean factuurExists(String factuurNummer, CheckAndFixData data) {
        return data.alleFacturen
                .stream()
                .filter(factuur -> factuur.getFactuurNummer().equals(factuurNummer))
                .count()>0;
    }

}
