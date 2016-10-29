package com.vdzon.administratie.checkandfix.actions.fix;

import com.vdzon.administratie.checkandfix.model.CheckAndFixRegel;
import com.vdzon.administratie.checkandfix.model.FixAction;
import com.vdzon.administratie.dto.BoekingType;
import com.vdzon.administratie.model.*;
import com.vdzon.administratie.model.boekingen.Boeking;

import java.util.ArrayList;
import java.util.List;

public class FixBoeking {

    @AdministratieFix
    public Administratie fixAfschrift(CheckAndFixRegel regel, Administratie administratie) {
        if (regel.getRubriceerAction() != FixAction.REMOVE_BOEKING) return administratie;
        ArrayList<Boeking> updatedBoekingenList = new ArrayList<>(administratie.getBoekingen());
        updatedBoekingenList.remove(administratie.getBoekingen().stream().filter(b->b.getUuid().equals(regel.getBoekingUuid())).findFirst().orElse(null) );
        return Administratie.newBuilder(administratie).boekingen(updatedBoekingenList).build();
    }


}
