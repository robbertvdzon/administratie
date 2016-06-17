package com.vdzon.administratie.rest.administratie;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vdzon.administratie.util.SessionHelper;
import com.vdzon.administratie.crud.UserCrud;
import com.vdzon.administratie.dto.AdministratieGegevensDto;
import com.vdzon.administratie.model.Administratie;
import com.vdzon.administratie.model.AdministratieGegevens;
import com.vdzon.administratie.model.Gebruiker;
import com.vdzon.administratie.util.SingleAnswer;
import spark.Request;
import spark.Response;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;

@Singleton
public class AdministratieService {

    @Inject
    UserCrud userCrud;

    protected Object putAdministratie(Request req, Response res) throws Exception {
        Gebruiker gebruiker = SessionHelper.getGebruikerOrThowForbiddenExceptin(req, userCrud);

        String administratieGegevensJson = req.body();
        ObjectMapper mapper = new ObjectMapper();
        AdministratieGegevensDto administratieGegevensDto = mapper.readValue(administratieGegevensJson, AdministratieGegevensDto.class);
        AdministratieGegevens administratieGegevens = administratieGegevensDto.toAdministratieGegevens();

        Administratie administratie = gebruiker.getDefaultAdministratie().toBuilder().administratieGegevens(administratieGegevens).build();
        ArrayList<Administratie> administraties = new ArrayList<>();
        administraties.add(administratie);
        Gebruiker gebruikerToUpdate = gebruiker.toBuilder().administraties(administraties).build();
        userCrud.updateGebruiker(gebruikerToUpdate);
        return new SingleAnswer("ok");
    }
}
