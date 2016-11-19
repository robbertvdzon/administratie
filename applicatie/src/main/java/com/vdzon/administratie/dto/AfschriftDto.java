package com.vdzon.administratie.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.vdzon.administratie.model.Afschrift;
import com.vdzon.administratie.model.BoekingenCache;
import com.vdzon.administratie.model.boekingen.Boeking;
import com.vdzon.administratie.model.boekingen.relaties.BoekingMetAfschrift;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@JsonIgnoreProperties
public class AfschriftDto {

    private String uuid;
    private String nummer;
    private String rekening;
    private String omschrijving;
    private String relatienaam;
    private String boekdatum;
    private double bedrag = 0;
    private List<BoekingDto> boekingen;

    private static DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public AfschriftDto() {
    }

    private AfschriftDto(Builder builder) {
        uuid = builder.uuid;
        nummer = builder.nummer;
        rekening = builder.rekening;
        omschrijving = builder.omschrijving;
        relatienaam = builder.relatienaam;
        boekdatum = builder.boekdatum;
        bedrag = builder.bedrag;
        boekingen = builder.boekingen;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static Builder newBuilder(AfschriftDto copy) {
        Builder builder = new Builder();
        builder.uuid = copy.uuid;
        builder.nummer = copy.nummer;
        builder.rekening = copy.rekening;
        builder.omschrijving = copy.omschrijving;
        builder.relatienaam = copy.relatienaam;
        builder.boekdatum = copy.boekdatum;
        builder.bedrag = copy.bedrag;
        builder.boekingen = copy.boekingen;
        return builder;
    }

    public String getUuid() {
        return uuid;
    }

    public String getNummer() {
        return nummer;
    }

    public String getRekening() {
        return rekening;
    }

    public String getOmschrijving() {
        return omschrijving;
    }

    public String getRelatienaam() {
        return relatienaam;
    }

    public String getBoekdatum() {
        return boekdatum;
    }

    public double getBedrag() {
        return bedrag;
    }

    public List<BoekingDto> getBoekingen() {
        return boekingen;
    }

    public AfschriftDto (Afschrift afschrift, BoekingenCache boekingenCache) {
        uuid = afschrift.uuid();
        nummer = afschrift.nummer();
        rekening = afschrift.rekening();
        omschrijving = afschrift.omschrijving();
        relatienaam = afschrift.relatienaam();
        boekdatum = afschrift.boekdatum()==null ? null : afschrift.boekdatum().format(DATE_FORMATTER);
        bedrag =  afschrift.bedrag();
        boekingen = toBoekingenDto(boekingenCache.getBoekingenVanAfschrift(nummer), boekingenCache);
    }

    private List<BoekingDto> toBoekingenDto(List<BoekingMetAfschrift> boekingen, BoekingenCache boekingenCache) {
        return boekingen == null ? null : boekingen
                .stream()
                .map(boeking -> new BoekingDto((Boeking)boeking))
                .collect(Collectors.toList());
    }

    public Afschrift toAfschrift() {
        return new Afschrift(uuid, nummer, rekening, omschrijving, relatienaam, LocalDate.parse(boekdatum,DATE_FORMATTER), bedrag);
    }

    public static final class Builder {
        private String uuid;
        private String nummer;
        private String rekening;
        private String omschrijving;
        private String relatienaam;
        private String boekdatum;
        private double bedrag;
        private List<BoekingDto> boekingen;

        private Builder() {
        }

        public Builder uuid(String val) {
            uuid = val;
            return this;
        }

        public Builder nummer(String val) {
            nummer = val;
            return this;
        }

        public Builder rekening(String val) {
            rekening = val;
            return this;
        }

        public Builder omschrijving(String val) {
            omschrijving = val;
            return this;
        }

        public Builder relatienaam(String val) {
            relatienaam = val;
            return this;
        }

        public Builder boekdatum(String val) {
            boekdatum = val;
            return this;
        }

        public Builder bedrag(double val) {
            bedrag = val;
            return this;
        }

        public Builder boekingen(List<BoekingDto> val) {
            boekingen = val;
            return this;
        }

        public AfschriftDto build() {
            return new AfschriftDto(this);
        }
    }
}
