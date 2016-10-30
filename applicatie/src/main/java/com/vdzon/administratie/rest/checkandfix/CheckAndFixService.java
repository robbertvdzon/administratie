package com.vdzon.administratie.rest.checkandfix;

import com.vdzon.administratie.checkandfix.CheckService;
import com.vdzon.administratie.checkandfix.FixService;
import com.vdzon.administratie.util.SessionHelper;
import com.vdzon.administratie.crud.UserCrud;
import com.vdzon.administratie.model.*;
import com.vdzon.administratie.util.SingleAnswer;
import spark.Request;
import spark.Response;

import javax.inject.Inject;
import java.util.*;

public class CheckAndFixService {

    @Inject
    UserCrud crudService;

    @Inject
    CheckService checkService;

    @Inject
    FixService fixService;

    public CheckAndFixService() {
    }

    public Object getCheckAndFixRegels(Request req, Response res) {
        Gebruiker gebruiker = SessionHelper.getGebruikerOrThowForbiddenExceptin(req, crudService);
        return checkService.getCheckAndFixRegels(gebruiker.getDefaultAdministratie());
    }

    protected Object fix(Request req, Response res) throws Exception {
        Gebruiker gebruiker = SessionHelper.getGebruikerOrThowForbiddenExceptin(req, crudService);
        Gebruiker updatedGebruiker = gebruiker.newBuilder(gebruiker).administraties(Arrays.asList(fixService.getFixedAdministratie(gebruiker.getDefaultAdministratie()))).build();
        crudService.updateGebruiker(updatedGebruiker);
        return new SingleAnswer("ok");
    }


}
