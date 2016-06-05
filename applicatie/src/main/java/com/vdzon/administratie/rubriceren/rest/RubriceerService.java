package com.vdzon.administratie.rubriceren.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vdzon.administratie.auth.SessionHelper;
import com.vdzon.administratie.crud.UserCrud;
import com.vdzon.administratie.dto.BestellingDto;
import com.vdzon.administratie.model.*;
import com.vdzon.administratie.rubriceren.model.RubriceerAction;
import com.vdzon.administratie.rubriceren.model.RubriceerRegel;
import com.vdzon.administratie.rubriceren.model.RubriceerRegels;
import com.vdzon.administratie.util.SingleAnswer;
import spark.Request;
import spark.Response;

import javax.inject.Inject;
import java.util.*;

public class RubriceerService {

    @Inject
    UserCrud crudService;

    protected Object getRubriceerRegels(Request req, Response res) throws Exception {
        try {
            String uuid = SessionHelper.getAuthenticatedUserUuid(req);
            Gebruiker gebruiker = crudService.getGebruiker(uuid);
            if (gebruiker == null) {
                res.status(404);
                return new SingleAnswer("not found");
            }

            List<RubriceerRegel> regels = getRubriceerRegels(gebruiker);
            return new RubriceerRegels(regels);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
    }

    private List<RubriceerRegel> getRubriceerRegels(Gebruiker gebruiker) {
        List<RubriceerRegel> regels = new ArrayList();

        for (Afschrift afschrift : gebruiker.getDefaultAdministratie().getAfschriften()){
            if (afschrift.getBoekingType()== null || afschrift.getBoekingType()== BoekingType.NONE){

                if (afschrift.getBedrag()>0){
                    RubriceerAction rubriceerAction = RubriceerAction.NONE;
                    String factuurNummer = null;
                    String rekeningNummer = null;
                    for (Factuur factuur : gebruiker.getDefaultAdministratie().getFacturen()){
                        if ((factuur.getBedragIncBtw()==afschrift.getBedrag()) && (afschrift.getOmschrijving().contains(factuur.getFactuurNummer()))){
                            rubriceerAction = RubriceerAction.CONNECT_EXISTING_FACTUUR;
                            factuurNummer = factuur.getFactuurNummer();
                        }
                    }
                    RubriceerRegel rubriceerRegel = new RubriceerRegel(rubriceerAction,afschrift.getUuid(),rekeningNummer, factuurNummer);
                    regels.add(rubriceerRegel);
                }
                if (afschrift.getBedrag()<0){
                    RubriceerAction rubriceerAction = RubriceerAction.CREATE_REKENING;
                    String factuurNummer = null;
                    String rekeningNummer = null;
                    for (Rekening rekening : gebruiker.getDefaultAdministratie().getRekeningen()){
                        if ((rekening.getBedragIncBtw()==afschrift.getBedrag()) && (afschrift.getOmschrijving().contains(rekening.getRekeningNummer()))){
                            rubriceerAction = RubriceerAction.CONNECT_EXISTING_REKENING;
                            factuurNummer = rekening.getRekeningNummer();
                        }
                    }
                    RubriceerRegel rubriceerRegel = new RubriceerRegel(rubriceerAction,afschrift.getUuid(),rekeningNummer, factuurNummer);
                    regels.add(rubriceerRegel);
                }
            }
        }
        return regels;
    }

    protected Object rubriceerRegels(Request req, Response res) throws Exception {
        try {
            String uuid = SessionHelper.getAuthenticatedUserUuid(req);
            Gebruiker gebruiker = crudService.getGebruiker(uuid);
            if (gebruiker == null) {
                res.status(404);
                return new SingleAnswer("not found");
            }
            String regelsJson = req.body();
            ObjectMapper mapper = new ObjectMapper();
            RubriceerRegels rubriceerRegels = mapper.readValue(regelsJson, RubriceerRegels.class);

            System.out.println("rubriceer:");
            rubriceerRegels.getRubriceerRegelList().stream().forEach(regel-> processRegel(regel, gebruiker));
            crudService.updateGebruiker(gebruiker);

        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
        return new SingleAnswer("ok");
    }

    private void processRegel(RubriceerRegel regel, Gebruiker gebruiker){
        switch (regel.getRubriceerAction()){
            case CONNECT_EXISTING_FACTUUR:
                break;
            case CONNECT_EXISTING_REKENING:
                break;
            case CREATE_REKENING:
                Afschrift afschrift = findAfschrift(regel.getAfschiftUuid(), gebruiker);
                Rekening rekening = new Rekening(UUID.randomUUID().toString(), findNextRekeningNummer(gebruiker), afschrift.getRelatienaam(), afschrift.getOmschrijving(), afschrift.getBoekdatum(), afschrift.getBedrag(), afschrift.getBedrag(), 0, regel.getAfschiftUuid());
                gebruiker.getDefaultAdministratie().addRekening(rekening);
                gebruiker.getDefaultAdministratie().removeAfschrift(afschrift.getUuid());
                gebruiker.getDefaultAdministratie().addAfschrift(new Afschrift(afschrift.getUuid(), afschrift.getRekening(), afschrift.getOmschrijving(), afschrift.getRelatienaam(), afschrift.getBoekdatum(), afschrift.getBedrag(), BoekingType.REKENING, "",rekening.getRekeningNummer()));
                break;
            case NONE:
                break;
            case PRIVE:
                break;
        }
    }

    private Afschrift findAfschrift(String uuid, Gebruiker gebruiker){
        return gebruiker.getDefaultAdministratie().getAfschriften().stream().filter(afschrift -> afschrift.getUuid().equals(uuid)).findFirst().orElse(null);
    }

    private String findNextRekeningNummer(Gebruiker gebruiker){
        return ""+gebruiker.getDefaultAdministratie().getRekeningen().stream().map(rekening->Integer.parseInt(rekening.getRekeningNummer())).max(Comparator.naturalOrder()).get();
    }


}
