package com.vdzon.administratie.checkandfix.actions.check;

import com.vdzon.administratie.checkandfix.model.CheckAndFixRegel;
import com.vdzon.administratie.checkandfix.model.CheckType;
import com.vdzon.administratie.checkandfix.model.FixAction;
import com.vdzon.administratie.checkandfix.CheckAndFixData;
import com.vdzon.administratie.model.BoekingType;

import java.util.Collection;
import java.util.stream.Collectors;

public class CheckConsistency extends CheckActionHelper {


    @AdministratieCheckRule
    public Collection<? extends CheckAndFixRegel> checkConsistencyTussenRekeningEnAfschriften(CheckAndFixData data) {
        return data.alleRekeningen
                .stream()
                .filter(rekening -> hasGekoppeldAfschrift(rekening))
                .filter(rekening -> !rekening.getRekeningNummer().equals(getRekeningNummer(rekening, data)))
                .map(rekening -> new CheckAndFixRegel(FixAction.NONE, CheckType.WARNING, getAfschriftDto(rekening, data), "Inconsistentie tussen rekening " + rekening.getRekeningNummer() + " en afschrift " + getAfschriftDto(rekening, data).getNummer() + "(heeft nr " + getRekeningNummer(rekening, data) + ")", "", rekening.getRekeningDate()))
                .collect(Collectors.toList());
    }

    @AdministratieCheckRule
    public Collection<? extends CheckAndFixRegel> checkConsistencyTussenFacturenEnAfschriften(CheckAndFixData data) {
        return data.alleFacturen
                .stream()
                .filter(factuur -> hasGekoppeldAfschrift(factuur))
                .filter(factuur -> !factuur.getFactuurNummer().equals(getFactuurNummer(factuur, data)))
                .map(factuur -> new CheckAndFixRegel(FixAction.NONE, CheckType.WARNING, getAfschriftDto(factuur, data), "Inconsistentie tussen factuur " + factuur.getFactuurNummer() + " en afschrift " + getAfschriftDto(factuur, data).getNummer() + "(heeft nr " + getFactuurNummer(factuur, data), "", factuur.getFactuurDate()))
                .collect(Collectors.toList());
    }

    @AdministratieCheckRule
    public Collection<? extends CheckAndFixRegel> checkConsistencyTussenAfschiftenEnRekeningen(CheckAndFixData data) {
        return data.alleAfschriften
                .stream()
                .filter(afschrift -> afschrift.getBoekingType() == BoekingType.REKENING)
                .filter(afschrift -> data.rekeningMap.get(afschrift.getRekeningNummer()) != null)
                .filter(afschrift -> !data.rekeningMap.get(afschrift.getRekeningNummer()).getGekoppeldAfschrift().equals(afschrift.getNummer()))
                .map(afschrift -> new CheckAndFixRegel(FixAction.REMOVE_REF_FROM_AFSCHRIFT, CheckType.FIX_NEEDED, getAfschriftDto(afschrift), "Inconsistentie tussen afschrift " + afschrift.getNummer() + " en rekening " + afschrift.getRekeningNummer(), afschrift.getNummer(), afschrift.getBoekdatum()))
                .collect(Collectors.toList());
    }

    @AdministratieCheckRule
    public Collection<? extends CheckAndFixRegel> checkConsistencyTussenAfschiftenEnFacturen(CheckAndFixData data) {
        return data.alleAfschriften
                .stream()
                .filter(afschrift -> afschrift.getBoekingType() == BoekingType.FACTUUR)
                .filter(afschrift -> data.factuurMap.get(afschrift.getFactuurNummer()) != null)
                .filter(afschrift -> !data.factuurMap.get(afschrift.getFactuurNummer()).getGekoppeldAfschrift().equals(afschrift.getNummer()))
                .map(afschrift -> new CheckAndFixRegel(FixAction.REMOVE_REF_FROM_AFSCHRIFT, CheckType.FIX_NEEDED, getAfschriftDto(afschrift), "Inconsistentie tussen afschrift " + afschrift.getNummer() + " en factuur " + afschrift.getFactuurNummer(), afschrift.getNummer(), afschrift.getBoekdatum()))
                .collect(Collectors.toList());
    }

}
