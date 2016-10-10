package com.vdzon.administratie.checkandfix.actions.check;

import com.vdzon.administratie.checkandfix.CheckAndFixData;
import com.vdzon.administratie.checkandfix.model.CheckAndFixRegel;
import com.vdzon.administratie.checkandfix.model.CheckType;
import com.vdzon.administratie.checkandfix.model.FixAction;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class CheckActionDubbelleNummers extends CheckActionHelper {
//
//    @AdministratieCheckRule
//    public Collection<? extends CheckAndFixRegel> checkDubbeleAfschriftNummers(CheckAndFixData data) {
//        return data.alleAfschriften
//                .stream()
//                .filter(afschrift -> data.afschriftMap.get(afschrift.getNummer()) != afschrift)
//                .map(afschrift -> CheckAndFixRegel
//                        .builder()
//                        .rubriceerAction(FixAction.NONE)
//                        .checkType(CheckType.WARNING)
//                        .afschrift(getAfschriftDto(afschrift))
//                        .omschrijving("Afschift " + afschrift.getNummer() + " bestaat meerdere keren")
//                        .data("")
//                        .date(afschrift.getBoekdatum())
//                        .build()
//                )
//                .collect(Collectors.toList());
//    }
//
//    @AdministratieCheckRule
//    public Collection<? extends CheckAndFixRegel> checkDubbeleRekeningNummers(CheckAndFixData data) {
//        return data.alleRekeningen
//                .stream()
//                .filter(rekening -> data.rekeningMap.get(rekening.getRekeningNummer()) != rekening)
//                .map(rekening -> CheckAndFixRegel
//                        .builder()
//                        .rubriceerAction(FixAction.NONE)
//                        .checkType(CheckType.WARNING)
//                        .afschrift(getAfschriftDto(rekening, data))
//                        .omschrijving("Rekening " + rekening.getRekeningNummer() + " bestaat meerdere keren")
//                        .data("")
//                        .date(rekening.getRekeningDate())
//                        .build()
//                )
//                .collect(Collectors.toList());
//    }
//
//    @AdministratieCheckRule
//    public List<CheckAndFixRegel> checkDubbeleFactuurNummers(CheckAndFixData data) {
//        return data.alleFacturen
//                .stream()
//                .filter(factuur -> data.factuurMap.get(factuur.getFactuurNummer()) != factuur)
//                .map(factuur -> CheckAndFixRegel
//                        .builder()
//                        .rubriceerAction(FixAction.NONE)
//                        .checkType(CheckType.WARNING)
//                        .afschrift(getAfschriftDto(factuur, data))
//                        .omschrijving("Factuur " + factuur.getFactuurNummer() + " bestaat meerdere keren")
//                        .data("")
//                        .date(factuur.getFactuurDate())
//                        .build()
//                )
//                .collect(Collectors.toList());
//    }


}
