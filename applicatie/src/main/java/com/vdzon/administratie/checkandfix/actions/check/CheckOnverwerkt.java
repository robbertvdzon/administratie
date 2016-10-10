package com.vdzon.administratie.checkandfix.actions.check;

import com.vdzon.administratie.checkandfix.CheckAndFixData;
import com.vdzon.administratie.checkandfix.model.CheckAndFixRegel;
import com.vdzon.administratie.checkandfix.model.CheckType;
import com.vdzon.administratie.checkandfix.model.FixAction;
import com.vdzon.administratie.dto.BoekingType;

import java.util.Collection;
import java.util.stream.Collectors;

public class CheckOnverwerkt extends CheckActionHelper {
//
//    @AdministratieCheckRule
//    public Collection<? extends CheckAndFixRegel> checkFacturenZonderAfschift(CheckAndFixData data) {
//        return data.alleFacturen
//                .stream()
//                .filter(factuur -> !hasGekoppeldAfschrift(factuur))
//                .map(factuur -> CheckAndFixRegel
//                        .builder()
//                        .rubriceerAction(FixAction.NONE)
//                        .checkType(CheckType.WARNING)
//                        .afschrift(null)
//                        .omschrijving("Factuur " + factuur.getFactuurNummer() + " is niet geboekt")
//                        .data("")
//                        .date(factuur.getFactuurDate())
//                        .build()
//                )
//                .collect(Collectors.toList());
//    }
//
//    @AdministratieCheckRule
//    public Collection<? extends CheckAndFixRegel> checkRekeningenZonderAfschift(CheckAndFixData data) {
//        return data.alleRekeningen
//                .stream()
//                .filter(rekening -> !hasGekoppeldAfschrift(rekening))
//                .map(rekening -> CheckAndFixRegel
//                        .builder()
//                        .rubriceerAction(FixAction.NONE)
//                        .checkType(CheckType.WARNING)
//                        .afschrift(null)
//                        .omschrijving("Rekening " + rekening.getRekeningNummer() + " is niet geboekt")
//                        .data("")
//                        .date(rekening.getRekeningDate())
//                        .build()
//                )
//                .collect(Collectors.toList());
//    }
//
//    @AdministratieCheckRule
//    public Collection<? extends CheckAndFixRegel> checkOnverwerkteAfschiften(CheckAndFixData data) {
//        return data.alleAfschriften
//                .stream()
//                .filter(afschrift -> afschrift.getBoekingType() == BoekingType.NONE)
//                .map(afschrift -> CheckAndFixRegel
//                        .builder()
//                        .rubriceerAction(FixAction.NONE)
//                        .checkType(CheckType.WARNING)
//                        .afschrift(getAfschriftDto(afschrift))
//                        .omschrijving("Afschift " + afschrift.getNummer() + " is niet verwerkt")
//                        .data("")
//                        .date(afschrift.getBoekdatum())
//                        .build()
//                )
//                .collect(Collectors.toList());
//    }
}
