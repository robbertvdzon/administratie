package com.vdzon.administratie.checkandfix.actions.check;

import com.vdzon.administratie.checkandfix.CheckAndFixData;
import com.vdzon.administratie.checkandfix.model.CheckAndFixRegel;
import com.vdzon.administratie.checkandfix.model.CheckType;
import com.vdzon.administratie.checkandfix.model.FixAction;
import com.vdzon.administratie.model.Afschrift;
import com.vdzon.administratie.model.Factuur;
import com.vdzon.administratie.model.Rekening;
import com.vdzon.administratie.model.boekingen.relaties.BoekingMetAfschrift;
import com.vdzon.administratie.model.boekingen.relaties.BoekingMetFactuur;
import com.vdzon.administratie.model.boekingen.relaties.BoekingMetRekening;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

public class CheckDubbeleNummers {


    @AdministratieCheckRule
    public Collection<? extends CheckAndFixRegel> checkDubbeleFactuurnummers(CheckAndFixData data) {
        Collection<CheckAndFixRegel> result = new ArrayList<>();
        Map<String, List<Factuur>> alleFacturenGroupedByNummer = data.alleFacturen
                .stream()
                .collect(groupingBy(Factuur::getFactuurNummer));

        for (String key : alleFacturenGroupedByNummer.keySet()){
            if (alleFacturenGroupedByNummer.get(key).size()>1){
                CheckAndFixRegel regel = CheckAndFixRegel
                        .newBuilder()
                        .rubriceerAction(FixAction.NONE)
                        .checkType(CheckType.WARNING)
                        .omschrijving("Factuur " + key + " bestaat " + alleFacturenGroupedByNummer.get(key).size() + " keer")
                        .build();
                result.add(regel);
            }
        }
        return result;
    }

    @AdministratieCheckRule
    public Collection<? extends CheckAndFixRegel> checkDubbeleRekeningnummers(CheckAndFixData data) {
        Collection<CheckAndFixRegel> result = new ArrayList<>();
        Map<String, List<Rekening>> alleRekeningenGroupedByNummer = data.alleRekeningen
                .stream()
                .collect(groupingBy(Rekening::getRekeningNummer));

        for (String key : alleRekeningenGroupedByNummer.keySet()){
            if (alleRekeningenGroupedByNummer.get(key).size()>1){
                CheckAndFixRegel regel = CheckAndFixRegel
                        .newBuilder()
                        .rubriceerAction(FixAction.NONE)
                        .checkType(CheckType.WARNING)
                        .omschrijving("Rekening " + key + " bestaat " + alleRekeningenGroupedByNummer.get(key).size() + " keer")
                        .build();
                result.add(regel);
            }
        }
        return result;
    }

    @AdministratieCheckRule
    public Collection<? extends CheckAndFixRegel> checkDubbeleAfschriftnummers(CheckAndFixData data) {
        Collection<CheckAndFixRegel> result = new ArrayList<>();
        Map<String, List<Afschrift>> alleAfschriftenGroupedByNummer = data.alleAfschriften
                .stream()
                .collect(groupingBy(Afschrift::getNummer));

        for (String key : alleAfschriftenGroupedByNummer.keySet()){
            if (alleAfschriftenGroupedByNummer.get(key).size()>1){
                CheckAndFixRegel regel = CheckAndFixRegel
                        .newBuilder()
                        .rubriceerAction(FixAction.NONE)
                        .checkType(CheckType.WARNING)
                        .omschrijving("Afschrift " + key + " bestaat " + alleAfschriftenGroupedByNummer.get(key).size() + " keer")
                        .build();
                result.add(regel);
            }
        }
        return result;
    }


}
