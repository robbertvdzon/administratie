package com.vdzon.administratie.gebruiker;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vdzon.administratie.auth.SessionHelper;
import com.vdzon.administratie.crud.UserCrud;
import com.vdzon.administratie.dto.FactuurDto;
import com.vdzon.administratie.dto.GebruikerDto;
import com.vdzon.administratie.model.Factuur;
import com.vdzon.administratie.model.Gebruiker;
import com.vdzon.administratie.util.SingleAnswer;
import spark.Request;
import spark.Response;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class GebruikerService {

    @Inject
    UserCrud crudService;

    protected Object postGebruiker(Request req, Response res) throws Exception {
        try {
            String uuid = SessionHelper.getAuthenticatedUserUuid(req);
            Gebruiker gebruiker = crudService.getGebruiker(uuid);
            if (gebruiker == null) {
                res.status(404);
                return new SingleAnswer("not found");
            }
            String nieuweGebruikerJson = req.body();
            ObjectMapper mapper = new ObjectMapper();
            GebruikerDto nieuweGebruikerDto = mapper.readValue(nieuweGebruikerJson, GebruikerDto.class);
            Gebruiker originalGebruiker = crudService.getGebruiker(nieuweGebruikerDto.getUuid());
            if (originalGebruiker == null){
                originalGebruiker = new Gebruiker();
            }
            Gebruiker updatedGebruiker  = nieuweGebruikerDto.cloneGebruikerAndCopyDtoFields(originalGebruiker);
            crudService.updateGebruiker(updatedGebruiker );
            return new SingleAnswer("ok");
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    protected Object removeGebruiker(Request req, Response res) throws Exception {
        String uuid = SessionHelper.getAuthenticatedUserUuid(req);
        Gebruiker gebruiker = crudService.getGebruiker(uuid);
        if (gebruiker == null) {
            res.status(404);
            return new SingleAnswer("not found");
        }
        String gebruikerUuid = req.params(":uuid");
        if ("undefined".equals(gebruikerUuid)) {
            gebruikerUuid = null;
        }
        crudService.deleteGebruiker(gebruikerUuid);;
        return new SingleAnswer("ok");
    }

    protected Object updatePassword(Request req, Response res) throws Exception {
        String uuid = SessionHelper.getAuthenticatedUserUuid(req);
        Gebruiker gebruiker = crudService.getGebruiker(uuid);
        if (gebruiker == null) {
            res.status(404);
            return new SingleAnswer("not found");
        }
        String gebruikerUuid = req.params(":uuid");
        String newPassword = req.params(":newPassword");
        Gebruiker gebruikerToChange = crudService.getGebruiker(gebruikerUuid);
        gebruikerToChange.setPassword(newPassword);
        crudService.updateGebruiker(gebruikerToChange);
        return new SingleAnswer("ok");
    }

}
