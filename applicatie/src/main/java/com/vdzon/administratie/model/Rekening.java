package com.vdzon.administratie.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import java.time.LocalDate;

@ToString
@EqualsAndHashCode
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor
@Entity("rekening")
public class Rekening {

    @Id
    private String uuid;
    private String rekeningNummer;
    private String factuurNummer;
    private String naam;
    private String omschrijving;
    private LocalDate rekeningDate;
    private double bedragExBtw = 0;
    private double bedragIncBtw = 0;
    private double btw = 0;
    private int maandenAfschrijving = 0;

}
