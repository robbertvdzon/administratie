package com.vdzon.administratie.checkandfix.actions.check;

import com.vdzon.administratie.checkandfix.rest.CheckAndFixData;
import com.vdzon.administratie.dto.AfschriftDto;
import com.vdzon.administratie.model.Afschrift;
import com.vdzon.administratie.model.Factuur;
import com.vdzon.administratie.model.Rekening;


public class CheckActionHelper {

    protected AfschriftDto getAfschriftDto(Afschrift afschrift) {
        return afschrift==null?null:new AfschriftDto(afschrift);
    }

    protected boolean hasGekoppeldAfschrift(Factuur factuur) {
        return factuur.getGekoppeldAfschrift() != null && factuur.getGekoppeldAfschrift().length() != 0;
    }

    protected boolean hasGekoppeldAfschrift(Rekening rekening) {
        return rekening.getGekoppeldAfschrift() != null && rekening.getGekoppeldAfschrift().length() != 0;
    }


    protected AfschriftDto getAfschriftDto(Factuur factuur, CheckAndFixData data) {
        Afschrift afschrift = data.afschriftMap.get(factuur.getGekoppeldAfschrift());
        return afschrift==null?null:new AfschriftDto(afschrift);
    }

    protected AfschriftDto getAfschriftDto(Rekening rekening, CheckAndFixData data) {
        Afschrift afschrift = data.afschriftMap.get(rekening.getGekoppeldAfschrift());
        return afschrift==null?null:new AfschriftDto(afschrift);
    }

    protected double getAfschiftBedrag(Factuur factuur, CheckAndFixData data) {
        Afschrift afschrift = data.afschriftMap.get(factuur.getGekoppeldAfschrift());
        return afschrift==null?0:afschrift.getBedrag();
    }

    protected double getAfschiftBedrag(Rekening rekening, CheckAndFixData data) {
        Afschrift afschrift = data.afschriftMap.get(rekening.getGekoppeldAfschrift());
        return afschrift==null?0:afschrift.getBedrag();
    }

    protected String getRekeningNummer(Rekening rekening, CheckAndFixData data) {
        Afschrift afschrift = data.afschriftMap.get(rekening.getGekoppeldAfschrift());
        return afschrift==null?"":afschrift.getRekeningNummer();
    }

    protected String getFactuurNummer(Factuur factuur, CheckAndFixData data) {
        Afschrift afschrift = data.afschriftMap.get(factuur.getGekoppeldAfschrift());
        return afschrift==null?"":factuur.getFactuurNummer();
    }



}
