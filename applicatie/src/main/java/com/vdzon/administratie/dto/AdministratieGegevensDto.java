package com.vdzon.administratie.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.vdzon.administratie.model.AdministratieGegevens;

import java.util.UUID;

@JsonIgnoreProperties
public class AdministratieGegevensDto {

    private String uuid;
    private String name;
    private String rekeningNummer;
    private String btwNummer;
    private String handelsRegister;
    private String adres;
    private String postcode;
    private String woonplaats;
    private String logoUrl;

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

    public AdministratieGegevensDto(AdministratieGegevens administratieGegegens) {
        if (administratieGegegens != null) {
            this.uuid = administratieGegegens.getUuid();
            this.name = administratieGegegens.getName();

            this.rekeningNummer = administratieGegegens.getRekeningNummer();
            this.btwNummer = administratieGegegens.getBtwNummer();
            this.handelsRegister = administratieGegegens.getHandelsRegister();
            this.adres = administratieGegegens.getAdres();
            this.postcode = administratieGegegens.getPostcode();
            this.woonplaats = administratieGegegens.getWoonplaats();
            this.logoUrl = administratieGegegens.getLogoUrl();
        } else {
            this.uuid = UUID.randomUUID().toString();
        }
    }

    private AdministratieGegevensDto(Builder builder) {
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

    public static Builder newBuilder(AdministratieGegevensDto copy) {
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

    public AdministratieGegevens toAdministratieGegevens() {
        return AdministratieGegevens.newBuilder()
                .uuid(uuid)
                .name(name)
                .rekeningNummer(rekeningNummer)
                .btwNummer(btwNummer)
                .handelsRegister(handelsRegister)
                .adres(adres)
                .postcode(postcode)
                .woonplaats(woonplaats)
                .logoUrl(logoUrl)
                .build();
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

        public AdministratieGegevensDto build() {
            return new AdministratieGegevensDto(this);
        }
    }
}
