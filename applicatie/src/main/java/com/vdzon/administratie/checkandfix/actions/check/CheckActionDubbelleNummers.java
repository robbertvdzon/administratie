package com.vdzon.administratie.checkandfix.actions.check;

import com.vdzon.administratie.checkandfix.model.CheckAndFixRegel;
import com.vdzon.administratie.checkandfix.model.CheckType;
import com.vdzon.administratie.checkandfix.model.FixAction;
import com.vdzon.administratie.checkandfix.CheckAndFixData;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class CheckActionDubbelleNummers extends CheckActionHelper {

    @AdministratieCheckRule
    public Collection<? extends CheckAndFixRegel> checkDubbeleAfschriftNummers(CheckAndFixData data) {
        return data.alleAfschriften
                .stream()
                .filter(afschrift -> data.afschriftMap.get(afschrift.getNummer())!=afschrift)
                .map(afschrift -> new CheckAndFixRegel(FixAction.NONE, CheckType.WARNING, getAfschriftDto(afschrift), "Afschift " + afschrift.getNummer() + " bestaat meerdere keren","", afschrift.getBoekdatum()))
                .collect(Collectors.toList());
    }

    @AdministratieCheckRule
    public Collection<? extends CheckAndFixRegel> checkDubbeleRekeningNummers(CheckAndFixData data) {
        return data.alleRekeningen
                .stream()
                .filter(rekening -> data.rekeningMap.get(rekening.getRekeningNummer())!=rekening)
                .map(rekening -> new CheckAndFixRegel(FixAction.NONE, CheckType.WARNING, getAfschriftDto(rekening, data), "Rekening " + rekening.getRekeningNummer() + " bestaat meerdere keren","", rekening.getRekeningDate()))
                .collect(Collectors.toList());
    }

    @AdministratieCheckRule
    public List<CheckAndFixRegel> checkDubbeleFactuurNummers(CheckAndFixData data) {
        return data.alleFacturen
                .stream()
                .filter(factuur -> data.factuurMap.get(factuur.getFactuurNummer())!=factuur)
                .map(factuur -> new CheckAndFixRegel(FixAction.NONE, CheckType.WARNING, getAfschriftDto(factuur, data), "Factuur " + factuur.getFactuurNummer() + " bestaat meerdere keren","", factuur.getFactuurDate()))
                .collect(Collectors.toList());
    }





}
