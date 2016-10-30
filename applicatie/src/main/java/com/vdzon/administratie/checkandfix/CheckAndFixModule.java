package com.vdzon.administratie.checkandfix;

import com.vdzon.administratie.checkandfix.actions.check.AdministratieCheckRule;
import com.vdzon.administratie.checkandfix.actions.fix.AdministratieFix;
import com.vdzon.administratie.checkandfix.model.CheckAndFixRegel;
import com.vdzon.administratie.checkandfix.model.CheckAndFixRegels;
import com.vdzon.administratie.checkandfix.model.CheckType;
import com.vdzon.administratie.crud.UserCrud;
import com.vdzon.administratie.model.Administratie;
import com.vdzon.administratie.model.BoekingenCache;
import com.vdzon.administratie.model.Gebruiker;
import com.vdzon.administratie.util.SessionHelper;
import com.vdzon.administratie.util.SingleAnswer;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import spark.Request;
import spark.Response;

import javax.inject.Inject;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

import static com.vdzon.administratie.util.ReflectionUtils.callMethod;
import static com.vdzon.administratie.util.ReflectionUtils.getInstance;

public class CheckAndFixModule {

    public static final String FIX_PACKAGE = "com.vdzon.administratie.checkandfix.actions.fix";
    public static final String CHECK_PACKAGE = "com.vdzon.administratie.checkandfix.actions.check";
    private Set<Method> methodsAnnotatedWithCheck;
    private Set<Method> methodsAnnotatedWithFix;

    public CheckAndFixModule() {
        Reflections reflectionsCheck = new Reflections(CHECK_PACKAGE, new MethodAnnotationsScanner());
        Reflections reflectionsFix = new Reflections(FIX_PACKAGE, new MethodAnnotationsScanner());

        methodsAnnotatedWithCheck = reflectionsCheck.getMethodsAnnotatedWith(AdministratieCheckRule.class);
        methodsAnnotatedWithFix = reflectionsFix.getMethodsAnnotatedWith(AdministratieFix.class);
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
