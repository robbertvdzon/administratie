package com.vdzon.administratie.rest.afschrift;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vdzon.administratie.bankimport.ImportFromAbnAmro;
import com.vdzon.administratie.util.SessionHelper;
import com.vdzon.administratie.crud.UserCrud;
import com.vdzon.administratie.dto.AfschriftDto;
import com.vdzon.administratie.model.Afschrift;
import com.vdzon.administratie.model.Gebruiker;
import com.vdzon.administratie.util.SingleAnswer;
import spark.Request;
import spark.Response;

import javax.inject.Inject;
import java.nio.file.Path;
import java.util.List;

public class AfschriftService {

    @Inject
    UserCrud crudService;

    protected Object putAfschrift(Request req, Response res) throws Exception {
        Gebruiker gebruiker = SessionHelper.getGebruikerOrThowForbiddenExceptin(req, crudService);
        String afschriftJson = req.body();
        ObjectMapper mapper = new ObjectMapper();
        AfschriftDto afschriftDto = mapper.readValue(afschriftJson, AfschriftDto.class);
        Afschrift afschrift = afschriftDto.toAfschrift();

        gebruiker.getDefaultAdministratie().removeAfschrift(afschrift.getNummer());
        gebruiker.getDefaultAdministratie().addAfschrift(afschrift);
        crudService.updateGebruiker(gebruiker);
        return new SingleAnswer("ok");
    }

    protected Object removeAfschrift(Request req, Response res) throws Exception {
        Gebruiker gebruiker = SessionHelper.getGebruikerOrThowForbiddenExceptin(req, crudService);
        String nummer = req.params(":nummer");
        if ("undefined".equals(nummer)) {
            nummer = null;
        }
        gebruiker.getDefaultAdministratie().removeAfschrift(nummer);
        crudService.updateGebruiker(gebruiker);
        return new SingleAnswer("ok");
    }

    protected Object uploadabn(Request request, Response response) {
        Gebruiker gebruiker = SessionHelper.getGebruikerOrThowForbiddenExceptin(request, crudService);
        Path uploadedFile = SessionHelper.getUploadedFile(request);
        List<Afschrift> afschriften = new ImportFromAbnAmro().parseFile(uploadedFile, gebruiker);
        updateAfschriftenVanGebruiker(gebruiker, afschriften);
        crudService.updateGebruiker(gebruiker);
        return "OK";
    }


    private void updateAfschriftenVanGebruiker(Gebruiker gebruiker, List<Afschrift> afschriften) {
        for (Afschrift afschrift : afschriften) {
            if (afschrift != null) {
                gebruiker.getDefaultAdministratie().removeAfschrift(afschrift.getNummer());
                gebruiker.getDefaultAdministratie().addAfschrift(afschrift);
            }
        }
    }


}
