package com.vdzon.administratie.model;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

@Entity("administratieGegevens")
public class AdministratieGegevens {

    @Id
    private String uuid;
    private String name;
    private String rekeningNummer;
    private String btwNummer;
    private String handelsRegister;
    private String adres;
    private String postcode;
    private String woonplaats;
    private String logoUrl;

    public AdministratieGegevens() {
    }

    private AdministratieGegevens(Builder builder) {
        uuid = builder.uuid;
        name = builder.name;
        rekeningNummer = builder.rekeningNummer;
        btwNummer = builder.btwNummer;
        handelsRegister = builder.handelsRegister;
        adres = builder.adres;
        postcode = builder.postcode;
        woonplaats = builder.woonplaats;
        logoUrl = builder.logoUrl;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static Builder newBuilder(AdministratieGegevens copy) {
        Builder builder = new Builder();
        builder.uuid = copy.uuid;
        builder.name = copy.name;
        builder.rekeningNummer = copy.rekeningNummer;
        builder.btwNummer = copy.btwNummer;
        builder.handelsRegister = copy.handelsRegister;
        builder.adres = copy.adres;
        builder.postcode = copy.postcode;
        builder.woonplaats = copy.woonplaats;
        builder.logoUrl = copy.logoUrl;
        return builder;
    }

    public String getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public String getRekeningNummer() {
        return rekeningNummer;
    }

    public String getBtwNummer() {
        return btwNummer;
    }

    public String getHandelsRegister() {
        return handelsRegister;
    }

    public String getAdres() {
        return adres;
    }

    public String getPostcode() {
        return postcode;
    }

    public String getWoonplaats() {
        return woonplaats;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public static final class Builder {
        private String uuid;
        private String name;
        private String rekeningNummer;
        private String btwNummer;
        private String handelsRegister;
        private String adres;
        private String postcode;
        private String woonplaats;
        private String logoUrl;

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

        public Builder rekeningNummer(String val) {
            rekeningNummer = val;
            return this;
        }

        public Builder btwNummer(String val) {
            btwNummer = val;
            return this;
        }

        public Builder handelsRegister(String val) {
            handelsRegister = val;
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

        public Builder woonplaats(String val) {
            woonplaats = val;
            return this;
        }

        public Builder logoUrl(String val) {
            logoUrl = val;
            return this;
        }

        public AdministratieGegevens build() {
            return new AdministratieGegevens(this);
        }
    }
}
