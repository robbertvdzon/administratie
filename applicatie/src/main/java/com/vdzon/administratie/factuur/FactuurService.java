package com.vdzon.administratie.factuur;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vdzon.administratie.auth.SessionHelper;
import com.vdzon.administratie.crud.UserCrud;
import com.vdzon.administratie.dto.FactuurDto;
import com.vdzon.administratie.model.Factuur;
import com.vdzon.administratie.model.Gebruiker;
import com.vdzon.administratie.pdfgenerator.GenerateFactuur;
import com.vdzon.administratie.util.SingleAnswer;
import spark.Request;
import spark.Response;

import javax.inject.Inject;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;

public class FactuurService {

    @Inject
    UserCrud crudService;

    protected Object putFactuur(Request req, Response res) throws Exception {
        try {
            String uuid = SessionHelper.getAuthenticatedUserUuid(req);
            Gebruiker gebruiker = crudService.getGebruiker(uuid);
            if (gebruiker == null) {
                res.status(404);
                return new SingleAnswer("not found");
            }
            String factuurJson = req.body();
            Factuur factuur = null;
            ObjectMapper mapper = new ObjectMapper();
            FactuurDto factuurDto = mapper.readValue(factuurJson, FactuurDto.class);
            factuur = factuurDto.toFactuur();
            gebruiker.getDefaultAdministratie().removeFactuur(factuur.getUuid());
            gebruiker.getDefaultAdministratie().addFactuur(factuur);
            crudService.updateGebruiker(gebruiker);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
        return new SingleAnswer("ok");
    }

    protected Object removeFactuur(Request req, Response res) throws Exception {
        try {
            String uuid = SessionHelper.getAuthenticatedUserUuid(req);
            Gebruiker gebruiker = crudService.getGebruiker(uuid);
            if (gebruiker == null) {
                res.status(404);
                return new SingleAnswer("not found");
            }
            String factuurUuid = req.params(":uuid");
            if ("undefined".equals(factuurUuid)) {
                factuurUuid = null;
            }
            gebruiker.getDefaultAdministratie().removeFactuur(factuurUuid);
            crudService.updateGebruiker(gebruiker);
            return new SingleAnswer("ok");
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }

    }

    protected Object getPdf(Request req, Response res) throws Exception {
        try {
            String uuid = SessionHelper.getAuthenticatedUserUuid(req);
            Gebruiker gebruiker = crudService.getGebruiker(uuid);
            if (gebruiker == null) {
                res.status(404);
                return new SingleAnswer("not found");
            }
            String factuurUuid = req.params(":uuid");
            if ("undefined".equals(factuurUuid)) {
                res.status(404);
                return new SingleAnswer("not found");
            }

            Factuur factuur = gebruiker.getDefaultAdministratie().getFactuur(factuurUuid);
            res.raw().setContentType("application/pdf");
            res.raw().setHeader("Content-Disposition","attachment; filename="+factuur.getFactuurNummer()+".pdf");
            try (BufferedOutputStream zipOutputStream = new BufferedOutputStream(res.raw().getOutputStream())) {
                GenerateFactuur.buildPdf(gebruiker.getDefaultAdministratie(), factuur, zipOutputStream);
                zipOutputStream.flush();
                zipOutputStream.close();
            }

            return null;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }

    }

}
