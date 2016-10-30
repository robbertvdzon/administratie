package com.vdzon.administratie.rest.overzicht;

import com.vdzon.administratie.checkandfix.CheckAndFixModule;
import com.vdzon.administratie.util.SessionHelper;
import com.vdzon.administratie.crud.UserCrud;
import com.vdzon.administratie.model.Gebruiker;
import com.vdzon.administratie.pdfgenerator.overzicht.GenerateOverzicht;
import com.vdzon.administratie.util.SingleAnswer;
import spark.Request;
import spark.Response;

import javax.inject.Inject;
import java.io.BufferedOutputStream;

public class OverzichtService {

    @Inject
    UserCrud crudService;

    @Inject
    CheckAndFixModule checkAndFixModule;

    protected Object getPdf(Request req, Response res) throws Exception {
        Gebruiker gebruiker = SessionHelper.getGebruikerOrThowForbiddenExceptin(req, crudService);
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
                GenerateOverzicht.buildPdf(gebruiker.getDefaultAdministratie(), beginDate, endDate, zipOutputStream, checkAndFixModule);
                zipOutputStream.flush();
                zipOutputStream.close();
            }

            return null;
    }

}
