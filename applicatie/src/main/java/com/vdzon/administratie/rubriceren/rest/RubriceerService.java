package com.vdzon.administratie.rubriceren.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vdzon.administratie.auth.SessionHelper;
import com.vdzon.administratie.checkandfix.actions.check.AdministratieCheckRule;
import com.vdzon.administratie.checkandfix.model.CheckAndFixRegel;
import com.vdzon.administratie.checkandfix.rest.CheckAndFixData;
import com.vdzon.administratie.crud.UserCrud;
import com.vdzon.administratie.dto.AfschriftDto;
import com.vdzon.administratie.model.*;
import com.vdzon.administratie.rubriceren.model.RubriceerAction;
import com.vdzon.administratie.rubriceren.model.RubriceerRegel;
import com.vdzon.administratie.rubriceren.model.RubriceerRegels;
import com.vdzon.administratie.rubriceren.rubriceerRegels.RubriceerRule;
import com.vdzon.administratie.rubriceren.rubriceerRegels.RubriceerRuleCommit;
import com.vdzon.administratie.util.SingleAnswer;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import spark.Request;
import spark.Response;

import javax.inject.Inject;
import java.lang.reflect.Method;
import java.util.*;

import static com.vdzon.administratie.util.ReflectionUtils.callMethod;
import static com.vdzon.administratie.util.ReflectionUtils.getInstance;

public class RubriceerService {
    private static final String RUBRICEER_PACKAGE = "com.vdzon.administratie.rubriceren.rubriceerRegels";

    @Inject
    UserCrud crudService;

    protected Object getRubriceerRegels(Request req, Response res) throws Exception {
        Gebruiker gebruiker = SessionHelper.getGebruikerOrThowForbiddenExceptin(req, crudService);
        List<RubriceerRegel> regels = getRubriceerRegels(gebruiker);
        return RubriceerRegels.builder().rubriceerRegelList(regels).build();
    }

    protected Object rubriceerRegels(Request req, Response res) throws Exception {
        Gebruiker gebruiker = SessionHelper.getGebruikerOrThowForbiddenExceptin(req, crudService);
        RubriceerRegels rubriceerRegels = getRubriceerRegels(req);
        processRubriceerRegels(gebruiker, rubriceerRegels);
        crudService.updateGebruiker(gebruiker);
        return new SingleAnswer("ok");
    }

    private void processRubriceerRegels(Gebruiker gebruiker, RubriceerRegels rubriceerRegels) {
        Reflections reflections = new Reflections(RUBRICEER_PACKAGE, new MethodAnnotationsScanner());
        Set<Method> methodsAnnotatedWith = reflections.getMethodsAnnotatedWith(RubriceerRuleCommit.class);
        methodsAnnotatedWith.stream().forEach(method -> callRubriceerCommitAction(method, gebruiker, rubriceerRegels));
    }

    private RubriceerRegels getRubriceerRegels(Request req) throws java.io.IOException {
        String regelsJson = req.body();
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(regelsJson, RubriceerRegels.class);
    }

    private List<RubriceerRegel> getRubriceerRegels(Gebruiker gebruiker) {
        List<RubriceerRegel> regels = new ArrayList();
        for (Afschrift afschrift : gebruiker.getDefaultAdministratie().getAfschriften()) {
            updateRegels(gebruiker, regels, afschrift);
        }
        return regels;
    }

    private void updateRegels(Gebruiker gebruiker, List<RubriceerRegel> regels, Afschrift afschrift) {
            Reflections reflections = new Reflections(RUBRICEER_PACKAGE, new MethodAnnotationsScanner());
            Set<Method> methodsAnnotatedWith = reflections.getMethodsAnnotatedWith(RubriceerRule.class);
            methodsAnnotatedWith.stream().forEach(method -> callRubriceerAction(method, gebruiker, regels, afschrift));
    }

    private void callRubriceerAction(Method method, Gebruiker gebruiker, List<RubriceerRegel> regels, Afschrift afschrift) {
        Object o = getInstance(method.getDeclaringClass());
        callMethod(method, o, gebruiker, regels, afschrift);
    }

    private void callRubriceerCommitAction(Method method, Gebruiker gebruiker, RubriceerRegels rubriceerRegels) {
        Object o = getInstance(method.getDeclaringClass());
        rubriceerRegels.getRubriceerRegelList().forEach(regel -> callMethod(method, o, regel, gebruiker));
    }

}
