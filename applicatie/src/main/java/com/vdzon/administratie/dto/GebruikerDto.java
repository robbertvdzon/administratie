package com.vdzon.administratie.dto;


import com.vdzon.administratie.model.Administratie;
import com.vdzon.administratie.model.Gebruiker;

import java.util.List;
import java.util.stream.Collectors;

public class GebruikerDto {

    private String uuid;
    private String name;
    private String username;
    private String password;
    private List<AdministratieDto> administraties;

    public GebruikerDto() {
    }

    public GebruikerDto(Gebruiker gebruiker) {
        this.uuid = gebruiker.getUuid();
        this.name = gebruiker.getName();
        this.username = gebruiker.getUsername();
        this.password = gebruiker.getPassword();
        this.administraties = toAdministratiesDto(gebruiker.getAdministraties());
    }

    private List<AdministratieDto> toAdministratiesDto(List<Administratie> administraties) {
        return administraties
                .stream()
                .map(administratie -> new AdministratieDto(administratie))
                .collect(Collectors.toList());
    }

    public Gebruiker toGebruiker() {
        return new Gebruiker(uuid, name, username, password, toAdministraties());
    }

    private List<Administratie> toAdministraties() {
        return administraties
                .stream()
                .map(administratie -> administratie.toAdministratie())
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

    public List<AdministratieDto> getAdministraties() {
        return administraties;
    }

    public void setAdministraties(List<AdministratieDto> administraties) {
        this.administraties = administraties;
    }
}
