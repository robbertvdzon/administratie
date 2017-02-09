package com.vdzon.administratie.model;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Entity("gebruiker")
public class Gebruiker {

    @Id
    private String uuid;
    private String name;
    private String username;
    private String password;
    private boolean admin;
    private List<Administratie> administraties = new ArrayList<>();

    public Gebruiker() {
    }

    private Gebruiker(Builder builder) {
        uuid = builder.uuid;
        name = builder.name;
        username = builder.username;
        password = builder.password;
        admin = builder.admin;
        administraties = builder.administraties;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static Builder newBuilder(Gebruiker copy) {
        Builder builder = new Builder();
        builder.uuid = copy.uuid;
        builder.name = copy.name;
        builder.username = copy.username;
        builder.password = copy.password;
        builder.admin = copy.admin;
        builder.administraties = copy.administraties;
        return builder;
    }

    public String getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public boolean isAdmin() {
        return admin;
    }

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
                    .newBuilder()
                    .uuid(UUID.randomUUID().toString()).name("mijn admin").build();
            administraties.add(
                    Administratie
                            .newBuilder()
                            .uuid(UUID.randomUUID().toString())
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

    public static final class Builder {
        private String uuid;
        private String name;
        private String username;
        private String password;
        private boolean admin;
        private List<Administratie> administraties;

        private Builder() {
        }

        public Builder uuid(String val) {
            uuid = val;
            return this;
        }

        public Builder name(String val) {
            name = val;
            return this;
        }

        public Builder username(String val) {
            username = val;
            return this;
        }

        public Builder password(String val) {
            password = val;
            return this;
        }

        public Builder admin(boolean val) {
            admin = val;
            return this;
        }

        public Builder administraties(List<Administratie> val) {
            administraties = val;
            return this;
        }

        public Gebruiker build() {
            return new Gebruiker(this);
        }
    }
}
