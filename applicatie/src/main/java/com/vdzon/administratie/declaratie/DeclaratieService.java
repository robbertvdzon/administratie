package com.vdzon.administratie.declaratie;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vdzon.administratie.auth.SessionHelper;
import com.vdzon.administratie.crud.UserCrud;
import com.vdzon.administratie.dto.DeclaratieDto;
import com.vdzon.administratie.model.Declaratie;
import com.vdzon.administratie.model.Gebruiker;
import com.vdzon.administratie.util.SingleAnswer;
import spark.Request;
import spark.Response;

import javax.inject.Inject;

public class DeclaratieService {

    @Inject
    UserCrud crudService;

    protected Object putDeclaratie(Request req, Response res) throws Exception {
        Gebruiker gebruiker = SessionHelper.getGebruikerOrThowForbiddenExceptin(req, crudService);
        String declaratieJson = req.body();
        Declaratie declaratie = null;
        ObjectMapper mapper = new ObjectMapper();
        DeclaratieDto declaratieDto = mapper.readValue(declaratieJson, DeclaratieDto.class);
        declaratie = declaratieDto.toDeclaratie();

        gebruiker.getDefaultAdministratie().removeDeclaratie(declaratie.getUuid());
        gebruiker.getDefaultAdministratie().addDeclaratie(declaratie);
        crudService.updateGebruiker(gebruiker);
        return new SingleAnswer("ok");
    }

    protected Object removeDeclaratie(Request req, Response res) throws Exception {
        Gebruiker gebruiker = SessionHelper.getGebruikerOrThowForbiddenExceptin(req, crudService);
        String declaratieUuid = req.params(":uuid");
        if ("undefined".equals(declaratieUuid)) {
            declaratieUuid = null;
        }
        gebruiker.getDefaultAdministratie().removeDeclaratie(declaratieUuid);
        crudService.updateGebruiker(gebruiker);
        return new SingleAnswer("ok");
    }
}
