package com.vdzon.administratie.checkandfix.actions.check;

import com.vdzon.administratie.checkandfix.model.CheckAndFixRegel;
import com.vdzon.administratie.checkandfix.model.CheckType;
import com.vdzon.administratie.checkandfix.model.FixAction;
import com.vdzon.administratie.checkandfix.rest.CheckAndFixData;
import com.vdzon.administratie.model.BoekingType;

import java.util.Collection;
import java.util.stream.Collectors;

public class CheckOnverwerkt extends CheckActionHelper {

    @AdministratieCheckRule
    public Collection<? extends CheckAndFixRegel> checkFacturenZonderAfschift(CheckAndFixData data) {
        return data.alleFacturen
                .stream()
                .filter(factuur -> !hasGekoppeldAfschrift(factuur))
                .map(factuur -> new CheckAndFixRegel(FixAction.NONE, CheckType.WARNING, null, "Factuur " + factuur.getFactuurNummer() + " is niet geboekt", "", factuur.getFactuurDate()))
                .collect(Collectors.toList());
    }

    @AdministratieCheckRule
    public Collection<? extends CheckAndFixRegel> checkRekeningenZonderAfschift(CheckAndFixData data) {
        return data.alleRekeningen
                .stream()
                .filter(rekening -> !hasGekoppeldAfschrift(rekening))
                .map(rekening -> new CheckAndFixRegel(FixAction.NONE, CheckType.WARNING, null, "Rekening " + rekening.getRekeningNummer() + " is niet geboekt", "", rekening.getRekeningDate()))
                .collect(Collectors.toList());
    }

    @AdministratieCheckRule
    public Collection<? extends CheckAndFixRegel> checkOnverwerkteAfschiften(CheckAndFixData data) {
        return data.alleAfschriften
                .stream()
                .filter(afschrift -> afschrift.getBoekingType() == BoekingType.NONE)
                .map(afschrift -> new CheckAndFixRegel(FixAction.NONE, CheckType.WARNING, getAfschriftDto(afschrift), "Afschift " + afschrift.getNummer() + " is niet verwerkt", "", afschrift.getBoekdatum()))
                .collect(Collectors.toList());
    }
}
