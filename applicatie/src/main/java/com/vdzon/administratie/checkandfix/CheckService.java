package com.vdzon.administratie.checkandfix;

import com.vdzon.administratie.checkandfix.actions.check.BedragenCheck;
import com.vdzon.administratie.checkandfix.actions.check.BestaanCheck;
import com.vdzon.administratie.checkandfix.actions.check.DubbeleNummersCheck;
import com.vdzon.administratie.checkandfix.actions.fix.BoekingenFix;
import com.vdzon.administratie.checkandfix.model.CheckAndFixRegel;
import com.vdzon.administratie.checkandfix.model.CheckType;
import com.vdzon.administratie.model.Administratie;
import com.vdzon.administratie.model.BoekingenCache;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CheckService {

    private List<CheckFunction> checkFunctions;

    public CheckService() {
        checkFunctions = new ArrayList<>();
        checkFunctions.add(BestaanCheck::checkOfAfschriftNogBestaat);
        checkFunctions.add(BestaanCheck::checkOfFactuurNogBestaat);
        checkFunctions.add(BestaanCheck::checkOfRekeningNogBestaat);
        checkFunctions.add(BedragenCheck::checkOfFacturenVolledigBetaaldZijn);
        checkFunctions.add(BedragenCheck::checkOfRekeningenVolledigBetaaldZijn);
        checkFunctions.add(DubbeleNummersCheck::checkAfschriftenMetHetzelfdeNummer);
        checkFunctions.add(DubbeleNummersCheck::checkFacturenMetDezelfdeFactuurNummer);
        checkFunctions.add(DubbeleNummersCheck::checkRekeningenMetHetzelfdeRekeningNummer);
    }

    public List<CheckAndFixRegel> getCheckAndFixRegels(Administratie administratie) {
        CheckAndFixData checkAndFixData = populateCheckAndFixData(administratie);
        List<CheckAndFixRegel> regels = new ArrayList();
        checkFunctions.stream()
                .forEach(f -> regels.addAll(f.checkFunction(checkAndFixData)));
        return regels;
    }

    private CheckAndFixData populateCheckAndFixData(Administratie administratie) {
        CheckAndFixData checkAndFixData = new CheckAndFixData();
        checkAndFixData.alleAfschriften = administratie.getAfschriften();
        checkAndFixData.alleRekeningen = administratie.getRekeningen();
        checkAndFixData.alleFacturen = administratie.getFacturen();
        checkAndFixData.alleBoekingen = administratie.getBoekingen();
        checkAndFixData.boekingenCache = new BoekingenCache(administratie.getBoekingen());
        return checkAndFixData;
    }

    class Wrapper {
        public Administratie adm = null;
    }

}
