package com.vdzon.administratie.checkandfix.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vdzon.administratie.auth.SessionHelper;
import com.vdzon.administratie.checkandfix.model.CheckAndFixRegel;
import com.vdzon.administratie.checkandfix.model.CheckType;
import com.vdzon.administratie.checkandfix.model.FixAction;
import com.vdzon.administratie.checkandfix.model.CheckAndFixRegels;
import com.vdzon.administratie.crud.UserCrud;
import com.vdzon.administratie.dto.AfschriftDto;
import com.vdzon.administratie.model.*;
import com.vdzon.administratie.util.SingleAnswer;
import spark.Request;
import spark.Response;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public class CheckAndFixService {

    @Inject
    UserCrud crudService;

    protected Object getCheckAndFixRegels(Request req, Response res) throws Exception {
        try {
            String uuid = SessionHelper.getAuthenticatedUserUuid(req);
            Gebruiker gebruiker = crudService.getGebruiker(uuid);
            if (gebruiker == null) {
                res.status(404);
                return new SingleAnswer("not found");
            }

            List<CheckAndFixRegel> regels = getCheckAndFixRegels(gebruiker);
            return new CheckAndFixRegels(regels);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
    }

    private List<CheckAndFixRegel> getCheckAndFixRegels(Gebruiker gebruiker) {
        List<CheckAndFixRegel> regels = new ArrayList();
        List<Afschrift> afschriften = gebruiker.getDefaultAdministratie().getAfschriften();
        regels.add(new CheckAndFixRegel(FixAction.REMOVE_REF_FROM_AFSCHRIFT, CheckType.FIX_NEEDED,new AfschriftDto(afschriften.get(0))));
        regels.add(new CheckAndFixRegel(FixAction.REMOVE_REF_FROM_FACTUUR, CheckType.FIX_NEEDED,new AfschriftDto(afschriften.get(1))));
        regels.add(new CheckAndFixRegel(FixAction.REMOVE_REF_FROM_REKENING, CheckType.FIX_NEEDED,new AfschriftDto(afschriften.get(2))));
        regels.add(new CheckAndFixRegel(FixAction.NONE, CheckType.BEDRAG_INCORRECT,new AfschriftDto(afschriften.get(3))));
        regels.add(new CheckAndFixRegel(FixAction.NONE, CheckType.BEDRAG_INCORRECT,new AfschriftDto(afschriften.get(4))));
        return regels;
    }

    protected Object fix(Request req, Response res) throws Exception {
        try {
            String uuid = SessionHelper.getAuthenticatedUserUuid(req);
            Gebruiker gebruiker = crudService.getGebruiker(uuid);
            if (gebruiker == null) {
                res.status(404);
                return new SingleAnswer("not found");
            }
            String regelsJson = req.body();
            ObjectMapper mapper = new ObjectMapper();
//            CheckAndFixRegels rubriceerRegels = mapper.readValue(regelsJson, CheckAndFixRegels.class);

//            System.out.println("rubriceer:");
//            rubriceerRegels.getCheckAndFixRegels().stream().forEach(regel-> processRegel(regel, gebruiker));
//            crudService.updateGebruiker(gebruiker);

        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
        return new SingleAnswer("ok");
    }

    private Afschrift findAfschrift(String uuid, Gebruiker gebruiker){
        return gebruiker.getDefaultAdministratie().getAfschriften().stream().filter(afschrift -> afschrift.getUuid().equals(uuid)).findFirst().orElse(null);
    }

    private int findNextRekeningNummer(Gebruiker gebruiker){
        return 1+gebruiker.getDefaultAdministratie().getRekeningen().stream().map(rekening->Integer.parseInt(rekening.getRekeningNummer())).max(Comparator.naturalOrder()).orElse(1000);
    }


}
