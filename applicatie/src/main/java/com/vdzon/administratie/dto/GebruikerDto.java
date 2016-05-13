package com.vdzon.administratie.dto;


import com.vdzon.administratie.model.Factuur;
import com.vdzon.administratie.model.Gebruiker;
import com.vdzon.administratie.model.Klant;

import java.util.List;
import java.util.stream.Collectors;

public class GebruikerDto {

    private String uuid;
    private String name;
    private String username;
    private String password;
    private List<FactuurDto> facturen;
    private List<KlantDto> adresboek;

    public GebruikerDto() {
    }

    public GebruikerDto(Gebruiker gebruiker) {
        this.uuid = gebruiker.getUuid();
        this.name = gebruiker.getName();
        this.username = gebruiker.getUsername();
        this.password = gebruiker.getPassword();
        this.facturen = toFacturenDto(gebruiker.getFacturen());
        this.adresboek = toAdressenDto(gebruiker.getAdresboek());
    }

    private List<KlantDto> toAdressenDto(List<Klant> klanten) {
        return klanten
                .stream()
                .map(klant -> new KlantDto(klant))
                .collect(Collectors.toList());
    }

    private List<FactuurDto> toFacturenDto(List<Factuur> facturen) {
        return facturen
                .stream()
                .map(factuur -> new FactuurDto(factuur))
                .sorted((factuurDto1,factuurDto2)  -> factuurDto2.getFactuurNummer().compareTo(factuurDto1.getFactuurNummer()))
                .collect(Collectors.toList());
    }

    public Gebruiker toGebruiker() {
        return new Gebruiker(uuid, name, username, password, toFacturen(), toAdressen());
    }

    private List<Factuur> toFacturen() {
        return facturen
                .stream()
                .map(factuur -> factuur.toFactuur())
                .collect(Collectors.toList());
    }

    private List<Klant> toAdressen() {
        return adresboek
                .stream()
                .map(klant -> klant.toKlant())
                .collect(Collectors.toList());
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFacturen(List<FactuurDto> facturen) {
        this.facturen = facturen;
    }

    public void setAdresboek(List<KlantDto> adresboek) {
        this.adresboek = adresboek;
    }
}
