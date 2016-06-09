package com.vdzon.administratie.overzicht;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vdzon.administratie.auth.SessionHelper;
import com.vdzon.administratie.crud.UserCrud;
import com.vdzon.administratie.dto.FactuurDto;
import com.vdzon.administratie.model.Bestelling;
import com.vdzon.administratie.model.Factuur;
import com.vdzon.administratie.model.Gebruiker;
import com.vdzon.administratie.pdfgenerator.GenerateFactuur;
import com.vdzon.administratie.pdfgenerator.GenerateOverzicht;
import com.vdzon.administratie.util.SingleAnswer;
import spark.Request;
import spark.Response;

import javax.inject.Inject;
import java.io.BufferedOutputStream;

public class OverzichtService {

    @Inject
    UserCrud crudService;

    protected Object getPdf(Request req, Response res) throws Exception {
        try {
            String uuid = SessionHelper.getAuthenticatedUserUuid(req);
            Gebruiker gebruiker = crudService.getGebruiker(uuid);
            if (gebruiker == null) {
                res.status(404);
                return new SingleAnswer("not found");
            }
            String beginDate = req.params(":beginDate");
            String endDate = req.params(":endDate");
            if ("endDate".equals(endDate)) {
                res.status(404);
                return new SingleAnswer("not found");
            }
            if ("beginDate".equals(beginDate)) {
                res.status(404);
                return new SingleAnswer("not found");
            }

            res.raw().setContentType("application/pdf");
            res.raw().setHeader("Content-Disposition","attachment; filename=overzicht_"+beginDate+"_"+endDate+".pdf");
            try (BufferedOutputStream zipOutputStream = new BufferedOutputStream(res.raw().getOutputStream())) {
                GenerateOverzicht.buildPdf(gebruiker.getDefaultAdministratie(), beginDate, endDate, zipOutputStream);
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
