package com.vdzon.administratie.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.vdzon.administratie.model.Declaratie;
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
public class DeclaratieDto {

    private String uuid;
    private String declaratieNummer;
    private String omschrijving;
    private String declaratieDate;
    private double bedragExBtw = 0;
    private double bedragIncBtw = 0;
    private double btw = 0;

    private static DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public DeclaratieDto(Declaratie declaratie) {
        this.uuid = declaratie.getUuid();
        this.declaratieNummer = declaratie.getDeclaratieNummer();
        this.omschrijving = declaratie.getOmschrijving();
        this.declaratieDate = declaratie.getDeclaratieDate()==null ? null : declaratie.getDeclaratieDate().format(DATE_FORMATTER);
        this.bedragExBtw = declaratie.getBedragExBtw();
        this.bedragIncBtw = declaratie.getBedragIncBtw();
        this.btw = declaratie.getBtw();
    }

    public Declaratie toDeclaratie() {
        return new Declaratie(uuid, declaratieNummer, omschrijving, LocalDate.parse(declaratieDate,DATE_FORMATTER), bedragExBtw, bedragIncBtw, btw);
    }
}
