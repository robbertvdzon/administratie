package com.vdzon.administratie.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.vdzon.administratie.model.BoekingenCache;
import com.vdzon.administratie.model.Rekening;
import com.vdzon.administratie.model.boekingen.Boeking;
import com.vdzon.administratie.model.boekingen.relaties.BoekingMetAfschrift;
import com.vdzon.administratie.model.boekingen.relaties.BoekingMetRekening;
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
public class RekeningDto {
    private static DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private String uuid;
    private String rekeningNummer;
    private String factuurNummer;
    private String naam;
    private String omschrijving;
    private String rekeningDate;
    private double bedragExBtw = 0;
    private double bedragIncBtw = 0;
    private double btw = 0;
    private List<BoekingDto> boekingen;
    private int maandenAfschrijving = 0;

    public RekeningDto(Rekening rekening, BoekingenCache boekingenCache) {
        this.uuid = rekening.getUuid();
        this.rekeningNummer = rekening.getRekeningNummer();
        this.factuurNummer = rekening.getFactuurNummer();
        this.naam = rekening.getNaam();
        this.omschrijving = rekening.getOmschrijving();
        this.rekeningDate = rekening.getRekeningDate() == null ? null : rekening.getRekeningDate().format(DATE_FORMATTER);
        this.bedragExBtw = rekening.getBedragExBtw();
        this.bedragIncBtw = rekening.getBedragIncBtw();
        this.btw = rekening.getBtw();
        this.boekingen = toBoekingenDto(boekingenCache.getBoekingenVanRekening(rekeningNummer), boekingenCache);
        this.maandenAfschrijving = rekening.getMaandenAfschrijving();
    }

    private List<BoekingDto> toBoekingenDto(List<BoekingMetRekening> boekingen, BoekingenCache boekingenCache) {
        return boekingen == null ? null : boekingen
                .stream()
                .map(boeking -> new BoekingDto((Boeking)boeking))
                .collect(Collectors.toList());
    }

    public Rekening toRekening() {
        return Rekening.builder()
                .uuid(uuid)
                .rekeningNummer(rekeningNummer)
                .factuurNummer(factuurNummer)
                .naam(naam)
                .omschrijving(omschrijving)
                .rekeningDate(LocalDate.parse(rekeningDate, DATE_FORMATTER))
                .bedragExBtw(bedragExBtw)
                .bedragIncBtw(bedragIncBtw)
                .btw(btw)
                .maandenAfschrijving(maandenAfschrijving)
                .build();
    }

}
