package com.vdzon.administratie.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.vdzon.administratie.model.Bestelling;
import com.vdzon.administratie.model.BestellingRegel;
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
@JsonIgnoreProperties
public class BestellingDto {

    private String uuid;
    private String bestellingNummer;
    private String gekoppeldFactuurNummer;
    private String bestellingDate;
    private ContactDto klant;
    private List<BestellingRegelDto> bestellingRegels;
    private double bedragExBtw = 0;
    private double bedragIncBtw = 0;
    private double btw = 0;

    private static DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public BestellingDto(Bestelling bestelling){
        this.uuid = bestelling.getUuid();
        this.bestellingNummer = bestelling.getBestellingNummer();
        this.gekoppeldFactuurNummer = bestelling.getGekoppeldFactuurNummer();
        this.bestellingDate = bestelling.getBestellingDate()==null ? null : bestelling.getBestellingDate().format(DATE_FORMATTER);
        this.klant = bestelling.getContact()==null ? null : new ContactDto(bestelling.getContact());
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
        return new Bestelling(bestellingNummer, gekoppeldFactuurNummer, LocalDate.parse(bestellingDate,DATE_FORMATTER), klant == null ? null : klant.toContact(), toBestellingRegels(), uuid);
    }

    private List<BestellingRegel> toBestellingRegels() {
        return bestellingRegels == null ? new ArrayList<>() : bestellingRegels
                .stream()
                .map(bestellingRegelDto -> bestellingRegelDto.toBestellingRegel())
                .collect(Collectors.toList());
    }

}
