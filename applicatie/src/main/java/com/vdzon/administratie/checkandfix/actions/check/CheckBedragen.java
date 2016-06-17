package com.vdzon.administratie.checkandfix.actions.check;

import com.vdzon.administratie.checkandfix.model.CheckAndFixRegel;
import com.vdzon.administratie.checkandfix.model.CheckType;
import com.vdzon.administratie.checkandfix.model.FixAction;
import com.vdzon.administratie.checkandfix.rest.CheckAndFixData;

import java.util.Collection;
import java.util.stream.Collectors;

public class CheckBedragen extends CheckActionHelper {


    @AdministratieCheckRule
    public Collection<? extends CheckAndFixRegel> vergelijkBedragTussenFacturenEnAfschriften(CheckAndFixData data) {
        return data.alleFacturen
                .stream()
                .filter(factuur -> hasGekoppeldAfschrift(factuur))
                .filter(factuur -> factuur.getBedragIncBtw() != getAfschiftBedrag(factuur, data))
                .map(factuur -> new CheckAndFixRegel(FixAction.NONE, CheckType.WARNING, getAfschriftDto(factuur, data), "Factuurbedrag van factuur " + factuur.getFactuurNummer() + " komt niet overeen met bedrag afschift " + factuur.getGekoppeldAfschrift() + "", " (" + factuur.getBedragIncBtw() + "!=" + getAfschiftBedrag(factuur, data) + ")", factuur.getFactuurDate()))
                .collect(Collectors.toList());
    }

    @AdministratieCheckRule
    public Collection<? extends CheckAndFixRegel> vergelijkBedragTussenRekeningenEnAfschriften(CheckAndFixData data) {
        return data.alleRekeningen
                .stream()
                .filter(rekening -> hasGekoppeldAfschrift(rekening))
                .filter(rekening -> rekening.getBedragIncBtw() * -1 != getAfschiftBedrag(rekening, data))
                .map(rekening -> new CheckAndFixRegel(FixAction.NONE, CheckType.WARNING, getAfschriftDto(rekening, data), "Rekeningbedrag van rekening " + rekening.getRekeningNummer() + " komt niet overeen met bedrag afschift " + rekening.getGekoppeldAfschrift() + ", (" + rekening.getBedragIncBtw() + "!=" + getAfschiftBedrag(rekening, data) + ")", "", rekening.getRekeningDate()))
                .collect(Collectors.toList());
    }
}
