package com.vdzon.administratie.model;

import lombok.*;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

@ToString
@EqualsAndHashCode
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor
@Entity("contact")
public class Contact {

    @Id
    private String uuid;
    private String klantNummer;
    private String naam;
    private String woonplaats;
    private String adres;
    private String postcode;
    private String land;


}
