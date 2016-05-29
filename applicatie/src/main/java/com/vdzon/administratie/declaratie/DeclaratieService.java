package com.vdzon.administratie.declaratie;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vdzon.administratie.auth.SessionHelper;
import com.vdzon.administratie.crud.UserCrud;
import com.vdzon.administratie.dto.ContactDto;
import com.vdzon.administratie.dto.DeclaratieDto;
import com.vdzon.administratie.model.Contact;
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
        try {
            String uuid = SessionHelper.getAuthenticatedUserUuid(req);
            Gebruiker gebruiker = crudService.getGebruiker(uuid);
            if (gebruiker == null) {
                res.status(404);
                return new SingleAnswer("not found");
            }
            String declaratieJson = req.body();
            Declaratie declaratie = null;
            ObjectMapper mapper = new ObjectMapper();
            DeclaratieDto declaratieDto = mapper.readValue(declaratieJson, DeclaratieDto.class);
            declaratie = declaratieDto.toDeclaratie();

            gebruiker.getDefaultAdministratie().removeDeclaratie(declaratie.getUuid());
            gebruiker.getDefaultAdministratie().addDeclaratie(declaratie);
            crudService.updateGebruiker(gebruiker);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
        return new SingleAnswer("ok");
    }

    protected Object removeDeclaratie(Request req, Response res) throws Exception {
        try {
            String uuid = SessionHelper.getAuthenticatedUserUuid(req);
            Gebruiker gebruiker = crudService.getGebruiker(uuid);
            if (gebruiker == null) {
                res.status(404);
                return new SingleAnswer("not found");
            }
            String declaratieUuid = req.params(":uuid");
            if ("undefined".equals(declaratieUuid)) {
                declaratieUuid = null;
            }
            gebruiker.getDefaultAdministratie().removeDeclaratie(declaratieUuid);
            crudService.updateGebruiker(gebruiker);
            return new SingleAnswer("ok");
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }

    }

}
