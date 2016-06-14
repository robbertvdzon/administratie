package com.vdzon.administratie.rubriceren.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vdzon.administratie.auth.SessionHelper;
import com.vdzon.administratie.crud.UserCrud;
import com.vdzon.administratie.dto.AfschriftDto;
import com.vdzon.administratie.model.*;
import com.vdzon.administratie.rubriceren.model.RubriceerAction;
import com.vdzon.administratie.rubriceren.model.RubriceerRegel;
import com.vdzon.administratie.rubriceren.model.RubriceerRegels;
import com.vdzon.administratie.util.SingleAnswer;
import spark.Request;
import spark.Response;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public class RubriceerService {

    //TODO: deze code opruimen

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
            return RubriceerRegels.builder().rubriceerRegelList(regels).build();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
    }

    private List<RubriceerRegel> getRubriceerRegels(Gebruiker gebruiker) {
        List<RubriceerRegel> regels = new ArrayList();

        for (Afschrift afschrift : gebruiker.getDefaultAdministratie().getAfschriften()) {
            if (afschrift.getBoekingType() == null || afschrift.getBoekingType() == BoekingType.NONE) {

                if (afschrift.getBedrag() > 0) {
                    RubriceerAction rubriceerAction = RubriceerAction.NONE;
                    String factuurNummer = null;
                    String rekeningNummer = null;
                    for (Factuur factuur : gebruiker.getDefaultAdministratie().getFacturen()) {
                        if ((factuur.getBedragIncBtw() == afschrift.getBedrag()) && (afschrift.getOmschrijving().contains(factuur.getFactuurNummer()))) {
                            rubriceerAction = RubriceerAction.CONNECT_EXISTING_FACTUUR;
                            factuurNummer = factuur.getFactuurNummer();
                        }
                    }
                    RubriceerRegel rubriceerRegel = RubriceerRegel.builder().rubriceerAction(rubriceerAction).rekeningNummer(rekeningNummer).faktuurNummer(factuurNummer).afschrift(new AfschriftDto(afschrift)).build();
                    regels.add(rubriceerRegel);
                }
                if (afschrift.getBedrag() < 0) {
                    RubriceerAction rubriceerAction = RubriceerAction.CREATE_REKENING;
                    String factuurNummer = null;
                    String rekeningNummer = null;
                    for (Rekening rekening : gebruiker.getDefaultAdministratie().getRekeningen()) {
                        if ((rekening.getBedragIncBtw() == afschrift.getBedrag() * -1) &&
                                (
                                        (afschrift.getOmschrijving().contains(rekening.getRekeningNummer()))) ||
                                (afschrift.getOmschrijving().equals(rekening.getOmschrijving()))
                                )

                        {
                            rubriceerAction = RubriceerAction.CONNECT_EXISTING_REKENING;
                            rekeningNummer = rekening.getRekeningNummer();
                        }
                    }
                    RubriceerRegel rubriceerRegel = RubriceerRegel.builder().rubriceerAction(rubriceerAction).rekeningNummer(rekeningNummer).faktuurNummer(factuurNummer).afschrift(new AfschriftDto(afschrift)).build();
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
            rubriceerRegels.getRubriceerRegelList().stream().forEach(regel -> processRegel(regel, gebruiker));
            crudService.updateGebruiker(gebruiker);

        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
        return new SingleAnswer("ok");
    }

    private void processRegel(RubriceerRegel regel, Gebruiker gebruiker) {
        Afschrift afschrift = regel.getAfschrift().toAfschrift();
        switch (regel.getRubriceerAction()) {
            case CONNECT_EXISTING_FACTUUR:
                for (Factuur factuur : gebruiker.getDefaultAdministratie().getFacturen()) {
                    if (regel.getFaktuurNummer().equals(factuur.getFactuurNummer())) {
                        Factuur newFactuur = factuur.toBuilder().betaald(true).gekoppeldAfschrift(afschrift.getNummer()).build();
                        gebruiker.getDefaultAdministratie().removeFactuur(factuur.getUuid());
                        gebruiker.getDefaultAdministratie().addFactuur(newFactuur);
                        gebruiker.getDefaultAdministratie().removeAfschrift(afschrift.getNummer());
                        gebruiker.getDefaultAdministratie().addAfschrift(
                                afschrift
                                        .toBuilder()
                                        .boekingType(BoekingType.FACTUUR)
                                        .factuurNummer(factuur.getFactuurNummer())
                                        .rekeningNummer("")
                                        .build()
                        );
                    }
                }
                break;
            case CONNECT_EXISTING_REKENING:
                for (Rekening rekening : gebruiker.getDefaultAdministratie().getRekeningen()) {
                    if (regel.getRekeningNummer().equals(rekening.getRekeningNummer())) {
                        Rekening newRekening = rekening.toBuilder().gekoppeldAfschrift(afschrift.getNummer()).build();
                        gebruiker.getDefaultAdministratie().removeRekening(rekening.getUuid());
                        gebruiker.getDefaultAdministratie().addRekening(newRekening);
                        gebruiker.getDefaultAdministratie().removeAfschrift(afschrift.getNummer());
                        gebruiker.getDefaultAdministratie().addAfschrift(
                                afschrift
                                        .toBuilder()
                                        .boekingType(BoekingType.REKENING)
                                        .factuurNummer("")
                                        .rekeningNummer(rekening.getRekeningNummer())
                                        .build()
                        );
                    }
                }
                break;
            case CREATE_REKENING:
                Rekening rekening = Rekening
                        .builder()
                        .uuid(UUID.randomUUID().toString())
                        .rekeningNummer("" + findNextRekeningNummer(gebruiker)
                        ).naam(afschrift.getRelatienaam())
                        .omschrijving(afschrift.getOmschrijving())
                        .rekeningDate(afschrift.getBoekdatum())
                        .bedragExBtw(afschrift.getBedrag() * -1)
                        .bedragIncBtw(afschrift.getBedrag() * -1)
                        .btw(0)
                        .gekoppeldAfschrift(regel.getAfschrift().getNummer())
                        .build();


                gebruiker.getDefaultAdministratie().addRekening(rekening);
                gebruiker.getDefaultAdministratie().removeAfschrift(afschrift.getNummer());
                gebruiker.getDefaultAdministratie().addAfschrift(
                        afschrift
                                .toBuilder()
                                .boekingType(BoekingType.REKENING)
                                .factuurNummer("")
                                .rekeningNummer(rekening.getRekeningNummer())
                                .build()
                );

                break;
            case NONE:
                break;
            case PRIVE:
                gebruiker.getDefaultAdministratie().removeAfschrift(afschrift.getNummer());
                gebruiker.getDefaultAdministratie().addAfschrift(
                        afschrift
                                .toBuilder()
                                .boekingType(BoekingType.PRIVE)
                                .factuurNummer("")
                                .rekeningNummer("")
                                .build()
                );
                break;
        }
    }

    private Afschrift findAfschrift(String uuid, Gebruiker gebruiker) {
        return gebruiker.getDefaultAdministratie().getAfschriften().stream().filter(afschrift -> afschrift.getUuid().equals(uuid)).findFirst().orElse(null);
    }

    private int findNextRekeningNummer(Gebruiker gebruiker) {
        return 1 + gebruiker.getDefaultAdministratie().getRekeningen().stream().map(rekening -> Integer.parseInt(rekening.getRekeningNummer())).max(Comparator.naturalOrder()).orElse(1000);
    }


}
