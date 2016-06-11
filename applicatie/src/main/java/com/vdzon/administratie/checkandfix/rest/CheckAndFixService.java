package com.vdzon.administratie.checkandfix.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vdzon.administratie.auth.SessionHelper;
import com.vdzon.administratie.checkandfix.model.CheckAndFixRegel;
import com.vdzon.administratie.checkandfix.model.CheckAndFixRegels;
import com.vdzon.administratie.checkandfix.model.CheckType;
import com.vdzon.administratie.checkandfix.model.FixAction;
import com.vdzon.administratie.crud.UserCrud;
import com.vdzon.administratie.dto.AfschriftDto;
import com.vdzon.administratie.model.*;
import com.vdzon.administratie.util.SingleAnswer;
import spark.Request;
import spark.Response;

import javax.inject.Inject;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CheckAndFixService {

    @Inject
    UserCrud crudService;

    protected Object getCheckAndFixRegels(Request req, Response res) throws Exception {
        try {
            String uuid = SessionHelper.getAuthenticatedUserUuid(req);
            Gebruiker gebruiker = crudService.getGebruiker(uuid);
            if (gebruiker == null) {
                res.status(404);
                return new SingleAnswer("not found");
            }

            List<CheckAndFixRegel> regels = getCheckAndFixRegels(gebruiker);
            return new CheckAndFixRegels(regels);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
    }

    private List<CheckAndFixRegel> getCheckAndFixRegels(Gebruiker gebruiker) {
        CheckAndFixData checkAndFixData = new CheckAndFixData();

        Administratie administratie = gebruiker.getDefaultAdministratie();
        checkAndFixData.alleAfschriften = administratie.getAfschriften();
        checkAndFixData.alleRekeningen = administratie.getRekeningen();
        checkAndFixData.alleFacturen = administratie.getFacturen();
        checkAndFixData.afschriftMap = checkAndFixData.alleAfschriften.stream().collect(Collectors.toMap(Afschrift::getNummer, Function.identity()));
        checkAndFixData.rekeningMap = checkAndFixData.alleRekeningen.stream().collect(Collectors.toMap(Rekening::getRekeningNummer, Function.identity()));
        checkAndFixData.factuurMap = checkAndFixData.alleFacturen.stream().collect(Collectors.toMap(Factuur::getFactuurNummer, Function.identity()));

        List<CheckAndFixRegel> regels = new ArrayList();
        List<Afschrift> afschriften = gebruiker.getDefaultAdministratie().getAfschriften();

        regels.addAll(checkFacturenZonderAfschift(checkAndFixData));
        regels.addAll(checkRekeningenZonderAfschift(checkAndFixData));
        regels.addAll(checkOnverwerkteAfschiften(checkAndFixData));

        regels.addAll(checkDubbeleFactuurNummers(checkAndFixData));
        regels.addAll(checkDubbeleRekeningNummers(checkAndFixData));
        regels.addAll(checkDubbeleAfschriftNummers(checkAndFixData));

        regels.addAll(checkFacturenOfAfschriftenWelBestaan(checkAndFixData));
        regels.addAll(checkRekeningenOfAfschriftenWelBestaan(checkAndFixData));
        regels.addAll(checkAfschriftenOfFacturenWelBestaan(checkAndFixData));
        regels.addAll(checkAfschriftenOfRekeningenWelBestaan(checkAndFixData));

        regels.addAll(checkConsistencyTussenAfschriftenEnRekeningen(checkAndFixData));
        regels.addAll(checkConsistencyTussenAfschriftenEnFacturen(checkAndFixData));

        regels.addAll(vergelijkBedragTussenRekeningenEnAfschriften(checkAndFixData));
        regels.addAll(vergelijkBedragTussenFacturenEnAfschriften(checkAndFixData));

        return regels;
    }

    private Collection<? extends CheckAndFixRegel> checkFacturenZonderAfschift(CheckAndFixData data) {
        return data.alleFacturen
                .stream()
                .filter(factuur -> !hasGekoppeldAfschrift(factuur))
                .map(factuur -> new CheckAndFixRegel(FixAction.NONE, CheckType.WARNING, null, "Factuur " + factuur.getFactuurNummer() + " is niet geboekt", ""))
                .collect(Collectors.toList());
    }

    private Collection<? extends CheckAndFixRegel> checkRekeningenZonderAfschift(CheckAndFixData data) {
        return data.alleRekeningen
                .stream()
                .filter(rekening -> !hasGekoppeldAfschrift(rekening))
                .map(rekening -> new CheckAndFixRegel(FixAction.NONE, CheckType.WARNING, null, "Rekening " + rekening.getRekeningNummer() + " is niet geboekt", ""))
                .collect(Collectors.toList());
    }

    private Collection<? extends CheckAndFixRegel> checkOnverwerkteAfschiften(CheckAndFixData data) {
        return data.alleAfschriften
                .stream()
                .filter(afschrift -> afschrift.getBoekingType() == BoekingType.NONE)
                .map(afschrift -> new CheckAndFixRegel(FixAction.NONE, CheckType.WARNING, getAfschriftDto(afschrift), "Afschift " + afschrift.getNummer() + " is niet verwerkt", ""))
                .collect(Collectors.toList());
    }

    private Collection<? extends CheckAndFixRegel> vergelijkBedragTussenFacturenEnAfschriften(CheckAndFixData data) {
        return data.alleFacturen
                .stream()
                .filter(factuur -> hasGekoppeldAfschrift(factuur))
                .filter(factuur -> factuur.getBedragIncBtw() != getAfschiftBedrag(factuur, data))
                .map(factuur -> new CheckAndFixRegel(FixAction.NONE, CheckType.WARNING, getAfschriftDto(factuur, data), "Factuurbedrag van factuur " + factuur.getFactuurNummer() + " komt niet overeen met bedrag afschift "+factuur.getGekoppeldAfschrift()+"", " ("+factuur.getBedragIncBtw()+"!="+getAfschiftBedrag(factuur, data)+")"))
                .collect(Collectors.toList());
    }

    private Collection<? extends CheckAndFixRegel> vergelijkBedragTussenRekeningenEnAfschriften(CheckAndFixData data) {
        return data.alleRekeningen
                .stream()
                .filter(rekening -> hasGekoppeldAfschrift(rekening))
                .filter(rekening -> rekening.getBedragIncBtw()*-1 != getAfschiftBedrag(rekening, data))
                .map(rekening -> new CheckAndFixRegel(FixAction.NONE, CheckType.WARNING, getAfschriftDto(rekening, data), "Rekeningbedrag van rekening " + rekening.getRekeningNummer() + " komt niet overeen met bedrag afschift "+rekening.getGekoppeldAfschrift()+"", " ("+rekening.getBedragIncBtw()+"!="+getAfschiftBedrag(rekening, data)+")"))
                .collect(Collectors.toList());
    }

    private Collection<? extends CheckAndFixRegel> checkConsistencyTussenAfschriftenEnRekeningen(CheckAndFixData data) {
        return data.alleRekeningen
                .stream()
                .filter(rekening -> hasGekoppeldAfschrift(rekening))
                .filter(rekening -> !rekening.getRekeningNummer().equals(getRekeningNummer(rekening, data)))
                .map(rekening -> new CheckAndFixRegel(FixAction.NONE, CheckType.WARNING, getAfschriftDto(rekening, data), "Inconsistentie tussen rekening " + rekening.getRekeningNummer() + " en afschrift "+getAfschriftDto(rekening, data).getNummer()+"(heeft nr "+getRekeningNummer(rekening, data)+")", ""))
                .collect(Collectors.toList());
    }

    private Collection<? extends CheckAndFixRegel> checkConsistencyTussenAfschriftenEnFacturen(CheckAndFixData data) {
        return data.alleFacturen
                .stream()
                .filter(factuur -> hasGekoppeldAfschrift(factuur))
                .filter(factuur -> !factuur.getFactuurNummer().equals(getFactuurNummer(factuur, data)))
                .map(factuur -> new CheckAndFixRegel(FixAction.NONE, CheckType.WARNING, getAfschriftDto(factuur, data), "Inconsistentie tussen factuur " + factuur.getFactuurNummer() + " en afschrift "+getAfschriftDto(factuur, data).getNummer()+"(heeft nr "+getFactuurNummer(factuur, data), ""))
                .collect(Collectors.toList());
    }

    private Collection<? extends CheckAndFixRegel> checkAfschriftenOfRekeningenWelBestaan(CheckAndFixData data) {
        return data.alleAfschriften
                .stream()
                .filter(afschrift -> afschrift.getBoekingType() == BoekingType.REKENING)
                .filter(afschrift -> data.rekeningMap.get(afschrift.getRekeningNummer())==null)
                .map(afschrift -> new CheckAndFixRegel(FixAction.REMOVE_REF_FROM_AFSCHRIFT, CheckType.FIX_NEEDED, getAfschriftDto(afschrift), "Afschift " + afschrift.getNummer() + " is gekoppeld aan niet bestaande rekening "+afschrift.getRekeningNummer(),afschrift.getNummer()))
                .collect(Collectors.toList());
    }

    private Collection<? extends CheckAndFixRegel> checkAfschriftenOfFacturenWelBestaan(CheckAndFixData data) {
        return data.alleAfschriften
                .stream()
                .filter(afschrift -> afschrift.getBoekingType() == BoekingType.FACTUUR)
                .filter(afschrift -> data.factuurMap.get(afschrift.getFactuurNummer())==null)
                .map(afschrift -> new CheckAndFixRegel(FixAction.REMOVE_REF_FROM_AFSCHRIFT, CheckType.FIX_NEEDED, getAfschriftDto(afschrift), "Afschift " + afschrift.getNummer() + " is gekoppeld aan niet bestaande factuur "+afschrift.getFactuurNummer(),afschrift.getNummer()))
                .collect(Collectors.toList());
    }

    private Collection<? extends CheckAndFixRegel> checkRekeningenOfAfschriftenWelBestaan(CheckAndFixData data) {
        return data.alleRekeningen
                .stream()
                .filter(rekening -> hasGekoppeldAfschrift(rekening))
                .filter(rekening -> data.afschriftMap.get(rekening.getGekoppeldAfschrift())==null)
                .map(rekening -> new CheckAndFixRegel(FixAction.REMOVE_REF_FROM_REKENING, CheckType.FIX_NEEDED, getAfschriftDto(rekening, data), "Rekening " + rekening.getRekeningNummer() + " is geboekt aan niet bestaande afschrift "+rekening.getGekoppeldAfschrift(),rekening.getRekeningNummer()))
                .collect(Collectors.toList());
    }

    private Collection<? extends CheckAndFixRegel> checkFacturenOfAfschriftenWelBestaan(CheckAndFixData data) {
        return data.alleFacturen
                .stream()
                .filter(factuur -> hasGekoppeldAfschrift(factuur))
                .filter(factuur -> data.afschriftMap.get(factuur.getGekoppeldAfschrift())==null)
                .map(factuur -> new CheckAndFixRegel(FixAction.REMOVE_REF_FROM_FACTUUR, CheckType.FIX_NEEDED, getAfschriftDto(factuur, data), "Factuur " + factuur.getFactuurNummer() + " is geboekt aan niet bestaande afschrift "+factuur.getGekoppeldAfschrift(),factuur.getFactuurNummer()))
                .collect(Collectors.toList());
    }

    private Collection<? extends CheckAndFixRegel> checkDubbeleAfschriftNummers(CheckAndFixData data) {
        return data.alleAfschriften
                .stream()
                .filter(afschrift -> data.afschriftMap.get(afschrift.getNummer())!=afschrift)
                .map(afschrift -> new CheckAndFixRegel(FixAction.NONE, CheckType.WARNING, getAfschriftDto(afschrift), "Afschift " + afschrift.getNummer() + " bestaat meerdere keren",""))
                .collect(Collectors.toList());
    }

    private Collection<? extends CheckAndFixRegel> checkDubbeleRekeningNummers(CheckAndFixData data) {
        return data.alleRekeningen
                .stream()
                .filter(rekening -> data.rekeningMap.get(rekening.getRekeningNummer())!=rekening)
                .map(rekening -> new CheckAndFixRegel(FixAction.NONE, CheckType.WARNING, getAfschriftDto(rekening, data), "Rekening " + rekening.getRekeningNummer() + " bestaat meerdere keren",""))
                .collect(Collectors.toList());
    }

    private List<CheckAndFixRegel> checkDubbeleFactuurNummers(CheckAndFixData data) {
        return data.alleFacturen
                .stream()
                .filter(factuur -> data.factuurMap.get(factuur.getFactuurNummer())!=factuur)
                .map(factuur -> new CheckAndFixRegel(FixAction.NONE, CheckType.WARNING, getAfschriftDto(factuur, data), "Factuur " + factuur.getFactuurNummer() + " bestaat meerdere keren",""))
                .collect(Collectors.toList());
    }

    //--------------


    private AfschriftDto getAfschriftDto(Factuur factuur, CheckAndFixData data) {
        Afschrift afschrift = data.afschriftMap.get(factuur.getGekoppeldAfschrift());
        return afschrift==null?null:new AfschriftDto(afschrift);
    }

    private AfschriftDto getAfschriftDto(Rekening rekening, CheckAndFixData data) {
        Afschrift afschrift = data.afschriftMap.get(rekening.getGekoppeldAfschrift());
        return afschrift==null?null:new AfschriftDto(afschrift);
    }

    private AfschriftDto getAfschriftDto(Afschrift afschrift) {
        return afschrift==null?null:new AfschriftDto(afschrift);
    }

    private double getAfschiftBedrag(Factuur factuur, CheckAndFixData data) {
        Afschrift afschrift = data.afschriftMap.get(factuur.getGekoppeldAfschrift());
        return afschrift==null?0:afschrift.getBedrag();
    }

    private double getAfschiftBedrag(Rekening rekening, CheckAndFixData data) {
        Afschrift afschrift = data.afschriftMap.get(rekening.getGekoppeldAfschrift());
        return afschrift==null?0:afschrift.getBedrag();
    }

    private String getRekeningNummer(Rekening rekening, CheckAndFixData data) {
        Afschrift afschrift = data.afschriftMap.get(rekening.getGekoppeldAfschrift());
        return afschrift==null?"":afschrift.getRekeningNummer();
    }

    private String getFactuurNummer(Factuur factuur, CheckAndFixData data) {
        Afschrift afschrift = data.afschriftMap.get(factuur.getGekoppeldAfschrift());
        return afschrift==null?"":factuur.getFactuurNummer();
    }

    private boolean hasGekoppeldAfschrift(Factuur factuur) {
        return factuur.getGekoppeldAfschrift() != null && factuur.getGekoppeldAfschrift().length() != 0;
    }

    private boolean hasGekoppeldAfschrift(Rekening rekening) {
        return rekening.getGekoppeldAfschrift() != null && rekening.getGekoppeldAfschrift().length() != 0;
    }

    protected Object fix(Request req, Response res) throws Exception {
        try {
            String uuid = SessionHelper.getAuthenticatedUserUuid(req);
            Gebruiker gebruiker = crudService.getGebruiker(uuid);
            if (gebruiker == null) {
                res.status(404);
                return new SingleAnswer("not found");
            }

            List<CheckAndFixRegel> regelsToFix = getCheckAndFixRegels(gebruiker).stream().filter(regel->regel.getCheckType()==CheckType.FIX_NEEDED).collect(Collectors.toList());
            List<CheckAndFixRegel> afschriftenToFix = regelsToFix.stream().filter(regel -> regel.getRubriceerAction() == FixAction.REMOVE_REF_FROM_AFSCHRIFT).collect(Collectors.toList());
            List<CheckAndFixRegel> rekeningenToFix = regelsToFix.stream().filter(regel -> regel.getRubriceerAction() == FixAction.REMOVE_REF_FROM_REKENING).collect(Collectors.toList());
            List<CheckAndFixRegel> facturenToFix = regelsToFix.stream().filter(regel -> regel.getRubriceerAction() == FixAction.REMOVE_REF_FROM_FACTUUR).collect(Collectors.toList());


            afschriftenToFix.forEach(regel->fixAfschrift(regel, gebruiker));
            rekeningenToFix.forEach(regel->fixRekening(regel, gebruiker));
            facturenToFix.forEach(regel->fixFactuur(regel, gebruiker));

            crudService.updateGebruiker(gebruiker);

        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
        return new SingleAnswer("ok");
    }
    private void fixRekening(CheckAndFixRegel regel, Gebruiker gebruiker) {
        Rekening rekening = gebruiker.getDefaultAdministratie().getRekeningen().stream().filter(rek -> rek.getRekeningNummer().equals(regel.getData())).findFirst().orElse(null);
        if (rekening==null) return;
        gebruiker.getDefaultAdministratie().removeRekening((rekening.getUuid()));
        gebruiker.getDefaultAdministratie().addRekening(new Rekening(
                rekening.getUuid(),
                rekening.getRekeningNummer(),
                rekening.getFactuurNummer(),
                rekening.getNaam(),
                rekening.getOmschrijving(),
                rekening.getRekeningDate(),
                rekening.getBedragExBtw(),
                rekening.getBedragIncBtw(),
                rekening.getBtw(),
                ""
        ));
    }

    private void fixFactuur(CheckAndFixRegel regel, Gebruiker gebruiker) {
        Factuur factuur  = gebruiker.getDefaultAdministratie().getFacturen().stream().filter(fak -> fak.getFactuurNummer().equals(regel.getData())).findFirst().orElse(null);
        if (factuur ==null) return;
        gebruiker.getDefaultAdministratie().removeFactuur(factuur .getUuid());
        gebruiker.getDefaultAdministratie().addFactuur(new Factuur(
                factuur.getFactuurNummer(),
                factuur.getGekoppeldeBestellingNummer(),
                factuur.getFactuurDate(),
                factuur.getContact(),
                factuur.isBetaald(),
                factuur.getFactuurRegels(),
                factuur.getUuid(),
                ""
        ));
    }

    private void fixAfschrift(CheckAndFixRegel regel, Gebruiker gebruiker) {
        Afschrift afschrift = regel.getAfschrift().toAfschrift();
        gebruiker.getDefaultAdministratie().removeAfschrift(afschrift.getNummer());
        gebruiker.getDefaultAdministratie().addAfschrift(new Afschrift(afschrift.getUuid(),
                afschrift.getNummer(),
                afschrift.getRekening(),
                afschrift.getOmschrijving(),
                afschrift.getRelatienaam(),
                afschrift.getBoekdatum(),
                afschrift.getBedrag(),
                BoekingType.NONE,
                "",
                ""
                ));
    }

    private Afschrift findAfschrift(String uuid, Gebruiker gebruiker) {
        return gebruiker.getDefaultAdministratie().getAfschriften().stream().filter(afschrift -> afschrift.getUuid().equals(uuid)).findFirst().orElse(null);
    }

    private int findNextRekeningNummer(Gebruiker gebruiker) {
        return 1 + gebruiker.getDefaultAdministratie().getRekeningen().stream().map(rekening -> Integer.parseInt(rekening.getRekeningNummer())).max(Comparator.naturalOrder()).orElse(1000);
    }

    private class CheckAndFixData {
        public List<Afschrift> alleAfschriften = null;
        public List<Rekening> alleRekeningen = null;
        public List<Factuur> alleFacturen = null;
        public Map<String, Afschrift> afschriftMap = null;
        public Map<String, Rekening> rekeningMap = null;
        public Map<String, Factuur> factuurMap = null;

    }


}
