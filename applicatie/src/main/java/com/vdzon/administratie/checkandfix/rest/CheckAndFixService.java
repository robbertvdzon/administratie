package com.vdzon.administratie.checkandfix.rest;

import com.vdzon.administratie.auth.SessionHelper;
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.vdzon.administratie.util.ReflectionUtils.callMethod;
import static com.vdzon.administratie.util.ReflectionUtils.getInstance;

public class CheckAndFixService {

    public static final String FIX_PACKAGE = "com.vdzon.administratie.checkandfix.actions.fix";
    public static final String CHECK_PACKAGE = "com.vdzon.administratie.checkandfix.actions.check";
    @Inject
    UserCrud crudService;

    public Object getCheckAndFixRegels(Request req, Response res) {
        Gebruiker gebruiker = SessionHelper.getGebruikerOrThowForbiddenExceptin(req, crudService);
        List<CheckAndFixRegel> regels = getCheckAndFixRegels(gebruiker.getDefaultAdministratie());
        return CheckAndFixRegels.builder().checkAndFixRegels(regels).build();
    }

    protected Object fix(Request req, Response res) throws Exception {
        Gebruiker gebruiker = SessionHelper.getGebruikerOrThowForbiddenExceptin(req, crudService);
        List<CheckAndFixRegel> regelsToFix = getCheckAndFixRegels(gebruiker.getDefaultAdministratie()).stream().filter(regel -> regel.getCheckType() == CheckType.FIX_NEEDED).collect(Collectors.toList());
        fixAllRegels(gebruiker, regelsToFix);
        return new SingleAnswer("ok");
    }

    private void fixAllRegels(Gebruiker gebruiker, List<CheckAndFixRegel> regelsToFix) {
        Reflections reflections = new Reflections(FIX_PACKAGE, new MethodAnnotationsScanner());
        Set<Method> methodsAnnotatedWith = reflections.getMethodsAnnotatedWith(AdministratieFix.class);
        methodsAnnotatedWith.stream().forEach(method -> callFix(method, regelsToFix, gebruiker));
        crudService.updateGebruiker(gebruiker);
    }

    public List<CheckAndFixRegel> getCheckAndFixRegels(Administratie administratie) {
        CheckAndFixData checkAndFixData = populateCheckAndFixData(administratie);
        Reflections reflections = new Reflections(CHECK_PACKAGE, new MethodAnnotationsScanner());
        Set<Method> methodsAnnotatedWith = reflections.getMethodsAnnotatedWith(AdministratieCheckRule.class);
        List<CheckAndFixRegel> regels = new ArrayList();
        methodsAnnotatedWith.stream().forEach(method -> callCheckAction(method, checkAndFixData, regels));
        return regels;
    }

    private CheckAndFixData populateCheckAndFixData(Administratie administratie) {
        CheckAndFixData checkAndFixData = new CheckAndFixData();
        checkAndFixData.alleAfschriften = administratie.getAfschriften();
        checkAndFixData.alleRekeningen = administratie.getRekeningen();
        checkAndFixData.alleFacturen = administratie.getFacturen();
        checkAndFixData.afschriftMap = checkAndFixData.alleAfschriften.stream().collect(Collectors.toMap(Afschrift::getNummer, Function.identity()));
        checkAndFixData.rekeningMap = checkAndFixData.alleRekeningen.stream().collect(Collectors.toMap(Rekening::getRekeningNummer, Function.identity()));
        checkAndFixData.factuurMap = checkAndFixData.alleFacturen.stream().collect(Collectors.toMap(Factuur::getFactuurNummer, Function.identity()));
        return checkAndFixData;
    }

    private void callCheckAction(Method method, CheckAndFixData checkAndFixData, List<CheckAndFixRegel> regels) {
        Object o = getInstance(method.getDeclaringClass());
        Object invoke = callMethod(method, o, checkAndFixData);
        regels.addAll((Collection<? extends CheckAndFixRegel>) invoke);
    }

    private void callFix(Method method, List<CheckAndFixRegel> regelsToFix, Gebruiker gebruiker) {
        Object instance = getInstance(method.getDeclaringClass());
        regelsToFix.stream().forEach(regel -> callMethod(method, instance, regel, gebruiker));
    }

}
