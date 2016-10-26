package com.vdzon.administratie.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.vdzon.administratie.model.Bestelling;
import com.vdzon.administratie.model.BestellingRegel;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@JsonIgnoreProperties
public class BestellingDto {

    private static DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private String uuid;
    private String bestellingNummer;
    private String gekoppeldFactuurNummer;
    private String bestellingDate;
    private ContactDto klant;
    private List<BestellingRegelDto> bestellingRegels;
    private double bedragExBtw = 0;
    private double bedragIncBtw = 0;
    private double btw = 0;

    private BestellingDto(Builder builder) {
        uuid = builder.uuid;
        bestellingNummer = builder.bestellingNummer;
        gekoppeldFactuurNummer = builder.gekoppeldFactuurNummer;
        bestellingDate = builder.bestellingDate;
        klant = builder.klant;
        bestellingRegels = builder.bestellingRegels;
        bedragExBtw = builder.bedragExBtw;
        bedragIncBtw = builder.bedragIncBtw;
        btw = builder.btw;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static Builder newBuilder(BestellingDto copy) {
        Builder builder = new Builder();
        builder.uuid = copy.uuid;
        builder.bestellingNummer = copy.bestellingNummer;
        builder.gekoppeldFactuurNummer = copy.gekoppeldFactuurNummer;
        builder.bestellingDate = copy.bestellingDate;
        builder.klant = copy.klant;
        builder.bestellingRegels = copy.bestellingRegels;
        builder.bedragExBtw = copy.bedragExBtw;
        builder.bedragIncBtw = copy.bedragIncBtw;
        builder.btw = copy.btw;
        return builder;
    }

    public String getUuid() {
        return uuid;
    }

    public String getBestellingNummer() {
        return bestellingNummer;
    }

    public String getGekoppeldFactuurNummer() {
        return gekoppeldFactuurNummer;
    }

    public String getBestellingDate() {
        return bestellingDate;
    }

    public ContactDto getKlant() {
        return klant;
    }

    public List<BestellingRegelDto> getBestellingRegels() {
        return bestellingRegels;
    }

    public double getBedragExBtw() {
        return bedragExBtw;
    }

    public double getBedragIncBtw() {
        return bedragIncBtw;
    }

    public double getBtw() {
        return btw;
    }

    public BestellingDto(Bestelling bestelling) {
        this.uuid = bestelling.getUuid();
        this.bestellingNummer = bestelling.getBestellingNummer();
        this.gekoppeldFactuurNummer = bestelling.getGekoppeldFactuurNummer();
        this.bestellingDate = bestelling.getBestellingDate() == null ? null : bestelling.getBestellingDate().format(DATE_FORMATTER);
        this.klant = bestelling.getContact() == null ? null : new ContactDto(bestelling.getContact());
        this.bestellingRegels = toBestellingRegelsDto(bestelling.getBestellingRegels());
        this.bedragExBtw = bestelling.getBedragExBtw();
        this.bedragIncBtw = bestelling.getBedragIncBtw();
        this.btw = bestelling.getBtw();
    }

    private static List<BestellingRegelDto> toBestellingRegelsDto(List<BestellingRegel> bestellingRegels) {
        return bestellingRegels == null ? new ArrayList<>() : bestellingRegels
                .stream()
                .map(bestellingRegel -> new BestellingRegelDto(bestellingRegel))
                .collect(Collectors.toList());
    }

    public Bestelling toBestelling() {
        return Bestelling.newBuilder()
                .bestellingNummer(bestellingNummer)
                .gekoppeldFactuurNummer(gekoppeldFactuurNummer)
                .bestellingDate(LocalDate.parse(bestellingDate, DATE_FORMATTER))
                .contact(klant == null ? null : klant.toContact())
                .bestellingRegels(toBestellingRegels())
                .uuid(uuid).build();
    }

    private List<BestellingRegel> toBestellingRegels() {
        return bestellingRegels == null ? new ArrayList<>() : bestellingRegels
                .stream()
                .map(bestellingRegelDto -> bestellingRegelDto.toBestellingRegel())
                .collect(Collectors.toList());
    }

    public static final class Builder {
        private String uuid;
        private String bestellingNummer;
        private String gekoppeldFactuurNummer;
        private String bestellingDate;
        private ContactDto klant;
        private List<BestellingRegelDto> bestellingRegels;
        private double bedragExBtw;
        private double bedragIncBtw;
        private double btw;

        private Builder() {
        }

        public Builder uuid(String val) {
            uuid = val;
            return this;
        }

        public Builder bestellingNummer(String val) {
            bestellingNummer = val;
            return this;
        }

        public Builder gekoppeldFactuurNummer(String val) {
            gekoppeldFactuurNummer = val;
            return this;
        }

        public Builder bestellingDate(String val) {
            bestellingDate = val;
            return this;
        }

        public Builder klant(ContactDto val) {
            klant = val;
            return this;
        }

        public Builder bestellingRegels(List<BestellingRegelDto> val) {
            bestellingRegels = val;
            return this;
        }

        public Builder bedragExBtw(double val) {
            bedragExBtw = val;
            return this;
        }

        public Builder bedragIncBtw(double val) {
            bedragIncBtw = val;
            return this;
        }

        public Builder btw(double val) {
            btw = val;
            return this;
        }

        public BestellingDto build() {
            return new BestellingDto(this);
        }
    }
}
