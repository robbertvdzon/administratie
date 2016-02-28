package com.vdzon.weegschaal.gewicht;

import com.vdzon.weegschaal.util.SingleAnswer;
import com.vdzon.weegschaal.crud.GewichtenCrud;
import com.vdzon.weegschaal.model.Gewicht;
import spark.Request;
import spark.Response;

import javax.inject.Inject;
import java.util.Random;

public class GewichtService {

    @Inject
    GewichtenCrud crudService;

    protected Object putGewicht(Request req, Response res) throws Exception{
        String gewicht = req.queryParams("gewicht");
        String uuid = ""+new Random().nextInt();
        crudService.addGewicht(new Gewicht(gewicht, uuid));
        return new SingleAnswer(uuid);
    }
    protected Object getGewicht(Request req, Response res) throws Exception{
        String uuid = req.queryParams("uuid");
        Gewicht gewicht = crudService.getGewicht(uuid);
        return gewicht;
    }
    protected Object deleteGewicht(Request req, Response res) throws Exception{
        String uuid = req.params(":uuid");
        crudService.deleteGewicht(uuid);
        return new SingleAnswer("ok");
    }
    protected Object postGewicht(Request req, Response res) throws Exception{
        String uuid = req.queryParams("uuid");
        String newgewicht = req.queryParams("gewicht");
        Gewicht gewicht = crudService.getGewicht(uuid);
        if (gewicht == null) {
            res.status(404);
            return "";
        }
        gewicht.setGewicht(newgewicht);
        crudService.updateGewicht(gewicht);
        return new SingleAnswer("ok");
    }
}
