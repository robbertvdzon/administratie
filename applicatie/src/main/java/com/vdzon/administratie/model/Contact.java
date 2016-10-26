package com.vdzon.administratie.model;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

@Entity("contact")
public class Contact {

    @Id
    private String uuid;
    private String klantNummer;
    private String naam;
    private String tenNameVan;
    private String woonplaats;
    private String adres;
    private String postcode;
    private String land;

    public Contact() {
    }

    private Contact(Builder builder) {
        uuid = builder.uuid;
        klantNummer = builder.klantNummer;
        naam = builder.naam;
        tenNameVan = builder.tenNameVan;
        woonplaats = builder.woonplaats;
        adres = builder.adres;
        postcode = builder.postcode;
        land = builder.land;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static Builder newBuilder(Contact copy) {
        Builder builder = new Builder();
        builder.uuid = copy.uuid;
        builder.klantNummer = copy.klantNummer;
        builder.naam = copy.naam;
        builder.tenNameVan = copy.tenNameVan;
        builder.woonplaats = copy.woonplaats;
        builder.adres = copy.adres;
        builder.postcode = copy.postcode;
        builder.land = copy.land;
        return builder;
    }

    public String getUuid() {
        return uuid;
    }

    public String getKlantNummer() {
        return klantNummer;
    }

    public String getNaam() {
        return naam;
    }

    public String getTenNameVan() {
        return tenNameVan;
    }

    public String getWoonplaats() {
        return woonplaats;
    }

    public String getAdres() {
        return adres;
    }

    public String getPostcode() {
        return postcode;
    }

    public String getLand() {
        return land;
    }

    public static final class Builder {
        private String uuid;
        private String klantNummer;
        private String naam;
        private String tenNameVan;
        private String woonplaats;
        private String adres;
        private String postcode;
        private String land;

        private Builder() {
        }

        public Builder uuid(String val) {
            uuid = val;
            return this;
        }

        public Builder klantNummer(String val) {
            klantNummer = val;
            return this;
        }

        public Builder naam(String val) {
            naam = val;
            return this;
        }

        public Builder tenNameVan(String val) {
            tenNameVan = val;
            return this;
        }

        public Builder woonplaats(String val) {
            woonplaats = val;
            return this;
        }

        public Builder adres(String val) {
            adres = val;
            return this;
        }

        public Builder postcode(String val) {
            postcode = val;
            return this;
        }

        public Builder land(String val) {
            land = val;
            return this;
        }

        public Contact build() {
            return new Contact(this);
        }
    }
}
