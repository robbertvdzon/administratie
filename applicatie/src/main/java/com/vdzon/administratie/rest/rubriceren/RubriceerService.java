package com.vdzon.administratie.rest.rubriceren;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vdzon.administratie.crud.UserCrud;
import com.vdzon.administratie.model.Afschrift;
import com.vdzon.administratie.model.BoekingenCache;
import com.vdzon.administratie.model.Gebruiker;
import com.vdzon.administratie.rubriceren.model.RubriceerRegel;
import com.vdzon.administratie.rubriceren.model.RubriceerRegels;
import com.vdzon.administratie.rubriceren.rubriceerRegels.RubriceerRule;
import com.vdzon.administratie.rubriceren.rubriceerRegels.RubriceerRuleCommit;
import com.vdzon.administratie.util.SessionHelper;
import com.vdzon.administratie.util.SingleAnswer;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import spark.Request;
import spark.Response;

import javax.inject.Inject;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.vdzon.administratie.util.ReflectionUtils.callMethod;
import static com.vdzon.administratie.util.ReflectionUtils.getInstance;

public class RubriceerService {
    private static final String RUBRICEER_PACKAGE = "com.vdzon.administratie.rubriceren.rubriceerRegels";
    @Inject
    UserCrud crudService;
    private Set<Method> methodsAnnotatedWithRule;
    private Set<Method> methodsAnnotatedWithCommit;

    public RubriceerService() {
        Reflections reflections = new Reflections(RUBRICEER_PACKAGE, new MethodAnnotationsScanner());
        methodsAnnotatedWithRule = reflections.getMethodsAnnotatedWith(RubriceerRule.class);
        methodsAnnotatedWithCommit = reflections.getMethodsAnnotatedWith(RubriceerRuleCommit.class);

    }

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
        methodsAnnotatedWithCommit.stream().forEach(method -> callRubriceerCommitAction(method, gebruiker, rubriceerRegels));
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
        methodsAnnotatedWithRule.stream().forEach(method -> callRubriceerAction(method, gebruiker, regels, afschrift));
    }

    private void callRubriceerAction(Method method, Gebruiker gebruiker, List<RubriceerRegel> regels, Afschrift afschrift) {
        Object o = getInstance(method.getDeclaringClass());
        BoekingenCache boekingenCache = new BoekingenCache(gebruiker.getDefaultAdministratie().getBoekingen());
        callMethod(method, o, gebruiker, regels, afschrift, boekingenCache);
    }

    private void callRubriceerCommitAction(Method method, Gebruiker gebruiker, RubriceerRegels rubriceerRegels) {
        Object o = getInstance(method.getDeclaringClass());
        rubriceerRegels.getRubriceerRegelList().forEach(regel -> callMethod(method, o, regel, gebruiker));
    }

}
