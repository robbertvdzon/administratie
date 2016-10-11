package com.vdzon.administratie.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.vdzon.administratie.model.Afschrift;
import com.vdzon.administratie.model.BoekingenCache;
import com.vdzon.administratie.model.boekingen.Boeking;
import com.vdzon.administratie.model.boekingen.relaties.BoekingMetAfschrift;
import lombok.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

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
    private List<BoekingDto> boekingen;

    private static DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public AfschriftDto (Afschrift afschrift, BoekingenCache boekingenCache) {
        uuid = afschrift.getUuid();
        nummer = afschrift.getNummer();
        rekening = afschrift.getRekening();
        omschrijving = afschrift.getOmschrijving();
        relatienaam = afschrift.getRelatienaam();
        boekdatum = afschrift.getBoekdatum()==null ? null : afschrift.getBoekdatum().format(DATE_FORMATTER);
        bedrag =  afschrift.getBedrag();
        boekingen = toBoekingenDto(boekingenCache.getBoekingenVanAfschrift(nummer), boekingenCache);
    }

    private List<BoekingDto> toBoekingenDto(List<BoekingMetAfschrift> boekingen, BoekingenCache boekingenCache) {
        return boekingen == null ? null : boekingen
                .stream()
                .map(boeking -> new BoekingDto((Boeking)boeking))
                .collect(Collectors.toList());
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
                .build();
    }

}
