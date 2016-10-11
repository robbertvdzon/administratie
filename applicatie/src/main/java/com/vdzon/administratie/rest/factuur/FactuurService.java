package com.vdzon.administratie.rest.factuur;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vdzon.administratie.util.SessionHelper;
import com.vdzon.administratie.crud.UserCrud;
import com.vdzon.administratie.dto.FactuurDto;
import com.vdzon.administratie.model.Bestelling;
import com.vdzon.administratie.model.Factuur;
import com.vdzon.administratie.model.Gebruiker;
import com.vdzon.administratie.pdfgenerator.factuur.GenerateFactuur;
import com.vdzon.administratie.util.SingleAnswer;
import spark.Request;
import spark.Response;

import javax.inject.Inject;
import java.io.BufferedOutputStream;

public class FactuurService {

    @Inject
    UserCrud crudService;

    protected Object putFactuur(Request req, Response res) throws Exception {
        Gebruiker gebruiker = SessionHelper.getGebruikerOrThowForbiddenExceptin(req, crudService);
        String factuurJson = req.body();
        Factuur factuur = null;
        ObjectMapper mapper = new ObjectMapper();
        FactuurDto factuurDto = mapper.readValue(factuurJson, FactuurDto.class);
        factuur = factuurDto.toFactuur();

        removeFactuur(gebruiker, factuur.getUuid());
        addFactuur(gebruiker, factuur);

        crudService.updateGebruiker(gebruiker);
        return new SingleAnswer("ok");
    }

    protected Object removeFactuur(Request req, Response res) throws Exception {
        Gebruiker gebruiker = SessionHelper.getGebruikerOrThowForbiddenExceptin(req, crudService);
        String factuurUuid = req.params(":uuid");
        if ("undefined".equals(factuurUuid)) {
            factuurUuid = null;
        }
        gebruiker.getDefaultAdministratie().removeFactuur(factuurUuid);
        crudService.updateGebruiker(gebruiker);
        return new SingleAnswer("ok");
    }


    private void addFactuur(Gebruiker gebruiker, Factuur factuur) {
        String nieuwBestellingNummer = factuur.getGekoppeldeBestellingNummer();
        Bestelling gekoppelBestelling = gebruiker.getDefaultAdministratie().getBestellingByBestellingNummer(nieuwBestellingNummer);
        if (gekoppelBestelling != null) {
            Bestelling updatedBestelling = gekoppelBestelling.toBuilder().gekoppeldFactuurNummer(factuur.getFactuurNummer()).build();
            gebruiker.getDefaultAdministratie().removeBestelling(gekoppelBestelling.getUuid());
            gebruiker.getDefaultAdministratie().addBestelling(updatedBestelling);
        }
        gebruiker.getDefaultAdministratie().addFactuur(factuur);
    }

    private void removeFactuur(Gebruiker gebruiker, String uuid) {
        Factuur factuur = gebruiker.getDefaultAdministratie().getFactuur(uuid);
        String bestellingNummerOudeFactuur = factuur == null ? null : factuur.getGekoppeldeBestellingNummer();
        Bestelling gekoppelBestelling = gebruiker.getDefaultAdministratie().getBestellingByBestellingNummer(bestellingNummerOudeFactuur);
        if (gekoppelBestelling != null) {
            Bestelling updatedBestelling = gekoppelBestelling.toBuilder().gekoppeldFactuurNummer(null).build();
            gebruiker.getDefaultAdministratie().removeBestelling(gekoppelBestelling.getUuid());
            gebruiker.getDefaultAdministratie().addBestelling(updatedBestelling);
        }
        gebruiker.getDefaultAdministratie().removeFactuur(uuid);
    }

    protected Object getPdf(Request req, Response res) throws Exception {
        Gebruiker gebruiker = SessionHelper.getGebruikerOrThowForbiddenExceptin(req, crudService);
        String factuurUuid = req.params(":uuid");
        if ("undefined".equals(factuurUuid)) {
            res.status(404);
            return new SingleAnswer("not found");
        }

        Factuur factuur = gebruiker.getDefaultAdministratie().getFactuur(factuurUuid);
        res.raw().setContentType("application/pdf");
        res.raw().setHeader("Content-Disposition", "attachment; filename=" + factuur.getFactuurNummer() + ".pdf");
        try (BufferedOutputStream zipOutputStream = new BufferedOutputStream(res.raw().getOutputStream())) {
            GenerateFactuur.buildPdf(gebruiker.getDefaultAdministratie(), factuur, zipOutputStream);
            zipOutputStream.flush();
            zipOutputStream.close();
        }
        return null;
    }

}