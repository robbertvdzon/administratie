package com.vdzon.administratie.rest.rekening;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vdzon.administratie.util.SessionHelper;
import com.vdzon.administratie.crud.UserCrud;
import com.vdzon.administratie.dto.RekeningDto;
import com.vdzon.administratie.model.Gebruiker;
import com.vdzon.administratie.model.Rekening;
import com.vdzon.administratie.util.SingleAnswer;
import spark.Request;
import spark.Response;

import javax.inject.Inject;

public class RekeningService {

    @Inject
    UserCrud crudService;

    protected Object putRekening(Request req, Response res) throws Exception {
        Gebruiker gebruiker = SessionHelper.getGebruikerOrThowForbiddenExceptin(req, crudService);
        String contactJson = req.body();
        Rekening rekening = null;
        ObjectMapper mapper = new ObjectMapper();
        RekeningDto rekeningDto = mapper.readValue(contactJson, RekeningDto.class);
        rekening = rekeningDto.toRekening();

        gebruiker.getDefaultAdministratie().removeRekening(rekening.getUuid());
        gebruiker.getDefaultAdministratie().addRekening(rekening);
        crudService.updateGebruiker(gebruiker);
        return new SingleAnswer("ok");
    }

    protected Object removeRekening(Request req, Response res) throws Exception {
        Gebruiker gebruiker = SessionHelper.getGebruikerOrThowForbiddenExceptin(req, crudService);
        String rekeningUuid = req.params(":uuid");
        if ("undefined".equals(rekeningUuid)) {
            rekeningUuid = null;
        }
        gebruiker.getDefaultAdministratie().removeRekening(rekeningUuid);
        crudService.updateGebruiker(gebruiker);
        return new SingleAnswer("ok");
    }

}
