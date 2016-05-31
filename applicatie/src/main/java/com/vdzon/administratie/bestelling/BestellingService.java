package com.vdzon.administratie.bestelling;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vdzon.administratie.auth.SessionHelper;
import com.vdzon.administratie.crud.UserCrud;
import com.vdzon.administratie.dto.BestellingDto;
import com.vdzon.administratie.dto.FactuurDto;
import com.vdzon.administratie.model.Bestelling;
import com.vdzon.administratie.model.Factuur;
import com.vdzon.administratie.model.Gebruiker;
import com.vdzon.administratie.pdfgenerator.GenerateFactuur;
import com.vdzon.administratie.util.SingleAnswer;
import spark.Request;
import spark.Response;

import javax.inject.Inject;
import java.io.BufferedOutputStream;

public class BestellingService {

    @Inject
    UserCrud crudService;

    protected Object putBestelling(Request req, Response res) throws Exception {
        try {
            String uuid = SessionHelper.getAuthenticatedUserUuid(req);
            Gebruiker gebruiker = crudService.getGebruiker(uuid);
            if (gebruiker == null) {
                res.status(404);
                return new SingleAnswer("not found");
            }
            String bestellingJson = req.body();
            Bestelling bestelling = null;
            ObjectMapper mapper = new ObjectMapper();
            BestellingDto bestellingDto = mapper.readValue(bestellingJson, BestellingDto.class);
            bestelling = bestellingDto.toBestelling();
            gebruiker.getDefaultAdministratie().removeBestelling(bestelling.getUuid());
            gebruiker.getDefaultAdministratie().addBestelling(bestelling);
            crudService.updateGebruiker(gebruiker);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
        return new SingleAnswer("ok");
    }

    protected Object removeBestelling(Request req, Response res) throws Exception {
        try {
            String uuid = SessionHelper.getAuthenticatedUserUuid(req);
            Gebruiker gebruiker = crudService.getGebruiker(uuid);
            if (gebruiker == null) {
                res.status(404);
                return new SingleAnswer("not found");
            }
            String bestellingUuid = req.params(":uuid");
            if ("undefined".equals(bestellingUuid)) {
                bestellingUuid = null;
            }
            gebruiker.getDefaultAdministratie().removeBestelling(bestellingUuid);
            crudService.updateGebruiker(gebruiker);
            return new SingleAnswer("ok");
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }

    }


}
