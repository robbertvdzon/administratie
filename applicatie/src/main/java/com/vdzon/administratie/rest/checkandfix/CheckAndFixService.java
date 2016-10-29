package com.vdzon.administratie.rest.checkandfix;

import com.vdzon.administratie.checkandfix.CheckAndFixData;
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

    public static final String FIX_PACKAGE = "com.vdzon.administratie.checkandfix.actions.fix";
    public static final String CHECK_PACKAGE = "com.vdzon.administratie.checkandfix.actions.check";
    private Set<Method> methodsAnnotatedWithCheck;
    private Set<Method> methodsAnnotatedWithFix;
    @Inject
    UserCrud crudService;

    public CheckAndFixService() {
        Reflections reflectionsCheck = new Reflections(CHECK_PACKAGE, new MethodAnnotationsScanner());
        Reflections reflectionsFix = new Reflections(FIX_PACKAGE, new MethodAnnotationsScanner());

        methodsAnnotatedWithCheck = reflectionsCheck.getMethodsAnnotatedWith(AdministratieCheckRule.class);
        methodsAnnotatedWithFix = reflectionsFix.getMethodsAnnotatedWith(AdministratieFix.class);

    }

    public Object getCheckAndFixRegels(Request req, Response res) {
        Gebruiker gebruiker = SessionHelper.getGebruikerOrThowForbiddenExceptin(req, crudService);
        List<CheckAndFixRegel> regels = getCheckAndFixRegels(gebruiker.getDefaultAdministratie());
        return CheckAndFixRegels.newBuilder().checkAndFixRegels(regels).build();
    }

    protected Object fix(Request req, Response res) throws Exception {
        Gebruiker gebruiker = SessionHelper.getGebruikerOrThowForbiddenExceptin(req, crudService);
        fix(gebruiker);
        return new SingleAnswer("ok");
    }

    protected void fix(Gebruiker gebruiker) throws Exception {
        Gebruiker updatedGebruiker = gebruiker.newBuilder(gebruiker).administraties(Arrays.asList(getFixedAdministratie(gebruiker.getDefaultAdministratie()))).build();
        crudService.updateGebruiker(updatedGebruiker);
    }

    public Administratie getFixedAdministratie(Administratie administratie) throws Exception {
        List<CheckAndFixRegel> regelsToFix = getCheckAndFixRegels(administratie).stream().filter(regel -> regel.getCheckType() == CheckType.FIX_NEEDED).collect(Collectors.toList());
        final Wrapper wrapper = new Wrapper();
        wrapper.adm = administratie;

        methodsAnnotatedWithFix.stream().forEach(method -> wrapper.adm = (Administratie) callFix(method, regelsToFix, wrapper.adm ));
        return wrapper.adm;
    }

    public List<CheckAndFixRegel> getCheckAndFixRegels(Administratie administratie) {
        CheckAndFixData checkAndFixData = populateCheckAndFixData(administratie);
        List<CheckAndFixRegel> regels = new ArrayList();
        methodsAnnotatedWithCheck.stream().forEach(method -> callCheckAction(method, checkAndFixData, regels));
        return regels;
    }

    private CheckAndFixData populateCheckAndFixData(Administratie administratie) {
        CheckAndFixData checkAndFixData = new CheckAndFixData();
        checkAndFixData.alleAfschriften = administratie.getAfschriften();
        checkAndFixData.alleRekeningen = administratie.getRekeningen();
        checkAndFixData.alleFacturen = administratie.getFacturen();
        checkAndFixData.alleBoekingen = administratie.getBoekingen();
//        checkAndFixData.afschriftMap = checkAndFixData.alleAfschriften.stream().collect(Collectors.toMap(Afschrift::getNummer, Function.identity()));
//        checkAndFixData.rekeningMap = checkAndFixData.alleRekeningen.stream().collect(Collectors.toMap(Rekening::getRekeningNummer, Function.identity()));
//        checkAndFixData.factuurMap = checkAndFixData.alleFacturen.stream().collect(Collectors.toMap(Factuur::getFactuurNummer, Function.identity()));
        checkAndFixData.boekingenCache = new BoekingenCache(administratie.getBoekingen());

        return checkAndFixData;
    }

    private void callCheckAction(Method method, CheckAndFixData checkAndFixData, List<CheckAndFixRegel> regels) {
        Object o = getInstance(method.getDeclaringClass());
        Object invoke = callMethod(method, o, checkAndFixData);
        Collection<? extends CheckAndFixRegel> checkAndFixRegels = (Collection<? extends CheckAndFixRegel>) invoke;
        if (checkAndFixRegels!=null) {
            regels.addAll(checkAndFixRegels);
        }
    }

    private Administratie callFix(Method method, List<CheckAndFixRegel> regelsToFix, Administratie administratie) {
        Object instance = getInstance(method.getDeclaringClass());
        final Wrapper wrapper = new Wrapper();
        wrapper.adm = administratie;
        regelsToFix.stream().forEach(regel -> wrapper.adm = (Administratie) callMethod(method, instance, regel, wrapper.adm));
        return wrapper.adm;
    }

    class Wrapper{
        public Administratie adm = null;
    }

}
