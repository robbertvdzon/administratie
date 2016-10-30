package com.vdzon.administratie.rest.checkandfix;

import com.vdzon.administratie.checkandfix.CheckAndFixData;
import com.vdzon.administratie.checkandfix.CheckAndFixModule;
import com.vdzon.administratie.util.SessionHelper;
import com.vdzon.administratie.checkandfix.actions.check.AdministratieCheckRule;
import com.vdzon.administratie.checkandfix.actions.fix.AdministratieFix;
import com.vdzon.administratie.checkandfix.model.CheckAndFixRegel;
import com.vdzon.administratie.checkandfix.model.CheckAndFixRegels;
import com.vdzon.administratie.checkandfix.model.CheckType;
import com.vdzon.administratie.crud.UserCrud;
import com.vdzon.administratie.model.*;
import com.vdzon.administratie.util.SingleAnswer;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import spark.Request;
import spark.Response;

import javax.inject.Inject;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.vdzon.administratie.util.ReflectionUtils.callMethod;
import static com.vdzon.administratie.util.ReflectionUtils.getInstance;

public class CheckAndFixService {

    @Inject
    UserCrud crudService;

    @Inject
    CheckAndFixModule checkAndFixModule;

    public CheckAndFixService() {
    }

    public Object getCheckAndFixRegels(Request req, Response res) {
        Gebruiker gebruiker = SessionHelper.getGebruikerOrThowForbiddenExceptin(req, crudService);
        return checkAndFixModule.getCheckAndFixRegels(gebruiker.getDefaultAdministratie());
    }

    protected Object fix(Request req, Response res) throws Exception {
        Gebruiker gebruiker = SessionHelper.getGebruikerOrThowForbiddenExceptin(req, crudService);
        Gebruiker updatedGebruiker = gebruiker.newBuilder(gebruiker).administraties(Arrays.asList(checkAndFixModule.getFixedAdministratie(gebruiker.getDefaultAdministratie()))).build();
        crudService.updateGebruiker(updatedGebruiker);
        return new SingleAnswer("ok");
    }


}
