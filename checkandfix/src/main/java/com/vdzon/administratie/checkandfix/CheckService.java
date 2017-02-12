package com.vdzon.administratie.checkandfix;

import com.vdzon.administratie.checkandfix.actions.check.BedragenCheck2;
import com.vdzon.administratie.checkandfix.model.CheckAndFixRegel;
import com.vdzon.administratie.model.Administratie;
import com.vdzon.administratie.model.BoekingenCache;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

/**
 * Created by robbe on 2/12/2017.
 */
public class CheckService {

    private static Function<CheckAndFixData2, List<CheckAndFixRegel>> checkOfRekeningenVolledigBetaaldZijn = (data) -> BedragenCheck2.INSTANCE.checkOfRekeningenVolledigBetaaldZijn(data);

    private static List<Function<CheckAndFixData2, List<CheckAndFixRegel>>> checkFunctions = Arrays.asList(
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
        CheckAndFixData2 checkAndFixData = populateCheckAndFixData(administratie);
        List<CheckAndFixRegel> regels = new ArrayList<>();
        checkFunctions.forEach(f -> {
//                f(checkAndFixData);
//            CheckAndFixRegel apply = f.apply(checkAndFixData);
            regels.addAll(f.apply(checkAndFixData));
        });
        return regels;
    }

    private static CheckAndFixData2 populateCheckAndFixData(Administratie administratie) {
        return new CheckAndFixData2(
                administratie.getAfschriften(),
                administratie.getRekeningen(),
                administratie.getFacturen(),
                administratie.getBoekingen(),
                new BoekingenCache(administratie.getBoekingen()));

    }

}
