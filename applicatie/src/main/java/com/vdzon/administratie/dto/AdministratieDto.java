package com.vdzon.administratie.dto;


import com.vdzon.administratie.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AdministratieDto {

    private String uuid;
    private AdministratieGegevensDto administratieGegevens;
    private List<FactuurDto> facturen;
    private List<ContactDto> adresboek;
    private List<RekeningDto> rekeningen;
    private List<AfschriftDto> afschriften;
    private List<DeclaratieDto> declaraties;


    public AdministratieDto() {
    }

    public AdministratieDto(Administratie administratie) {
        this.uuid = administratie.getUuid();
        this.administratieGegevens = new AdministratieGegevensDto(administratie.getAdministratieGegevens());
        this.facturen = toFacturenDto(administratie.getFacturen());
        this.adresboek = toAdressenDto(administratie.getAdresboek());
        this.rekeningen = toRekeningenDto(administratie.getRekeningen());
        this.afschriften = toAfschriftenDto(administratie.getAfschriften());
        this.declaraties = toDeclaratiesDto(administratie.getDeclaraties());

    }

    private List<RekeningDto> toRekeningenDto(List<Rekening> rekeningen) {
        return rekeningen
                .stream()
                .map(rekening -> new RekeningDto(rekening))
                .sorted((rekeningDto1, rekeningDto2) -> rekeningDto2.getRekeningNummer().compareTo(rekeningDto1.getRekeningNummer()))
                .collect(Collectors.toList());
    }

    private List<AfschriftDto> toAfschriftenDto(List<Afschrift> afschriften) {
        return afschriften
                .stream()
                .map(afschrift -> new AfschriftDto(afschrift))
                .sorted((afschriftDto1, afschriftDto2) -> afschriftDto2.getBoekdatum().compareTo(afschriftDto1.getBoekdatum()))
                .collect(Collectors.toList());
    }

    private List<DeclaratieDto> toDeclaratiesDto(List<Declaratie> declaraties) {
        return declaraties
                .stream()
                .map(declaratie -> new DeclaratieDto(declaratie))
                .sorted((declaratieDto1, declaratieDto2) -> declaratieDto2.getDeclaratieNummer().compareTo(declaratieDto1.getDeclaratieNummer()))
                .collect(Collectors.toList());
    }



    private List<ContactDto> toAdressenDto(List<Contact> klanten) {
        return klanten
                .stream()
                .map(klant -> new ContactDto(klant))
                .sorted((ContactDto1, ContactDto2) -> ContactDto2.getKlantNummer().compareTo(ContactDto1.getKlantNummer()))
                .collect(Collectors.toList());
    }

    private List<FactuurDto> toFacturenDto(List<Factuur> facturen) {
        return facturen
                .stream()
                .map(factuur -> new FactuurDto(factuur))
                .sorted((factuurDto1, factuurDto2) -> factuurDto2.getFactuurNummer().compareTo(factuurDto1.getFactuurNummer()))
                .collect(Collectors.toList());
    }

    public Administratie toAdministratie() {
        return new Administratie(uuid, toFacturen(), toAdressen(), toRekeningen(), toAfschriften(), toDeclaraties(),  administratieGegevens.toAdministratieGegevens());
    }

    private List<Factuur> toFacturen() {
        return facturen
                .stream()
                .map(factuur -> factuur.toFactuur())
                .collect(Collectors.toList());
    }

    private List<Contact> toAdressen() {
        return adresboek
                .stream()
                .map(klant -> klant.toContact())
                .collect(Collectors.toList());
    }

    private List<Rekening> toRekeningen() {
        return rekeningen
                .stream()
                .map(rekening -> rekening.toRekening())
                .collect(Collectors.toList());
    }

    private List<Declaratie> toDeclaraties() {
        return declaraties
                .stream()
                .map(declaratie -> declaratie.toDeclaratie())
                .collect(Collectors.toList());
    }

    private List<Afschrift> toAfschriften() {
        return afschriften
                .stream()
                .map(afschrift -> afschrift.toAfschrift())
                .collect(Collectors.toList());
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setFacturen(List<FactuurDto> facturen) {
        this.facturen = facturen;
    }

    public void setAdresboek(List<ContactDto> adresboek) {
        this.adresboek = adresboek;
    }

}
