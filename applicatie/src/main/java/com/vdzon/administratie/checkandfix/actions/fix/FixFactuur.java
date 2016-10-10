package com.vdzon.administratie.checkandfix.actions.fix;

import com.vdzon.administratie.checkandfix.model.CheckAndFixRegel;
import com.vdzon.administratie.checkandfix.model.FixAction;
import com.vdzon.administratie.model.*;

public class FixFactuur {

//    @AdministratieFix
//    public void fixFactuur(CheckAndFixRegel regel, Gebruiker gebruiker) {
//        if (regel.getRubriceerAction() != FixAction.REMOVE_REF_FROM_FACTUUR) return;
//        Factuur factuur = gebruiker.getDefaultAdministratie().getFacturen().stream().filter(fak -> fak.getFactuurNummer().equals(regel.getData())).findFirst().orElse(null);
//        if (factuur == null) return;
//        gebruiker.getDefaultAdministratie().removeFactuur(factuur.getUuid());
//        gebruiker.getDefaultAdministratie().addFactuur(factuur.toBuilder().gekoppeldAfschrift(null).build());
//    }

}
