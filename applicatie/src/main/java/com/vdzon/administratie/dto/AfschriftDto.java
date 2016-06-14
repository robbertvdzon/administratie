package com.vdzon.administratie.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.vdzon.administratie.model.Afschrift;
import com.vdzon.administratie.model.BoekingType;
import lombok.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@ToString
@EqualsAndHashCode
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor

@JsonIgnoreProperties
public class AfschriftDto {

    private String uuid;
    private String nummer;
    private String rekening;
    private String omschrijving;
    private String relatienaam;
    private String boekdatum;
    private double bedrag = 0;
    private BoekingType boekingType;
    private String factuurNummer;
    private String rekeningNummer;

    private static DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public AfschriftDto (Afschrift afschrift) {
        uuid = afschrift.getUuid();
        nummer = afschrift.getNummer();
        rekening = afschrift.getRekening();
        omschrijving = afschrift.getOmschrijving();
        relatienaam = afschrift.getRelatienaam();
        boekdatum = afschrift.getBoekdatum()==null ? null : afschrift.getBoekdatum().format(DATE_FORMATTER);
        bedrag =  afschrift.getBedrag();
        boekingType = afschrift.getBoekingType();
        factuurNummer = afschrift.getFactuurNummer();
        rekeningNummer = afschrift.getRekeningNummer();
    }

    public Afschrift toAfschrift() {
        return Afschrift.builder()
                .uuid(uuid)
                .nummer(nummer)
                .rekening(rekening)
                .omschrijving(omschrijving)
                .relatienaam(relatienaam)
                .boekdatum(LocalDate.parse(boekdatum,DATE_FORMATTER))
                .bedrag(bedrag)
                .boekingType(boekingType)
                .factuurNummer(factuurNummer)
                .rekeningNummer(rekeningNummer)
                .build();
    }

}
