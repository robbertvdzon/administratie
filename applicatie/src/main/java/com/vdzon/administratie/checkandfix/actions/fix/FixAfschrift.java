package com.vdzon.administratie.checkandfix.actions.fix;

import com.vdzon.administratie.checkandfix.model.CheckAndFixRegel;
import com.vdzon.administratie.checkandfix.model.FixAction;
import com.vdzon.administratie.dto.BoekingType;
import com.vdzon.administratie.model.*;

public class FixAfschrift {

//    @AdministratieFix
//    public void fixAfschrift(CheckAndFixRegel regel, Gebruiker gebruiker) {
//        if (regel.getRubriceerAction() != FixAction.REMOVE_REF_FROM_AFSCHRIFT) return;
//        Afschrift afschrift = regel.getAfschrift().toAfschrift();
//        gebruiker.getDefaultAdministratie().removeAfschrift(afschrift.getNummer());
//        Afschrift modifiedAfschrift = afschrift.toBuilder()
//                .boekingType(BoekingType.NONE)
//                .factuurNummer("")
//                .rekeningNummer("")
//                .build();
//        gebruiker.getDefaultAdministratie().addAfschrift(modifiedAfschrift);
//    }

}
