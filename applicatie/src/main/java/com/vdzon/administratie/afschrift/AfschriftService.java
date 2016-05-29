package com.vdzon.administratie.afschrift;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vdzon.administratie.auth.SessionHelper;
import com.vdzon.administratie.crud.UserCrud;
import com.vdzon.administratie.dto.AfschriftDto;
import com.vdzon.administratie.dto.ContactDto;
import com.vdzon.administratie.model.Afschrift;
import com.vdzon.administratie.model.Contact;
import com.vdzon.administratie.model.Gebruiker;
import com.vdzon.administratie.util.SingleAnswer;
import spark.Request;
import spark.Response;

import javax.inject.Inject;

public class AfschriftService {

    @Inject
    UserCrud crudService;

    protected Object putAfschrift(Request req, Response res) throws Exception {
        try {
            String uuid = SessionHelper.getAuthenticatedUserUuid(req);
            Gebruiker gebruiker = crudService.getGebruiker(uuid);
            if (gebruiker == null) {
                res.status(404);
                return new SingleAnswer("not found");
            }
            String afschriftJson = req.body();
            Afschrift afschrift = null;
            ObjectMapper mapper = new ObjectMapper();
            AfschriftDto afschriftDto = mapper.readValue(afschriftJson, AfschriftDto.class);
            afschrift = afschriftDto.toAfschrift();

            gebruiker.getDefaultAdministratie().removeAfschrift(afschrift.getUuid());
            gebruiker.getDefaultAdministratie().addAfschrift(afschrift);
            crudService.updateGebruiker(gebruiker);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
        return new SingleAnswer("ok");
    }

    protected Object removeAfschrift(Request req, Response res) throws Exception {
        try {
            String uuid = SessionHelper.getAuthenticatedUserUuid(req);
            Gebruiker gebruiker = crudService.getGebruiker(uuid);
            if (gebruiker == null) {
                res.status(404);
                return new SingleAnswer("not found");
            }
            String afschriftUuid = req.params(":uuid");
            if ("undefined".equals(afschriftUuid)) {
                afschriftUuid = null;
            }
            gebruiker.getDefaultAdministratie().removeAfschrift(afschriftUuid);
            crudService.updateGebruiker(gebruiker);
            return new SingleAnswer("ok");
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
    }
}
