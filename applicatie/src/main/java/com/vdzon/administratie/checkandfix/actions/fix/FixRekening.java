package com.vdzon.administratie.checkandfix.actions.fix;

import com.vdzon.administratie.checkandfix.model.CheckAndFixRegel;
import com.vdzon.administratie.checkandfix.model.FixAction;
import com.vdzon.administratie.model.*;

public class FixRekening {

    @AdministratieFix
    public void fixRekening(CheckAndFixRegel regel, Gebruiker gebruiker) {
        if (regel.getRubriceerAction() != FixAction.REMOVE_REF_FROM_REKENING) return;
        Rekening rekening = gebruiker.getDefaultAdministratie().getRekeningen().stream().filter(rek -> rek.getRekeningNummer().equals(regel.getData())).findFirst().orElse(null);
        if (rekening == null) return;
        gebruiker.getDefaultAdministratie().removeRekening((rekening.getUuid()));
        gebruiker.getDefaultAdministratie().addRekening(rekening.toBuilder().gekoppeldAfschrift("").build());
    }

}
