package com.vdzon.administratie.checkandfix.actions.check;

import com.vdzon.administratie.checkandfix.CheckAndFixData;
import com.vdzon.administratie.checkandfix.model.CheckAndFixRegel;
import com.vdzon.administratie.checkandfix.model.CheckType;
import com.vdzon.administratie.checkandfix.model.FixAction;

import java.util.Collection;
import java.util.stream.Collectors;

public class CheckBedragen extends CheckActionHelper {


    @AdministratieCheckRule
    public Collection<? extends CheckAndFixRegel> vergelijkBedragTussenFacturenEnAfschriften(CheckAndFixData data) {
        return data.alleFacturen
                .stream()
                .filter(factuur -> hasGekoppeldAfschrift(factuur))
                .filter(factuur -> factuur.getBedragIncBtw() != getAfschiftBedrag(factuur, data))
                .map(factuur -> CheckAndFixRegel
                        .builder()
                        .rubriceerAction(FixAction.NONE)
                        .checkType(CheckType.WARNING)
                        .afschrift(getAfschriftDto(factuur, data))
                        .omschrijving("Factuurbedrag van factuur " + factuur.getFactuurNummer() + " komt niet overeen met bedrag afschift " + factuur.getGekoppeldAfschrift() + "," + "(" + +factuur.getBedragIncBtw() + "!=" + getAfschiftBedrag(factuur, data) + ")")
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
                .filter(rekening -> hasGekoppeldAfschrift(rekening))
                .filter(rekening -> rekening.getBedragIncBtw() * -1 != getAfschiftBedrag(rekening, data))
                .map(rekening -> CheckAndFixRegel
                        .builder()
                        .rubriceerAction(FixAction.NONE)
                        .checkType(CheckType.WARNING)
                        .afschrift(getAfschriftDto(rekening, data))
                        .omschrijving("Rekeningbedrag van rekening " + rekening.getRekeningNummer() + " komt niet overeen met bedrag afschift " + rekening.getGekoppeldAfschrift() + ", (" + rekening.getBedragIncBtw() + "!=" + getAfschiftBedrag(rekening, data) + ")")
                        .data("")
                        .date(rekening.getRekeningDate())
                        .build()
                )
                .collect(Collectors.toList());
    }
}
