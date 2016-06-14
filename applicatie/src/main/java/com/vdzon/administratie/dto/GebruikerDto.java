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

    public Gebruiker cloneGebruikerWithDtoFields(Gebruiker gebruiker) {
        Gebruiker newGebruiker = gebruiker
                .toBuilder()
                .username(username)
                .name(name)
                .uuid(uuid)
                .admin(admin).build();
        return newGebruiker;
    }

}
