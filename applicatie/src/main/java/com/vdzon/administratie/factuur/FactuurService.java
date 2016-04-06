package com.vdzon.administratie.factuur;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vdzon.administratie.auth.SessionHelper;
import com.vdzon.administratie.crud.UserCrud;
import com.vdzon.administratie.model.Factuur;
import com.vdzon.administratie.model.Gebruiker;
import com.vdzon.administratie.util.SingleAnswer;
import spark.Request;
import spark.Response;

import javax.inject.Inject;
import java.util.Random;
import java.util.UUID;

public class FactuurService {

    @Inject
    UserCrud crudService;

    protected Object putFactuur(Request req, Response res) throws Exception{
        String uuid = SessionHelper.getAuthenticatedUserUuid(req);
        Gebruiker gebruiker = crudService.getGebruiker(uuid);
        if (gebruiker == null){
            res.status(404);
            return new SingleAnswer("not found");
        }
//        String factuurJson = req.queryParams("factuur");
        String factuurJson = req.body();
        Factuur factuur = null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            factuur = mapper.readValue(factuurJson, Factuur.class);
        } catch (JsonParseException e){
            // Hey, you did not send a valid request!
        }
        Factuur newFactuur = new Factuur(factuur.getOmschrijving(),factuur.getFactuurNummer(),factuur.getDatum(),factuur.getKlant(), factuur.isBetaald(),factuur.getFactuurRegels(), UUID.randomUUID().toString());

        gebruiker.removeFactuur(factuur.getFactuurNummer());
        gebruiker.addFactuur(newFactuur);
        crudService.updateGebruiker(gebruiker);
        return new SingleAnswer("ok");
    }

    protected Object removeFactuur(Request req, Response res) throws Exception{
        String uuid = SessionHelper.getAuthenticatedUserUuid(req);
        Gebruiker gebruiker = crudService.getGebruiker(uuid);
        if (gebruiker == null){
            res.status(404);
            return new SingleAnswer("not found");
        }
        String factuurNummer = req.params(":factuurNummer");
        gebruiker.removeFactuur(factuurNummer);
        crudService.updateGebruiker(gebruiker);
        return new SingleAnswer("ok");
    }
}
