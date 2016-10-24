package com.vdzon.administratie.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.vdzon.administratie.model.Contact;
import lombok.*;

@ToString
@EqualsAndHashCode
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties
public class ContactDto {

    private String uuid;
    private String klantNummer;
    private String naam;
    private String tenNameVan;
    private String woonplaats;
    private String adres;
    private String postcode;
    private String land;

    public ContactDto(Contact contact) {
        this.uuid = contact.getUuid();
        this.klantNummer = contact.getKlantNummer();
        this.naam = contact.getNaam();
        this.tenNameVan = contact.getTenNameVan();
        this.woonplaats = contact.getWoonplaats();
        this.adres = contact.getAdres();
        this.postcode = contact.getPostcode();
        this.land = contact.getLand();
    }

    public Contact toContact() {
        return Contact.builder()
                .uuid(uuid)
                .klantNummer(klantNummer)
                .naam(naam)
                .tenNameVan(tenNameVan)
                .woonplaats(woonplaats)
                .adres(adres)
                .postcode(postcode)
                .land(land)
                .build();

    }
}
