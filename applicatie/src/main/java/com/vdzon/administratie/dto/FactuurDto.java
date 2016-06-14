package com.vdzon.administratie.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.vdzon.administratie.model.Factuur;
import com.vdzon.administratie.model.FactuurRegel;
import lombok.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
@ToString
@EqualsAndHashCode
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class FactuurDto {

    private String uuid;
    private String factuurNummer;
    private String gekoppeldeBestellingNummer;
    private String factuurDate;
    private ContactDto klant;
    private boolean betaald;
    private List<FactuurRegelDto> factuurRegels;
    private double bedragExBtw = 0;
    private double bedragIncBtw = 0;
    private double btw = 0;
    private String gekoppeldAfschrift;

    private static DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public FactuurDto(Factuur factuur){
        this.uuid = factuur.getUuid();
        this.factuurNummer = factuur.getFactuurNummer();
        this.gekoppeldeBestellingNummer = factuur.getGekoppeldeBestellingNummer();
        this.factuurDate = factuur.getFactuurDate()==null ? null : factuur.getFactuurDate().format(DATE_FORMATTER);
        this.klant = factuur.getContact()==null ? null : new ContactDto(factuur.getContact());
        this.betaald = factuur.isBetaald();
        this.factuurRegels = toFactuurRegelsDto(factuur.getFactuurRegels());
        this.bedragExBtw = factuur.getBedragExBtw();
        this.bedragIncBtw = factuur.getBedragIncBtw();
        this.btw = factuur.getBtw();
        this.gekoppeldAfschrift = factuur.getGekoppeldAfschrift();
    }

    private static List<FactuurRegelDto> toFactuurRegelsDto(List<FactuurRegel> factuurRegels) {
        return factuurRegels == null ? new ArrayList<>() : factuurRegels
                .stream()
                .map(factuurRegel -> new FactuurRegelDto(factuurRegel))
                .collect(Collectors.toList());
    }

    public Factuur toFactuur() {
        return new Factuur(factuurNummer, gekoppeldeBestellingNummer, LocalDate.parse(factuurDate,DATE_FORMATTER), klant == null ? null : klant.toContact(), betaald, toFactuurRegels(), uuid, gekoppeldAfschrift);
    }

    private List<FactuurRegel> toFactuurRegels() {
        return factuurRegels == null ? new ArrayList<>() : factuurRegels
                .stream()
                .map(factuurRegelDto -> factuurRegelDto.toFactuurRegel())
                .collect(Collectors.toList());
    }

}
