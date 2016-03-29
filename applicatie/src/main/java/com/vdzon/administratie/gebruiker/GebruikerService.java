package com.vdzon.administratie.gebruiker;

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

public class GebruikerService {

    @Inject
    UserCrud crudService;

    protected Object postGebruiker(Request req, Response res) throws Exception{
        String uuid = SessionHelper.getAuthenticatedUserUuid(req);
        Gebruiker gebruiker = crudService.getGebruiker(uuid);
        if (gebruiker == null){
            res.status(404);
            return new SingleAnswer("not found");
        }
        String nieuweGebruikerJson = req.body();
        Gebruiker nieuweGebruiker = null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            nieuweGebruiker = mapper.readValue(nieuweGebruikerJson, Gebruiker.class);
        } catch (JsonParseException e){
            // Hey, you did not send a valid request!
        }
        // copy values from new user
        gebruiker.setName(nieuweGebruiker.getName());


        crudService.updateGebruiker(gebruiker);
        return new SingleAnswer("ok");
    }

    protected Object removeGebruiker(Request req, Response res) throws Exception{
        String uuid = SessionHelper.getAuthenticatedUserUuid(req);
        Gebruiker gebruiker = crudService.getGebruiker(uuid);
        if (gebruiker == null){
            res.status(404);
            return new SingleAnswer("not found");
        }
        crudService.deleteGebruiker(gebruiker.getUuid());
        return new SingleAnswer("ok");
    }
}
