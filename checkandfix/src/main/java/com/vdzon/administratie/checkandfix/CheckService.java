package com.vdzon.administratie.checkandfix;

import com.vdzon.administratie.checkandfix.actions.check.BedragenCheck2;
import com.vdzon.administratie.checkandfix.model.CheckAndFixRegel;
import com.vdzon.administratie.model.Administratie;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

/**
 * Created by robbe on 2/12/2017.
 */
public class CheckService {

    private static Function<CheckAndFixData, CheckAndFixRegel> checkOfRekeningenVolledigBetaaldZijn = (data) -> BedragenCheck2.checkOfRekeningenVolledigBetaaldZijn(data);

    private static List<Function<CheckAndFixData, CheckAndFixRegel>> checkFunctions = Arrays.asList(
//            checkOfFacturenVolledigBetaaldZijn,
            checkOfRekeningenVolledigBetaaldZijn
//            checkOfAfschriftNogBestaat,
//            checkOfFactuurNogBestaat,
//            checkOfRekeningNogBestaat,
//            checkAfschriftenMetHetzelfdeNummer,
//            checkFacturenMetDezelfdeFactuurNummer,
//            checkRekeningenMetHetzelfdeRekeningNummer
    );

    public static List<CheckAndFixRegel> getCheckAndFixRegels(Administratie administratie) {
        CheckAndFixData checkAndFixData = populateCheckAndFixData(administratie);
        List<CheckAndFixRegel> regels = new ArrayList<>();
        checkFunctions.forEach(f -> {
//                f(checkAndFixData);
//            CheckAndFixRegel apply = f.apply(checkAndFixData);
            regels.add(f.apply(checkAndFixData));
        });
        return regels;
    }

    private static CheckAndFixData populateCheckAndFixData(Administratie administratie) {
        return null;
    }
}
