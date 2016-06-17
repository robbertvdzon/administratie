package com.vdzon.administratie.checkandfix.actions.check;

import com.vdzon.administratie.checkandfix.model.CheckAndFixRegel;
import com.vdzon.administratie.checkandfix.model.CheckType;
import com.vdzon.administratie.checkandfix.model.FixAction;
import com.vdzon.administratie.checkandfix.rest.CheckAndFixData;
import com.vdzon.administratie.model.BoekingType;

import java.util.Collection;
import java.util.stream.Collectors;

public class CheckBestaan extends CheckActionHelper {


    @AdministratieCheckRule
    public Collection<? extends CheckAndFixRegel> checkAfschriftenOfRekeningenWelBestaan(CheckAndFixData data) {
        return data.alleAfschriften
                .stream()
                .filter(afschrift -> afschrift.getBoekingType() == BoekingType.REKENING)
                .filter(afschrift -> data.rekeningMap.get(afschrift.getRekeningNummer()) == null)
                .map(afschrift -> new CheckAndFixRegel(FixAction.REMOVE_REF_FROM_AFSCHRIFT, CheckType.FIX_NEEDED, getAfschriftDto(afschrift), "Afschift " + afschrift.getNummer() + " is gekoppeld aan niet bestaande rekening " + afschrift.getRekeningNummer(), afschrift.getNummer(), afschrift.getBoekdatum()))
                .collect(Collectors.toList());
    }

    @AdministratieCheckRule
    public Collection<? extends CheckAndFixRegel> checkAfschriftenOfFacturenWelBestaan(CheckAndFixData data) {
        return data.alleAfschriften
                .stream()
                .filter(afschrift -> afschrift.getBoekingType() == BoekingType.FACTUUR)
                .filter(afschrift -> data.factuurMap.get(afschrift.getFactuurNummer()) == null)
                .map(afschrift -> new CheckAndFixRegel(FixAction.REMOVE_REF_FROM_AFSCHRIFT, CheckType.FIX_NEEDED, getAfschriftDto(afschrift), "Afschift " + afschrift.getNummer() + " is gekoppeld aan niet bestaande factuur " + afschrift.getFactuurNummer(), afschrift.getNummer(), afschrift.getBoekdatum()))
                .collect(Collectors.toList());
    }

    @AdministratieCheckRule
    public Collection<? extends CheckAndFixRegel> checkRekeningenOfAfschriftenWelBestaan(CheckAndFixData data) {
        return data.alleRekeningen
                .stream()
                .filter(rekening -> hasGekoppeldAfschrift(rekening))
                .filter(rekening -> data.afschriftMap.get(rekening.getGekoppeldAfschrift()) == null)
                .map(rekening -> new CheckAndFixRegel(FixAction.REMOVE_REF_FROM_REKENING, CheckType.FIX_NEEDED, getAfschriftDto(rekening, data), "Rekening " + rekening.getRekeningNummer() + " is geboekt aan niet bestaande afschrift " + rekening.getGekoppeldAfschrift(), rekening.getRekeningNummer(), rekening.getRekeningDate()))
                .collect(Collectors.toList());
    }

    @AdministratieCheckRule
    public Collection<? extends CheckAndFixRegel> checkFacturenOfAfschriftenWelBestaan(CheckAndFixData data) {
        return data.alleFacturen
                .stream()
                .filter(factuur -> hasGekoppeldAfschrift(factuur))
                .filter(factuur -> data.afschriftMap.get(factuur.getGekoppeldAfschrift()) == null)
                .map(factuur -> new CheckAndFixRegel(FixAction.REMOVE_REF_FROM_FACTUUR, CheckType.FIX_NEEDED, getAfschriftDto(factuur, data), "Factuur " + factuur.getFactuurNummer() + " is geboekt aan niet bestaande afschrift " + factuur.getGekoppeldAfschrift(), factuur.getFactuurNummer(), factuur.getFactuurDate()))
                .collect(Collectors.toList());
    }
}
