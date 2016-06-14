package com.vdzon.administratie.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.vdzon.administratie.model.Rekening;
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
public class RekeningDto {
    private String uuid;
    private String rekeningNummer;
    private String factuurNummer;
    private String naam;
    private String omschrijving;
    private String rekeningDate;
    private double bedragExBtw = 0;
    private double bedragIncBtw = 0;
    private double btw = 0;
    private String gekoppeldAfschrift;

    private static DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public RekeningDto(Rekening rekening) {
        this.uuid = rekening.getUuid();
        this.rekeningNummer = rekening.getRekeningNummer();
        this.factuurNummer = rekening.getFactuurNummer();
        this.naam = rekening.getNaam();
        this.omschrijving = rekening.getOmschrijving();
        this.rekeningDate = rekening.getRekeningDate()==null ? null : rekening.getRekeningDate().format(DATE_FORMATTER);
        this.bedragExBtw = rekening.getBedragExBtw();
        this.bedragIncBtw = rekening.getBedragIncBtw();
        this.btw = rekening.getBtw();
        this.gekoppeldAfschrift = rekening.getGekoppeldAfschrift();
    }

    public Rekening toRekening() {
        return new Rekening(uuid, rekeningNummer,factuurNummer, naam, omschrijving, LocalDate.parse(rekeningDate,DATE_FORMATTER), bedragExBtw, bedragIncBtw, btw, gekoppeldAfschrift);
    }

}
