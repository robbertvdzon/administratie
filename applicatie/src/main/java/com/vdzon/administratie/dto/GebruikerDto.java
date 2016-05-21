package com.vdzon.administratie.dto;


import com.vdzon.administratie.model.Administratie;
import com.vdzon.administratie.model.Gebruiker;

import java.util.List;
import java.util.stream.Collectors;

public class GebruikerDto {

    private String uuid;
    private String name;
    private String username;
    private boolean admin;

    public GebruikerDto() {
    }

    public GebruikerDto(Gebruiker gebruiker) {
        this.uuid = gebruiker.getUuid();
        this.name = gebruiker.getName();
        this.username = gebruiker.getUsername();
        this.admin = gebruiker.isAdmin();
    }

    public Gebruiker cloneGebruikerAndCopyDtoFields(Gebruiker gebruiker) {
        Gebruiker newGebruiker = new Gebruiker(gebruiker);
        newGebruiker.setUsername(username);
        newGebruiker.setName(name);
        newGebruiker.setUuid(uuid);
        newGebruiker.setAdmin(admin);
        return newGebruiker;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }
}
