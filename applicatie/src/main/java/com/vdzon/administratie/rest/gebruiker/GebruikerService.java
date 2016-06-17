package com.vdzon.administratie.rest.gebruiker;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vdzon.administratie.crud.UserCrud;
import com.vdzon.administratie.dto.GebruikerDto;
import com.vdzon.administratie.model.Gebruiker;
import com.vdzon.administratie.util.SessionHelper;
import com.vdzon.administratie.util.SingleAnswer;
import spark.Request;
import spark.Response;

import javax.inject.Inject;

public class GebruikerService {

    @Inject
    UserCrud crudService;

    protected Object postGebruiker(Request req, Response res) throws Exception {
        Gebruiker gebruiker = SessionHelper.getGebruikerOrThowForbiddenExceptin(req, crudService);
        String nieuweGebruikerJson = req.body();
        ObjectMapper mapper = new ObjectMapper();
        GebruikerDto nieuweGebruikerDto = mapper.readValue(nieuweGebruikerJson, GebruikerDto.class);
        Gebruiker originalGebruiker = crudService.getGebruiker(nieuweGebruikerDto.getUuid());
        if (originalGebruiker == null) {
            originalGebruiker = new Gebruiker();
        }
        Gebruiker updatedGebruiker = nieuweGebruikerDto.cloneGebruikerWithDtoFields(originalGebruiker);
        crudService.updateGebruiker(updatedGebruiker);
        return new SingleAnswer("ok");
    }

    protected Object removeGebruiker(Request req, Response res) throws Exception {
        Gebruiker gebruiker = SessionHelper.getGebruikerOrThowForbiddenExceptin(req, crudService);
        String gebruikerUuid = req.params(":uuid");
        if ("undefined".equals(gebruikerUuid)) {
            gebruikerUuid = null;
        }
        crudService.deleteGebruiker(gebruikerUuid);
        ;
        return new SingleAnswer("ok");
    }

    protected Object updatePassword(Request req, Response res) throws Exception {
        Gebruiker gebruiker = SessionHelper.getGebruikerOrThowForbiddenExceptin(req, crudService);
        String gebruikerUuid = req.params(":uuid");
        String newPassword = req.params(":newPassword");
        Gebruiker gebruikerToChange = crudService.getGebruiker(gebruikerUuid);
        Gebruiker changedGebruiker = gebruikerToChange.toBuilder().password(newPassword).build();
        crudService.updateGebruiker(changedGebruiker);
        return new SingleAnswer("ok");
    }

}
