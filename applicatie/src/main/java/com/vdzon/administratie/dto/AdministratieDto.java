package com.vdzon.administratie.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.vdzon.administratie.model.*;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@ToString
@EqualsAndHashCode
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties
public class AdministratieDto {

    private String uuid;
    private AdministratieGegevensDto administratieGegevens;
    private List<FactuurDto> facturen;
    private List<ContactDto> adresboek;
    private List<RekeningDto> rekeningen;
    private List<AfschriftDto> afschriften;
    private List<DeclaratieDto> declaraties;
    private List<BestellingDto> bestellingen;


    public AdministratieDto(Administratie administratie) {
        BoekingenCache boekingenCache = new BoekingenCache(administratie.getBoekingen());
        this.uuid = administratie.getUuid();
        this.administratieGegevens = new AdministratieGegevensDto(administratie.getAdministratieGegevens());
        this.adresboek = toAdressenDto(administratie.getAdresboek());
        this.facturen = toFacturenDto(administratie.getFacturen(), boekingenCache);
        this.rekeningen = toRekeningenDto(administratie.getRekeningen(), boekingenCache);
        this.afschriften = toAfschriftenDto(administratie.getAfschriften(), boekingenCache);
        this.declaraties = toDeclaratiesDto(administratie.getDeclaraties(), boekingenCache);
        this.bestellingen = toBestellingenDto(administratie.getBestellingen());

    }

    private List<BestellingDto> toBestellingenDto(List<Bestelling> bestellingen) {
        return bestellingen
                .stream()
                .map(bestelling -> new BestellingDto(bestelling))
                .sorted((rekeningDto1, rekeningDto2) -> rekeningDto2.getBestellingNummer().compareTo(rekeningDto1.getBestellingNummer()))
                .collect(Collectors.toList());
    }

    private List<RekeningDto> toRekeningenDto(List<Rekening> rekeningen, BoekingenCache boekingenCache) {
        return rekeningen
                .stream()
                .map(rekening -> new RekeningDto(rekening, boekingenCache))
                .sorted((rekeningDto1, rekeningDto2) -> rekeningDto2.getRekeningNummer().compareTo(rekeningDto1.getRekeningNummer()))
                .collect(Collectors.toList());
    }

    private List<AfschriftDto> toAfschriftenDto(List<Afschrift> afschriften, BoekingenCache boekingenCache) {
        return afschriften
                .stream()
                .map(afschrift -> new AfschriftDto(afschrift, boekingenCache))
                .sorted((afschriftDto1, afschriftDto2) -> afschriftDto2.getNummer().compareTo(afschriftDto1.getNummer()))
                .collect(Collectors.toList());
    }

    private List<DeclaratieDto> toDeclaratiesDto(List<Declaratie> declaraties, BoekingenCache boekingenCache) {
        return declaraties
                .stream()
                .map(declaratie -> new DeclaratieDto(declaratie, boekingenCache))
                .sorted((declaratieDto1, declaratieDto2) -> declaratieDto2.getDeclaratieNummer().compareTo(declaratieDto1.getDeclaratieNummer()))
                .collect(Collectors.toList());
    }

    private List<ContactDto> toAdressenDto(List<Contact> klanten) {
        return klanten
                .stream()
                .map(klant -> new ContactDto(klant))
                .sorted((ContactDto1, ContactDto2) -> ContactDto2.getKlantNummer().compareTo(ContactDto1.getKlantNummer()))
                .collect(Collectors.toList());
    }

    private List<FactuurDto> toFacturenDto(List<Factuur> facturen, BoekingenCache boekingenCache) {
        return facturen
                .stream()
                .map(factuur -> new FactuurDto(factuur, boekingenCache))
                .sorted((factuurDto1, factuurDto2) -> factuurDto2.getFactuurNummer().compareTo(factuurDto1.getFactuurNummer()))
                .collect(Collectors.toList());
    }

    public Administratie toAdministratie() {
        return Administratie.builder()
                .uuid(uuid)
                .bestellingen(toBestellingen())
                .facturen(toFacturen())
                .adresboek(toAdressen())
                .rekeningen(toRekeningen())
                .afschriften(toAfschriften())
                .declaraties(toDeclaraties())
                .administratieGegevens(administratieGegevens.toAdministratieGegevens()).build();
    }

    private List<Bestelling> toBestellingen() {
        return bestellingen
                .stream()
                .map(bestelling -> bestelling.toBestelling())
                .collect(Collectors.toList());
    }

    private List<Factuur> toFacturen() {
        return facturen
                .stream()
                .map(factuur -> factuur.toFactuur())
                .collect(Collectors.toList());
    }

    private List<Contact> toAdressen() {
        return adresboek
                .stream()
                .map(klant -> klant.toContact())
                .collect(Collectors.toList());
    }

    private List<Rekening> toRekeningen() {
        return rekeningen
                .stream()
                .map(rekening -> rekening.toRekening())
                .collect(Collectors.toList());
    }

    private List<Declaratie> toDeclaraties() {
        return declaraties
                .stream()
                .map(declaratie -> declaratie.toDeclaratie())
                .collect(Collectors.toList());
    }

    private List<Afschrift> toAfschriften() {
        return afschriften
                .stream()
                .map(afschrift -> afschrift.toAfschrift())
                .collect(Collectors.toList());
    }


}
