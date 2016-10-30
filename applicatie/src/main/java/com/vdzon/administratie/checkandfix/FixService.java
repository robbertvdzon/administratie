package com.vdzon.administratie.checkandfix;

import com.vdzon.administratie.checkandfix.actions.fix.BoekingenFix;
import com.vdzon.administratie.checkandfix.model.CheckAndFixRegel;
import com.vdzon.administratie.model.Administratie;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class FixService {

    private CheckService checkService;
    private List<FixFunction> fixFunctions;

    @Inject
    public FixService(CheckService checkService) {
        fixFunctions = new ArrayList<>();
        fixFunctions.add(BoekingenFix::fixAfschrift);
        this.checkService = checkService;
    }

    public Administratie getFixedAdministratie(Administratie administratie) throws Exception {
        List<CheckAndFixRegel> regelsToFix = checkService.getCheckAndFixRegels(administratie);
        final Wrapper wrapper = new Wrapper();
        wrapper.adm = administratie;
        regelsToFix.stream()
                .forEach(regel -> fixFunctions.stream().forEach(f -> wrapper.adm = f.fixAfschrift(regel, wrapper.adm))
                );
        return wrapper.adm;
    }

    class Wrapper {
        public Administratie adm = null;
    }

}
