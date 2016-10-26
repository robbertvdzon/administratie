package com.vdzon.administratie.dto;


import com.vdzon.administratie.model.Gebruiker;

public class GebruikerDto {

    private String uuid;
    private String name;
    private String username;
    private boolean admin;

    private GebruikerDto(Builder builder) {
        uuid = builder.uuid;
        name = builder.name;
        username = builder.username;
        admin = builder.admin;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static Builder newBuilder(GebruikerDto copy) {
        Builder builder = new Builder();
        builder.uuid = copy.uuid;
        builder.name = copy.name;
        builder.username = copy.username;
        builder.admin = copy.admin;
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

    public boolean isAdmin() {
        return admin;
    }

    public GebruikerDto(Gebruiker gebruiker) {
        this.uuid = gebruiker.getUuid();
        this.name = gebruiker.getName();
        this.username = gebruiker.getUsername();
        this.admin = gebruiker.isAdmin();
    }

    public Gebruiker cloneGebruikerWithDtoFields(Gebruiker gebruiker) {
        Gebruiker newGebruiker = Gebruiker.newBuilder(gebruiker)
                .username(username)
                .name(name)
                .uuid(uuid)
                .admin(admin).build();
        return newGebruiker;
    }

    public static final class Builder {
        private String uuid;
        private String name;
        private String username;
        private boolean admin;

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

        public Builder admin(boolean val) {
            admin = val;
            return this;
        }

        public GebruikerDto build() {
            return new GebruikerDto(this);
        }
    }
}
