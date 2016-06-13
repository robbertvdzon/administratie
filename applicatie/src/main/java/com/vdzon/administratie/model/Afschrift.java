package com.vdzon.administratie.model;


import lombok.*;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import java.time.LocalDate;

@ToString
@EqualsAndHashCode
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor

@Entity("afschrift")
public class Afschrift {

    @Id
    private String uuid;
    private String nummer;
    private String rekening;
    private String omschrijving;
    private String relatienaam;
    private LocalDate boekdatum;
    private double bedrag;
    private BoekingType boekingType;
    private String factuurNummer;
    private String rekeningNummer;


}
