package com.vdzon.administratie.rubriceren.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vdzon.administratie.auth.SessionHelper;
import com.vdzon.administratie.crud.UserCrud;
import com.vdzon.administratie.dto.BestellingDto;
import com.vdzon.administratie.model.Bestelling;
import com.vdzon.administratie.model.Factuur;
import com.vdzon.administratie.model.Gebruiker;
import com.vdzon.administratie.rubriceren.model.RubriceerAction;
import com.vdzon.administratie.rubriceren.model.RubriceerRegel;
import com.vdzon.administratie.rubriceren.model.RubriceerRegels;
import com.vdzon.administratie.util.SingleAnswer;
import spark.Request;
import spark.Response;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

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

            List<RubriceerRegel> regels = new ArrayList();
            regels.add(new RubriceerRegel(RubriceerAction.NONE, "1234","",""));
            regels.add(new RubriceerRegel(RubriceerAction.NONE, "1235","",""));
            regels.add(new RubriceerRegel(RubriceerAction.CREATE_REKENING, "1236","",""));
            regels.add(new RubriceerRegel(RubriceerAction.CONNECT_EXISTING_FACTUUR, "1237","1001",""));
            regels.add(new RubriceerRegel(RubriceerAction.CONNECT_EXISTING_REKENING, "1238","1002",""));
            return new RubriceerRegels(regels);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
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
            rubriceerRegels.getRubriceerRegelList().stream().forEach(regel-> System.out.println(regel));

        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
        return new SingleAnswer("ok");
    }


}
