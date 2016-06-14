package com.vdzon.administratie.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.vdzon.administratie.model.AdministratieGegevens;
import lombok.*;

import java.util.UUID;

@ToString
@EqualsAndHashCode
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties
public class AdministratieGegevensDto {

    private String uuid;
    private String name;
    private String rekeningNummer;
    private String btwNummer;
    private String handelsRegister;
    private String adres;
    private String postcode;
    private String woonplaats;
    private String logoUrl;

    public AdministratieGegevensDto(AdministratieGegevens administratieGegegens) {
        if (administratieGegegens != null) {
            this.uuid = administratieGegegens.getUuid();
            this.name = administratieGegegens.getName();

            this.rekeningNummer = administratieGegegens.getRekeningNummer();
            this.btwNummer = administratieGegegens.getBtwNummer();
            this.handelsRegister = administratieGegegens.getHandelsRegister();
            this.adres = administratieGegegens.getAdres();
            this.postcode = administratieGegegens.getPostcode();
            this.woonplaats = administratieGegegens.getWoonplaats();
            this.logoUrl = administratieGegegens.getLogoUrl();
        } else {
            this.uuid = UUID.randomUUID().toString();
        }
    }

    public AdministratieGegevens toAdministratieGegevens() {
        return AdministratieGegevens.builder()
                .uuid(uuid)
                .name(name)
                .rekeningNummer(rekeningNummer)
                .btwNummer(btwNummer)
                .handelsRegister(handelsRegister)
                .adres(adres)
                .postcode(postcode)
                .woonplaats(woonplaats)
                .logoUrl(logoUrl)
                .build();
    }

}
