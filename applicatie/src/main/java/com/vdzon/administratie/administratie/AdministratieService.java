package com.vdzon.administratie.administratie;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vdzon.administratie.auth.SessionHelper;
import com.vdzon.administratie.crud.UserCrud;
import com.vdzon.administratie.dto.*;
import com.vdzon.administratie.model.Administratie;
import com.vdzon.administratie.model.AdministratieGegevens;
import com.vdzon.administratie.model.Contact;
import com.vdzon.administratie.model.Gebruiker;
import com.vdzon.administratie.util.SingleAnswer;
import spark.Request;
import spark.Response;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Singleton
public class AdministratieService {

    @Inject
    UserCrud userCrud;

    protected Object putAdministratie(Request req, Response res) throws Exception {
        try {
            String uuid = SessionHelper.getAuthenticatedUserUuid(req);
            if (uuid == null) {
                res.status(404);
                return new SingleAnswer("not found");
            }
            Gebruiker gebruiker = userCrud.getGebruiker(uuid);

            String administratieGegevensJson = req.body();
            ObjectMapper mapper = new ObjectMapper();
            AdministratieGegevensDto administratieGegevensDto = mapper.readValue(administratieGegevensJson, AdministratieGegevensDto.class);
            AdministratieGegevens administratieGegevens = administratieGegevensDto.toAdministratieGegevens();

            Administratie administratie = gebruiker.getDefaultAdministratie().toBuilder().administratieGegevens(administratieGegevens).build();
            ArrayList<Administratie> administraties = new ArrayList<>();
            administraties.add(administratie);
            Gebruiker gebruikerToUpdate = gebruiker.toBuilder().administraties(administraties).build();
            userCrud.updateGebruiker(gebruikerToUpdate);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
        return new SingleAnswer("ok");
    }
}
