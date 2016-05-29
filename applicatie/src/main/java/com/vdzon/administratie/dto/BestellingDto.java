package com.vdzon.administratie.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.vdzon.administratie.model.Bestelling;
import com.vdzon.administratie.model.BestellingRegel;
import com.vdzon.administratie.model.Factuur;
import com.vdzon.administratie.model.FactuurRegel;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BestellingDto {

    private String uuid;
    private String bestellingNummer;
    private String bestellingDate;
    private ContactDto klant;
    private List<BestellingRegelDto> bestellingRegels;
    private double bedragExBtw = 0;
    private double bedragIncBtw = 0;
    private double btw = 0;

    private static DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public BestellingDto() {
    }

    public BestellingDto(String uuid, String bestellingNummer, String bestellingDate, ContactDto klant, List<BestellingRegelDto> bestellingRegels, double bedragExBtw, double bedragIncBtw, double btw) {
        this.uuid = uuid;
        this.bestellingNummer = bestellingNummer;
        this.bestellingDate = bestellingDate;
        this.klant = klant;
        this.bestellingRegels = bestellingRegels;
        this.bedragExBtw = bedragExBtw;
        this.bedragIncBtw = bedragIncBtw;
        this.btw = btw;
    }

    public BestellingDto(Bestelling bestelling){
        this(bestelling.getUuid(), bestelling.getBestellingNummer(),bestelling.getBestellingDate()==null ? null : bestelling.getBestellingDate().format(DATE_FORMATTER), bestelling.getContact()==null ? null : new ContactDto(bestelling.getContact()),toBestellingRegelsDto(bestelling.getBestellingRegels()), bestelling.getBedragExBtw(), bestelling.getBedragIncBtw(), bestelling.getBtw());
    }

    private static List<BestellingRegelDto> toBestellingRegelsDto(List<BestellingRegel> bestellingRegels) {
        return bestellingRegels == null ? new ArrayList<>() : bestellingRegels
                .stream()
                .map(bestellingRegel -> new BestellingRegelDto(bestellingRegel))
                .collect(Collectors.toList());
    }

    public Bestelling toBestelling() {
        return new Bestelling(bestellingNummer, LocalDate.parse(bestellingDate,DATE_FORMATTER), klant == null ? null : klant.toContact(), toBestellingRegels(), uuid);
    }

    private List<BestellingRegel> toBestellingRegels() {
        return bestellingRegels == null ? new ArrayList<>() : bestellingRegels
                .stream()
                .map(bestellingRegelDto -> bestellingRegelDto.toBestellingRegel())
                .collect(Collectors.toList());
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getBestellingNummer() {
        return bestellingNummer;
    }

    public void setBestellingNummer(String bestellingNummer) {
        this.bestellingNummer = bestellingNummer;
    }

    public String getBestellingDate() {
        return bestellingDate;
    }

    public void setBestellingDate(String bestellingDate) {
        this.bestellingDate = bestellingDate;
    }

    public ContactDto getKlant() {
        return klant;
    }

    public void setKlant(ContactDto klant) {
        this.klant = klant;
    }

    public List<BestellingRegelDto> getBestellingRegels() {
        return bestellingRegels;
    }

    public void setBestellingRegels(List<BestellingRegelDto> bestellingRegels) {
        this.bestellingRegels = bestellingRegels;
    }

    public double getBedragExBtw() {
        return bedragExBtw;
    }

    public void setBedragExBtw(double bedragExBtw) {
        this.bedragExBtw = bedragExBtw;
    }

    public double getBedragIncBtw() {
        return bedragIncBtw;
    }

    public void setBedragIncBtw(double bedragIncBtw) {
        this.bedragIncBtw = bedragIncBtw;
    }

    public double getBtw() {
        return btw;
    }

    public void setBtw(double btw) {
        this.btw = btw;
    }

    public static DateTimeFormatter getDateFormatter() {
        return DATE_FORMATTER;
    }

    public static void setDateFormatter(DateTimeFormatter dateFormatter) {
        DATE_FORMATTER = dateFormatter;
    }
}
