package com.vdzon.administratie.rest.rekening;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vdzon.administratie.dto.BoekingDto;
import com.vdzon.administratie.model.BoekingenCache;
import com.vdzon.administratie.model.boekingen.Boeking;
import com.vdzon.administratie.model.boekingen.relaties.BoekingMetRekening;
import com.vdzon.administratie.util.SessionHelper;
import com.vdzon.administratie.crud.UserCrud;
import com.vdzon.administratie.dto.RekeningDto;
import com.vdzon.administratie.model.Gebruiker;
import com.vdzon.administratie.model.Rekening;
import com.vdzon.administratie.util.SingleAnswer;
import spark.Request;
import spark.Response;

import javax.inject.Inject;
import java.util.List;

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
        List<BoekingMetRekening> boekingenVanRekening = new BoekingenCache(gebruiker.getDefaultAdministratie().getBoekingen()).getBoekingenVanRekening(rekening.getRekeningNummer());
        for (BoekingMetRekening boeking : boekingenVanRekening ){
            boolean found = false;
            for (BoekingDto boekingDto : rekeningDto.getBoekingen()){
                if (boekingDto.getUuid().equals(boeking.getUuid())){
                    found = true;
                }
            }
            if (!found){
                gebruiker.getDefaultAdministratie().removeBoeking(boeking.getUuid());
            }
        }

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
