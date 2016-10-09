package com.vdzon.administratie.model;

import lombok.*;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@ToString
@EqualsAndHashCode
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor
@Entity("gebruiker")
public class Gebruiker {

    @Id
    private String uuid;
    private String name;
    private String username;
    private String password;
    private boolean admin;
    private List<Administratie> administraties = new ArrayList<>();


    public List<Administratie> getAdministraties() {
        initDefaultAdministratie();
        return Collections.unmodifiableList(new ArrayList<>(administraties));
    }

    public Administratie getDefaultAdministratie() {
        initDefaultAdministratie();
        return administraties.get(0);
    }

    public void initDefaultAdministratie() {
        if (administraties == null) {
            administraties = new ArrayList<>();
        }
        if (administraties.size() == 0) {
            AdministratieGegevens administratieGegevens = AdministratieGegevens
                    .builder()
                    .uuid(UUID.randomUUID().toString()).name("mijn admin").build();
            administraties.add(
                    Administratie
                            .builder()
                            .uuid(getUuid())
                            .administratieGegevens(administratieGegevens)
                            .bestellingen(new ArrayList<>())
                            .facturen(new ArrayList<>())
                            .adresboek(new ArrayList<>())
                            .rekeningen(new ArrayList<>())
                            .afschriften(new ArrayList<>())
                            .boekingen(new ArrayList<>())
                            .declaraties(new ArrayList<>())
                            .build());
        }
    }

    public void addAdministratie(Administratie administratie) {
        administraties.add(administratie);
    }

    public void removeAdministratie(String uuid) {
        Administratie administratieToRemove = null;
        for (Administratie administratie : getAdministraties()) {
            if (administratieNummerMatchesUuid(uuid, administratie)) {
                administratieToRemove = administratie;
            }
        }
        if (administratieToRemove != null) {
            administraties.remove(administratieToRemove);
        }
    }

    private boolean administratieNummerMatchesUuid(String uuid, Administratie administratie) {
        return uuid == null && administratie.getUuid() == null || uuid != null && uuid.equals(administratie.getUuid());
    }

    public boolean authenticate(String password) {
        return getPassword().equals(password);
    }

}
