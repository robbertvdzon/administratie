package com.vdzon.administratie.checkandfix.actions.fix;

import com.vdzon.administratie.checkandfix.model.CheckAndFixRegel;
import com.vdzon.administratie.checkandfix.model.FixAction;
import com.vdzon.administratie.dto.BoekingType;
import com.vdzon.administratie.model.*;

public class FixBoeking {

    @AdministratieFix
    public void fixAfschrift(CheckAndFixRegel regel, Gebruiker gebruiker) {
        if (regel.getRubriceerAction() != FixAction.REMOVE_BOEKING) return;
        gebruiker.getDefaultAdministratie().removeBoeking(regel.getBoekingUuid());
    }

}
