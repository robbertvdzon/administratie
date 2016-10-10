package com.vdzon.administratie.checkandfix.actions.check;

import com.vdzon.administratie.checkandfix.CheckAndFixData;
import com.vdzon.administratie.checkandfix.model.CheckAndFixRegel;
import com.vdzon.administratie.checkandfix.model.CheckType;
import com.vdzon.administratie.checkandfix.model.FixAction;
import com.vdzon.administratie.dto.BoekingType;

import java.util.Collection;
import java.util.stream.Collectors;

public class CheckBestaan extends CheckActionHelper {

//
//    @AdministratieCheckRule
//    public Collection<? extends CheckAndFixRegel> checkAfschriftenOfRekeningenWelBestaan(CheckAndFixData data) {
//        return data.alleAfschriften
//                .stream()
//                .filter(afschrift -> afschrift.getBoekingType() == BoekingType.REKENING)
//                .filter(afschrift -> data.rekeningMap.get(afschrift.getRekeningNummer()) == null)
//                .map(afschrift -> CheckAndFixRegel
//                        .builder()
//                        .rubriceerAction(FixAction.REMOVE_REF_FROM_AFSCHRIFT)
//                        .checkType(CheckType.FIX_NEEDED)
//                        .afschrift(getAfschriftDto(afschrift))
//                        .omschrijving("Afschift " + afschrift.getNummer() + " is gekoppeld aan niet bestaande rekening " + afschrift.getRekeningNummer())
//                        .data(afschrift.getNummer())
//                        .date(afschrift.getBoekdatum()).build()
//                )
//                .collect(Collectors.toList());
//    }
//
//    @AdministratieCheckRule
//    public Collection<? extends CheckAndFixRegel> checkAfschriftenOfFacturenWelBestaan(CheckAndFixData data) {
//        return data.alleAfschriften
//                .stream()
//                .filter(afschrift -> afschrift.getBoekingType() == BoekingType.FACTUUR)
//                .filter(afschrift -> data.factuurMap.get(afschrift.getFactuurNummer()) == null)
//                .map(afschrift -> CheckAndFixRegel
//                        .builder()
//                        .rubriceerAction(FixAction.REMOVE_REF_FROM_AFSCHRIFT)
//                        .checkType(CheckType.FIX_NEEDED)
//                        .afschrift(getAfschriftDto(afschrift))
//                        .omschrijving("Afschift " + afschrift.getNummer() + " is gekoppeld aan niet bestaande factuur " + afschrift.getFactuurNummer())
//                        .data(afschrift.getNummer())
//                        .date(afschrift.getBoekdatum())
//                        .build()
//                )
//                .collect(Collectors.toList());
//    }
//
//    @AdministratieCheckRule
//    public Collection<? extends CheckAndFixRegel> checkRekeningenOfAfschriftenWelBestaan(CheckAndFixData data) {
//        return data.alleRekeningen
//                .stream()
//                .filter(rekening -> hasGekoppeldAfschrift(rekening))
//                .filter(rekening -> data.afschriftMap.get(rekening.getGekoppeldAfschrift()) == null)
//                .map(rekening -> CheckAndFixRegel
//                        .builder()
//                        .rubriceerAction(FixAction.REMOVE_REF_FROM_REKENING)
//                        .checkType(CheckType.FIX_NEEDED)
//                        .afschrift(getAfschriftDto(rekening, data))
//                        .omschrijving("Rekening " + rekening.getRekeningNummer() + " is geboekt aan niet bestaande afschrift " + rekening.getGekoppeldAfschrift())
//                        .data(rekening.getRekeningNummer())
//                        .date(rekening.getRekeningDate()).build()
//                )
//                .collect(Collectors.toList());
//    }
//
//    @AdministratieCheckRule
//    public Collection<? extends CheckAndFixRegel> checkFacturenOfAfschriftenWelBestaan(CheckAndFixData data) {
//        return data.alleFacturen
//                .stream()
//                .filter(factuur -> hasGekoppeldAfschrift(factuur))
//                .filter(factuur -> data.afschriftMap.get(factuur.getGekoppeldAfschrift()) == null)
//                .map(factuur -> CheckAndFixRegel
//                        .builder()
//                        .rubriceerAction(FixAction.REMOVE_REF_FROM_FACTUUR)
//                        .checkType(CheckType.FIX_NEEDED)
//                        .afschrift(getAfschriftDto(factuur, data))
//                        .omschrijving("Factuur " + factuur.getFactuurNummer() + " is geboekt aan niet bestaande afschrift " + factuur.getGekoppeldAfschrift())
//                        .data(factuur.getFactuurNummer())
//                        .date(factuur.getFactuurDate())
//                        .build()
//                )
//                .collect(Collectors.toList());
//    }
}
