package com.vdzon.administratie.dto;


import com.vdzon.administratie.model.Gebruiker;
import lombok.*;


@ToString
@EqualsAndHashCode
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class GebruikerDto {

    private String uuid;
    private String name;
    private String username;
    private boolean admin;

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

}
