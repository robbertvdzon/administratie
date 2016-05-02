package com.vdzon.administratie.factuur;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vdzon.administratie.auth.SessionHelper;
import com.vdzon.administratie.crud.UserCrud;
import com.vdzon.administratie.dto.FactuurDto;
import com.vdzon.administratie.model.Factuur;
import com.vdzon.administratie.model.Gebruiker;
import com.vdzon.administratie.util.SingleAnswer;
import spark.Request;
import spark.Response;

import javax.inject.Inject;
import java.util.UUID;

//import com.fasterxml.jackson.datatype.jsr310.JSR310Module;

public class FactuurService {

    @Inject
    UserCrud crudService;

    protected Object putFactuur(Request req, Response res) throws Exception {
        String uuid = SessionHelper.getAuthenticatedUserUuid(req);
        Gebruiker gebruiker = crudService.getGebruiker(uuid);
        if (gebruiker == null) {
            res.status(404);
            return new SingleAnswer("not found");
        }
        String factuurJson = req.body();
        Factuur factuur = null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            FactuurDto factuurDto = mapper.readValue(factuurJson, FactuurDto.class);
            factuur = factuurDto.toFactuur();
        } catch (JsonParseException e) {
            e.printStackTrace();
            // Hey, you did not send a valid request!
        }
//        Factuur newFactuur = new Factuur(factuur.getFactuurNummer(), factuur.getFactuurDate(), factuur.getKlant(), factuur.isBetaald(), factuur.getFactuurRegels(), UUID.randomUUID().toString());

        gebruiker.removeFactuur(factuur.getFactuurNummer());
        gebruiker.addFactuur(factuur);
        crudService.updateGebruiker(gebruiker);
        return new SingleAnswer("ok");
    }

    protected Object removeFactuur(Request req, Response res) throws Exception {
        String uuid = SessionHelper.getAuthenticatedUserUuid(req);
        Gebruiker gebruiker = crudService.getGebruiker(uuid);
        if (gebruiker == null) {
            res.status(404);
            return new SingleAnswer("not found");
        }
        String factuurNummer = req.params(":factuurNummer");
        gebruiker.removeFactuur(factuurNummer);
        crudService.updateGebruiker(gebruiker);
        return new SingleAnswer("ok");
    }
}
