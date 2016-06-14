package com.vdzon.administratie.bestelling;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vdzon.administratie.auth.SessionHelper;
import com.vdzon.administratie.crud.UserCrud;
import com.vdzon.administratie.dto.BestellingDto;
import com.vdzon.administratie.model.Bestelling;
import com.vdzon.administratie.model.Factuur;
import com.vdzon.administratie.model.Gebruiker;
import com.vdzon.administratie.util.SingleAnswer;
import spark.Request;
import spark.Response;

import javax.inject.Inject;

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
            ObjectMapper mapper = new ObjectMapper();
            BestellingDto bestellingDto = mapper.readValue(bestellingJson, BestellingDto.class);
            Bestelling bestelling = bestellingDto.toBestelling();

            removeBestelling(gebruiker, bestelling.getUuid());
            addBestelling(gebruiker, bestelling);

            crudService.updateGebruiker(gebruiker);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
        return new SingleAnswer("ok");
    }

    private void addBestelling(Gebruiker gebruiker, Bestelling bestelling) {
        String niewFactuurNummer = bestelling.getGekoppeldFactuurNummer();
        Factuur gekoppelFactuur = gebruiker.getDefaultAdministratie().getFactuurByFactuurNummer(niewFactuurNummer);
        if (gekoppelFactuur!=null){
            Factuur updatedFactuur = gekoppelFactuur.toBuilder().gekoppeldeBestellingNummer(bestelling.getBestellingNummer()).build();
            gebruiker.getDefaultAdministratie().removeFactuur(gekoppelFactuur.getUuid());
            gebruiker.getDefaultAdministratie().addFactuur(updatedFactuur);
        }
        gebruiker.getDefaultAdministratie().addBestelling(bestelling);
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
            removeBestelling(gebruiker, bestellingUuid);
            crudService.updateGebruiker(gebruiker);
            return new SingleAnswer("ok");
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }

    }

    private void removeBestelling(Gebruiker gebruiker, String uuid) {
        Bestelling bestelling = gebruiker.getDefaultAdministratie().getBestelling(uuid);
        String factuurNummerOudeBestelling = bestelling==null ? null : bestelling.getGekoppeldFactuurNummer();
        Factuur gekoppelFactuur = gebruiker.getDefaultAdministratie().getFactuurByFactuurNummer(factuurNummerOudeBestelling);
        if (gekoppelFactuur!=null){
            Factuur updatedFactuur = gekoppelFactuur.toBuilder().gekoppeldeBestellingNummer(null).build();
            gebruiker.getDefaultAdministratie().removeFactuur(gekoppelFactuur.getUuid());
            gebruiker.getDefaultAdministratie().addFactuur(updatedFactuur);
        }
        gebruiker.getDefaultAdministratie().removeBestelling(uuid);
    }

}
