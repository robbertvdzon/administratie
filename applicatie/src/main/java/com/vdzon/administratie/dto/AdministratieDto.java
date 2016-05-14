package com.vdzon.administratie.dto;


import com.vdzon.administratie.model.Administratie;
import com.vdzon.administratie.model.Factuur;
import com.vdzon.administratie.model.Contact;

import java.util.List;
import java.util.stream.Collectors;

public class AdministratieDto {

    private String uuid;
    private String name;
    private List<FactuurDto> facturen;
    private List<ContactDto> adresboek;

    public AdministratieDto() {
    }

    public AdministratieDto(Administratie administratie) {
        this.uuid = administratie.getUuid();
        this.name = administratie.getName();
        this.facturen = toFacturenDto(administratie.getFacturen());
    }

    private List<ContactDto> toAdressenDto(List<Contact> klanten) {
        return klanten
                .stream()
                .map(klant -> new ContactDto(klant))
                .collect(Collectors.toList());
    }

    private List<FactuurDto> toFacturenDto(List<Factuur> facturen) {
        return facturen
                .stream()
                .map(factuur -> new FactuurDto(factuur))
                .sorted((factuurDto1,factuurDto2)  -> factuurDto2.getFactuurNummer().compareTo(factuurDto1.getFactuurNummer()))
                .collect(Collectors.toList());
    }

    public Administratie toAdministratie() {
        return new Administratie(uuid, name, toFacturen(), toAdressen());
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

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFacturen(List<FactuurDto> facturen) {
        this.facturen = facturen;
    }

    public void setAdresboek(List<ContactDto> adresboek) {
        this.adresboek = adresboek;
    }
}
